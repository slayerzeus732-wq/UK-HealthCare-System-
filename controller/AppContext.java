package controller;

import data.*;
import model.*;

public class AppContext {
    // Repositories
    public static PatientRepository patientRepository;
    public static AppointmentRepository appointmentRepository;
    public static ClinicianRepository clinicianRepository;
    public static FacilityRepository facilityRepository;
    public static PrescriptionRepository prescriptionRepository;
    public static ReferralRepository referralRepository;

    // Controllers
    public static UserController userController;
    public static AppointmentController appointmentController;
    public static PrescriptionController prescriptionController;
    public static ReferralController referralController;

    public static void init() {
        // Initialize repositories
        patientRepository = new PatientRepository("csv/patients.csv");
        appointmentRepository = new AppointmentRepository("csv/appointments.csv");
        clinicianRepository = new ClinicianRepository("csv/clinicians.csv");
        facilityRepository = new FacilityRepository("csv/facilities.csv");
        prescriptionRepository = new PrescriptionRepository("csv/prescriptions.csv");
        referralRepository = new ReferralRepository("csv/referrals.csv");

        // Initialize controllers
        userController = new UserController();
        appointmentController = new AppointmentController(appointmentRepository);
        prescriptionController = new PrescriptionController(prescriptionRepository);
        referralController = new ReferralController(referralRepository);
    }
}