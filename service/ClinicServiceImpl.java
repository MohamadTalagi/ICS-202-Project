package kfupm.clinic.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import kfupm.clinic.api.Result;
import kfupm.clinic.ds.*;
import kfupm.clinic.matching.KMPMatcher;
import kfupm.clinic.matching.NaiveMatcher;
import kfupm.clinic.matching.StringMatcher;
import kfupm.clinic.model.*;

public class ClinicServiceImpl implements ClinicService {

    // Hash tables
    private final HashTable<String, Patient> patientsById = new HashTable<>();
    private final HashTable<String, Appointment> apptsById = new HashTable<>();

    // Appointment schedule index (AVL)
    private final AVLTree<AppointmentKey, Appointment> apptsByTime = new AVLTree<>();

    // Walk-ins and urgent
    private final LinkedQueue<Patient> walkIns = new LinkedQueue<>();
    private final MaxHeap<UrgentPatient> urgentHeap = new MaxHeap<>((a, b) -> {
        if (a.severity() != b.severity()) {
            return Integer.compare(a.severity(), b.severity());
        }
        return Long.compare(b.arrivalEpochMillis(), a.arrivalEpochMillis());
    });

    private final LinkedStack<Action> undoStack = new LinkedStack<>();
    private final SinglyLinkedList<VisitLogEntry> log = new SinglyLinkedList<>();

    private final StringMatcher naive = new NaiveMatcher();
    private final StringMatcher kmp = new KMPMatcher();

    private int nextApptId = 1;

    @Override
    public Result<Void> addPatient(String id, String name, String phone) {
        if (id == null || name == null) {
            return Result.fail("Invalid patient data");
        }
        if (patientsById.get(id) != null) {
            return Result.fail("Patient already exists");
        }

        Patient p = new Patient(id, name, phone);
        patientsById.put(id, p);
        return Result.ok(null, "Patient added");
    }

    @Override
    public Result<Patient> findPatient(String id) {
        Patient p = patientsById.get(id);
        if (p == null) {
            return Result.fail("Patient not found");
        }
        return Result.ok(p, "Patient found");
    }

    @Override
    public Result<Void> deletePatient(String id) {
        Patient p = patientsById.remove(id);
        if (p == null) {
            return Result.fail("Patient not found");
        }
        return Result.ok(null, "Patient deleted");
    }

    @Override
    public Result<String> addAppointment(String patientId, LocalDate date, LocalTime time, String doctor) {
        if (patientId == null) {
            return Result.fail("Patient ID is required");
        }

        Patient patient = patientsById.get(patientId);
        if (patient == null) {
            return Result.fail("Patient not found");
        }

        String appointmentId = newAppointmentId();
        Appointment appointment = new Appointment(
                appointmentId,
                patient.id(),
                patient.name(),
                patient.phone(),
                date,
                time,
                doctor
        );

        AppointmentKey key = new AppointmentKey(date, time, appointmentId);

        apptsById.put(appointmentId, appointment);
        apptsByTime.put(key, appointment);

        undoStack.push(new Action(ActionType.ADD_APPT, appointment));

        return Result.ok(appointmentId, "Appointment scheduled successfully.");
    }

    @Override
    public Result<Void> cancelAppointment(String appointmentId) {
        if (appointmentId == null) {
            return Result.fail("Appointment ID is required.");
        }

        Appointment appointment = apptsById.remove(appointmentId);
        if (appointment == null) {
            return Result.fail("Appointment not found.");
        }

        AppointmentKey key = new AppointmentKey(
                appointment.date(),
                appointment.time(),
                appointment.appointmentId()
        );

        apptsByTime.remove(key);

        undoStack.push(new Action(ActionType.CANCEL_APPT, appointment));

        return Result.ok(null, "Appointment cancelled successfully.");
    }

    @Override
    public Result<Appointment> findAppointment(String appointmentId) {
        if (appointmentId == null) {
            return Result.fail("Appointment ID is required.");
        }

        Appointment appointment = apptsById.get(appointmentId);
        if (appointment == null) {
            return Result.fail("Appointment not found.");
        }

        return Result.ok(appointment, "Appointment found.");
    }

    @Override
    public List<Appointment> viewDay(LocalDate date) {
        List<Appointment> dayList = new ArrayList<>();
        if (date == null) {
            return dayList;
        }

        apptsByTime.inOrder((key, appt) -> {
            if (key.date().equals(date)) {
                dayList.add(appt);
            }
        });

        return dayList;
    }

    @Override
    public List<Appointment> viewRange(LocalDate date, LocalTime start, LocalTime end) {
        List<Appointment> rangeList = new ArrayList<>();
        if (date == null || start == null || end == null) {
            return rangeList;
        }

        apptsByTime.inOrder((key, appt) -> {
            if (key.date().equals(date)) {
                if (!key.time().isBefore(start)) {
                    if (!key.time().isAfter(end)) {
                        rangeList.add(appt);
                    }
                }
            }
        });

        return rangeList;
    }

    @Override
    public Result<Void> addWalkIn(String patientId) {
        if (patientId == null || patientId.isBlank()) {
            return Result.fail("Patient ID is required.");
        }

        Patient patient = patientsById.get(patientId);

        if (patient == null) {
            return Result.fail("Patient not found.");
        }

        walkIns.enqueue(patient);

        undoStack.push(new Action(ActionType.ADD_WALKIN, patient));

        return Result.ok(null, "Walk-in patient added.");
    }

