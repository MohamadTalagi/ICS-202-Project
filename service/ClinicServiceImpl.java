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

    private final HashTable<String, Patient> patientsById = new HashTable<>();
    private final HashTable<String, Appointment> apptsById = new HashTable<>();
    private final AVLTree<AppointmentKey, Appointment> apptsByTime = new AVLTree<>();

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
        throw new UnsupportedOperationException("TODO: ClinicServiceImpl.addAppointment");
    }

    @Override
    public Result<Void> cancelAppointment(String appointmentId) {
        throw new UnsupportedOperationException("TODO: ClinicServiceImpl.cancelAppointment");
    }

    @Override
    public Result<Appointment> findAppointment(String appointmentId) {
        throw new UnsupportedOperationException("TODO: ClinicServiceImpl.findAppointment");
    }

    @Override
    public List<Appointment> viewDay(LocalDate date) {
        throw new UnsupportedOperationException("TODO: ClinicServiceImpl.viewDay");
    }

    @Override
    public List<Appointment> viewRange(LocalDate date, LocalTime start, LocalTime end) {
        throw new UnsupportedOperationException("TODO: ClinicServiceImpl.viewRange");
    }

    @Override
    public Result<Void> addWalkIn(String patientId) {
        throw new UnsupportedOperationException("TODO: ClinicServiceImpl.addWalkIn");
    }

    @Override
    public List<Patient> viewWalkIns() {
        return walkIns.toList();
    }

    @Override
    public Result<Void> addUrgent(String patientId, int severity) {
        throw new UnsupportedOperationException("TODO: ClinicServiceImpl.addUrgent");
    }

    @Override
    public Result<UrgentPatient> peekUrgent() {
        throw new UnsupportedOperationException("TODO: ClinicServiceImpl.peekUrgent");
    }

    @Override
    public List<UrgentPatient> viewUrgentsSnapshot() {
        return urgentHeap.toListSnapshot();
    }

    @Override
    public Result<VisitLogEntry> serveNext(String doctor, String note) {
        throw new UnsupportedOperationException("TODO: ClinicServiceImpl.serveNext");
    }

    @Override
    public List<VisitLogEntry> printLog() {
        return log.toList();
    }

    @Override
    public List<VisitLogEntry> searchLogNaive(String pattern) {
        throw new UnsupportedOperationException("TODO: ClinicServiceImpl.searchLogNaive");
    }

    @Override
    public List<VisitLogEntry> searchLogKmp(String pattern) {
        throw new UnsupportedOperationException("TODO: ClinicServiceImpl.searchLogKmp");
    }

    @Override
    public Result<Action> undo() {
        throw new UnsupportedOperationException("TODO: ClinicServiceImpl.undo");
    }

    private String newAppointmentId() {
        String id = "A" + nextApptId;
        nextApptId = nextApptId + 1;
        return id;
    }
}