package kfupm.clinic.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

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
        undoStack.push(new Action(ActionType.ADD_PATIENT, p));
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
        undoStack.push(new Action(ActionType.DELETE_PATIENT, p));
        return Result.ok(null, "Patient deleted");
    }

    @Override
    public Result<String> addAppointment(String patientId, LocalDate date, LocalTime time, String doctor) {
        // TODO: ensure patient exists; create appointmentId; insert into AVL + hash; record undo
        throw new UnsupportedOperationException("TODO: ClinicServiceImpl.addAppointment");
    }

    @Override
    public Result<Void> cancelAppointment(String appointmentId) {
        // TODO: use hash to find appt; remove from AVL + hash; record undo
        throw new UnsupportedOperationException("TODO: ClinicServiceImpl.cancelAppointment");
    }

    @Override
    public Result<Appointment> findAppointment(String appointmentId) {
        // TODO: use hash table
        throw new UnsupportedOperationException("TODO: ClinicServiceImpl.findAppointment");
    }

    @Override
    public List<Appointment> viewDay(LocalDate date) {
        // TODO: in-order traverse AVL and filter by date, OR implement date range traversal
        return new ArrayList<>();
    }

    @Override
    public List<Appointment> viewRange(LocalDate date, LocalTime start, LocalTime end) {
        // TODO: range query traversal on AVL for (date,start) .. (date,end)
        return new ArrayList<>();
    }

    @Override
    public Result<Void> addWalkIn(String patientId) {
        // TODO: ensure patient exists; enqueue; record undo
        throw new UnsupportedOperationException("TODO: ClinicServiceImpl.addWalkIn");
    }

    @Override
    public List<Patient> viewWalkIns() {
        // Non-destructive view
        return walkIns.toList();
    }

    @Override
    public Result<Void> addUrgent(String patientId, int severity) {
        // TODO: validate severity; ensure patient exists; heap push; record undo
        throw new UnsupportedOperationException("TODO: ClinicServiceImpl.addUrgent");
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
        // TODO: serving policy: urgent > walk-in > earliest appointment
        //  append log entry ----> DONE       TODO: record undo
        VisitLogEntry entry = new VisitLogEntry(
                System.currentTimeMillis(),
                patient.id(),
                patient.name(),
                type,
                doctor,
                note
        );

        log.addLast(entry);

        throw new UnsupportedOperationException("TODO: ClinicServiceImpl.serveNext");
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
        // TODO: pop undo stack and reverse last action
        throw new UnsupportedOperationException("TODO: ClinicServiceImpl.undo");
    }

    // Helpers you may want
    private String newAppointmentId() {
        String id = "A" + nextApptId;
        nextApptId = nextApptId + 1;
        return id;
    }
}