    @Override
    public List<Patient> viewWalkIns() {
        // Non-destructive view
        return walkIns.toList();
    }

    @Override
    public Result<Void> addUrgent(String patientId, int severity) {
        if (patientId == null || patientId.isBlank()) {
            return Result.fail("Patient ID is required.");
        }

        if (severity <= 0) {
            return Result.fail("Severity must be positive.");
        }

        Patient patient = patientsById.get(patientId);

        if (patient == null) {
            return Result.fail("Patient not found.");
        }

        UrgentPatient urgentPatient = new UrgentPatient(
                patient,
                severity,
                System.currentTimeMillis()
        );

        urgentHeap.push(urgentPatient);

        undoStack.push(new Action(ActionType.ADD_URGENT, urgentPatient));

        return Result.ok(null, "Urgent patient added.");
    }

    @Override
    public Result<UrgentPatient> peekUrgent() {
        UrgentPatient up = urgentHeap.peek();
        if (up == null) return Result.fail("No urgent patients.");
        return Result.ok(up, "Most urgent patient.");
    }

    @Override
    public List<UrgentPatient> viewUrgentsSnapshot() {
        return urgentHeap.toListSnapshot();
    }

    @Override
    public Result<VisitLogEntry> serveNext(String doctor, String note) {
        if (doctor == null || doctor.isBlank()) {
            return Result.fail("Doctor name is required to serve a patient.");
        }

        Patient p = null;
        Object originalObject = null;
        String type = "";

        if (!urgentHeap.isEmpty()) {
            UrgentPatient up = urgentHeap.pop();
            p = up.patient();
            originalObject = up;
            type = "URGENT";
        } else if (!walkIns.isEmpty()) {
            p = walkIns.dequeue();
            originalObject = p;
            type = "WALKIN";
        } else {
            AVLTree.Entry<AppointmentKey, Appointment> entry = apptsByTime.minEntry();
            if (entry != null) {
                Appointment appt = entry.value();
                p = patientsById.get(appt.patientId());
                originalObject = appt;
                type = "APPOINTMENT";
                apptsByTime.remove(entry.key());
                apptsById.remove(appt.appointmentId());
            }
        }

        if (p == null) {
            return Result.fail("No patients waiting in any category.");
        }

        VisitLogEntry logEntry = new VisitLogEntry(
                System.currentTimeMillis(),
                p.id(),
                p.name(),
                type,
                doctor,
                note
        );

        log.addLast(logEntry);
        undoStack.push(new Action(ActionType.SERVE, originalObject));

        return Result.ok(logEntry, "Patient served successfully.");
    }

    @Override
    public List<VisitLogEntry> printLog() {
        return log.toList();
    }

    @Override
    public List<VisitLogEntry> searchLogNaive(String pattern) {
        List<VisitLogEntry> occurances = new ArrayList<>();
        List<VisitLogEntry> entries = log.toList();
        StringMatcher matcher = new NaiveMatcher();
        for (VisitLogEntry entry : entries) {
            if(matcher.contains(entry.notes(), pattern)){
                occurances.add(entry);
            }
        }
        return occurances;

    }

    @Override
    public List<VisitLogEntry> searchLogKmp(String pattern) {
        List<VisitLogEntry> occurances = new ArrayList<>();
        List<VisitLogEntry> entries = log.toList();
        StringMatcher matcher = new KMPMatcher();
        for (VisitLogEntry entry : entries) {
            if(matcher.contains(entry.notes(), pattern)){
                occurances.add(entry);
            }
        }
        return occurances;
    }

    @Override
    public Result<Action> undo() {
        if (undoStack.isEmpty()) {
            return Result.fail("Nothing to undo.");
        }

        Action lastAction = undoStack.pop();
        ActionType type = lastAction.type();
        Object payload = lastAction.payload();

        if (type == ActionType.ADD_APPT) {
            Appointment a = (Appointment) payload;
            AppointmentKey key = new AppointmentKey(a.date(), a.time(), a.appointmentId());
            apptsById.remove(a.appointmentId());
            apptsByTime.remove(key);
        }
        else if (type == ActionType.CANCEL_APPT) {
            Appointment a = (Appointment) payload;
            AppointmentKey key = new AppointmentKey(a.date(), a.time(), a.appointmentId());
            apptsById.put(a.appointmentId(), a);
            apptsByTime.put(key, a);
        }
        else if (type == ActionType.ADD_WALKIN) {
            Patient p = (Patient) payload;
            walkIns.remove(p);
        }
        else if (type == ActionType.ADD_URGENT) {
            UrgentPatient up = (UrgentPatient) payload;
            urgentHeap.remove(up);
        }
        else if (type == ActionType.SERVE) {
            //Remove from end of log because log.addLast was used
            log.removeLast();

            if (payload instanceof UrgentPatient up) {
                urgentHeap.push(up); // Re-heapify
            }
            else if (payload instanceof Patient p) {
                //return it to front of queue to keep FIFO order
                walkIns.pushFront(p);
            }
            else if (payload instanceof Appointment a) {
                AppointmentKey key = new AppointmentKey(a.date(), a.time(), a.appointmentId());
                apptsByTime.put(key, a);
                apptsById.put(a.appointmentId(), a);
            }
        }

        return Result.ok(lastAction, "State restored successfully.");
    }

    private String newAppointmentId() {
        String id = "A" + nextApptId;
        nextApptId = nextApptId + 1;
        return id;
    }
}
