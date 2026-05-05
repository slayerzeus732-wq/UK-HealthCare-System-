package view;

import controller.*;
import model.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MainView extends JFrame {
    private JTabbedPane tabbedPane;

    // Controllers
    private UserController userController;
    private AppointmentController appointmentController;
    private PrescriptionController prescriptionController;
    private ReferralController referralController;

    // Tables
    private JTable patientTable;
    private JTable appointmentTable;
    private JTable prescriptionTable;
    private JTable referralTable;

    // Table Models
    private DefaultTableModel patientTableModel;
    private DefaultTableModel appointmentTableModel;
    private DefaultTableModel prescriptionTableModel;
    private DefaultTableModel referralTableModel;

    public MainView() {
        // Initialize controllers from AppContext
        this.userController = AppContext.userController;
        this.appointmentController = AppContext.appointmentController;
        this.prescriptionController = AppContext.prescriptionController;
        this.referralController = AppContext.referralController;

        initializeUI();
        loadAllData();
    }

    private void initializeUI() {
        setTitle("Healthcare Management System");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        tabbedPane = new JTabbedPane();

        // Create tabs
        tabbedPane.addTab("Patients", createPatientPanel());
        tabbedPane.addTab("Appointments", createAppointmentPanel());
        tabbedPane.addTab("Prescriptions", createPrescriptionPanel());
        tabbedPane.addTab("Referrals", createReferralPanel());

        add(tabbedPane);
    }

    // patient panel
    private JPanel createPatientPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Table - FIXED: Updated column names to match new structure
        String[] columnNames = {"Patient ID", "First Name", "Last Name", "Email", "Phone", "Address", "DOB", "NHS Number"};
        patientTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        patientTable = new JTable(patientTableModel);
        patientTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(patientTable);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("Add Patient");
        JButton editButton = new JButton("Edit Patient");
        JButton deleteButton = new JButton("Delete Patient");
        JButton refreshButton = new JButton("Refresh");

        addButton.addActionListener(e -> showAddPatientDialog());
        editButton.addActionListener(e -> showEditPatientDialog());
        deleteButton.addActionListener(e -> deleteSelectedPatient());
        refreshButton.addActionListener(e -> loadPatients());

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    //  APPOINTMENT PANEL
    private JPanel createAppointmentPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Table
        String[] columnNames = {"Appointment ID", "Patient ID", "Clinician ID", "Facility ID",
                "Date", "Time", "Duration", "Type", "Status", "Reason"};
        appointmentTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        appointmentTable = new JTable(appointmentTableModel);
        appointmentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            JScrollPane scrollPane = new JScrollPane(appointmentTable);
        // Butons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("Add Appointment");
        JButton editButton = new JButton("Edit Status");
        JButton deleteButton = new JButton("Cancel Appointment");
        JButton refreshButton = new JButton("Refresh");

        addButton.addActionListener(e -> showAddAppointmentDialog());
        editButton.addActionListener(e -> showEditAppointmentDialog());
        deleteButton.addActionListener(e -> cancelSelectedAppointment());
        refreshButton.addActionListener(e -> loadAppointments());

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    //  PRESCRIPTION PANEL
    private JPanel createPrescriptionPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Table
        String[] columnNames = {"Prescription ID", "Patient ID", "Clinician ID", "Medication",
                "Dosage", "Frequency", "Duration", "Status", "Issue Date"};
        prescriptionTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        prescriptionTable = new JTable(prescriptionTableModel);
        prescriptionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(prescriptionTable);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("Issue Prescription");
        JButton viewButton = new JButton("View Details");
        JButton refreshButton = new JButton("Refresh");

        addButton.addActionListener(e -> showAddPrescriptionDialog());
        viewButton.addActionListener(e -> showPrescriptionDetails());
        refreshButton.addActionListener(e -> loadPrescriptions());

        buttonPanel.add(addButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(refreshButton);

        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // ==================== REFERRAL PANEL ====================
    private JPanel createReferralPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Table
        String[] columnNames = {"Referral ID", "Patient ID", "Referring Dr", "Referred To",
                "Urgency", "Reason", "Status", "Date"};
        referralTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        referralTable = new JTable(referralTableModel);
        referralTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(referralTable);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("Create Referral");
        JButton updateButton = new JButton("Update Status");
        JButton viewButton = new JButton("View Details");
        JButton refreshButton = new JButton("Refresh");

        addButton.addActionListener(e -> showAddReferralDialog());
        updateButton.addActionListener(e -> showUpdateReferralDialog());
        viewButton.addActionListener(e -> showReferralDetails());
        refreshButton.addActionListener(e -> loadReferrals());

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(refreshButton);

        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // DATA LOADING METHODS
    private void loadAllData() {
        loadPatients();
        loadAppointments();
        loadPrescriptions();
        loadReferrals();
    }


    private void loadPatients() {
        patientTableModel.setRowCount(0);
        List<Patient> patients = userController.getAllPatients();
        for (Patient patient : patients) {
            Object[] row = {
                    patient.getUserId(),
                    patient.getFirstName(),
                    patient.getLastName(),
                    patient.getEmail(),
                    patient.getPhone(),
                    patient.getFullAddress(),  // Combined address + postcode
                    patient.getDob(),
                    patient.getNhsNumber()
            };
            patientTableModel.addRow(row);
        }
    }

    private void loadAppointments() {
        appointmentTableModel.setRowCount(0);
        List<Appointment> appointments = appointmentController.getAllAppointments();
        for (Appointment apt : appointments) {
            Object[] row = {
                    apt.getAppointmentId(),
                    apt.getPatientId(),
                    apt.getClinicianId(),
                    apt.getFacilityId(),
                    apt.getAppointmentDate(),
                    apt.getAppointmentTime(),
                    apt.getDurationMinutes(),
                    apt.getAppointmentType(),
                    apt.getStatus(),
                    apt.getReasonForVisit()
            };
            appointmentTableModel.addRow(row);
        }
    }

    private void loadPrescriptions() {
        prescriptionTableModel.setRowCount(0);
        List<Prescription> prescriptions = AppContext.prescriptionRepository.getAllPrescriptions();
        for (Prescription presc : prescriptions) {
            Object[] row = {
                    presc.getPrescriptionId(),
                    presc.getPatientId(),
                    presc.getClinicianId(),
                    presc.getMedicationName(),
                    presc.getDosage(),
                    presc.getFrequency(),
                    presc.getDurationDays(),
                    presc.getStatus(),
                    presc.getIssueDate()
            };
            prescriptionTableModel.addRow(row);
        }
    }

    private void loadReferrals() {
        referralTableModel.setRowCount(0);
        List<Referral> referrals = AppContext.referralRepository.loadRefferals();
        for (Referral ref : referrals) {
            Object[] row = {
                    ref.getReferralId(),
                    ref.getPatientId(),
                    ref.getReferringClinicianId(),
                    ref.getReferredToClinicianId(),
                    ref.getUrgencyLevel(),
                    ref.getReferralReason(),
                    ref.getStatus(),
                    ref.getReferralDate()
            };
            referralTableModel.addRow(row);
        }
    }

    // ==================== PATIENT DIALOGS ====================

    private void showAddPatientDialog() {
        JDialog dialog = new JDialog(this, "Add New Patient", true);
        dialog.setSize(450, 650);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(14, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField idField = new JTextField();
        JTextField firstNameField = new JTextField();
        JTextField lastNameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField addressField = new JTextField();
        JTextField postcodeField = new JTextField();
        JTextField passwordField = new JTextField();
        JTextField dobField = new JTextField();
        JTextField nhsField = new JTextField();
        JTextField genderField = new JTextField();
        JTextField emergencyNameField = new JTextField();
        JTextField emergencyPhoneField = new JTextField();
        JTextField gpSurgeryField = new JTextField();

        panel.add(new JLabel("Patient ID:"));
        panel.add(idField);
        panel.add(new JLabel("First Name:"));
        panel.add(firstNameField);
        panel.add(new JLabel("Last Name:"));
        panel.add(lastNameField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Phone:"));
        panel.add(phoneField);
        panel.add(new JLabel("Address:"));
        panel.add(addressField);
        panel.add(new JLabel("Postcode:"));
        panel.add(postcodeField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(new JLabel("DOB (YYYY-MM-DD):"));
        panel.add(dobField);
        panel.add(new JLabel("NHS Number:"));
        panel.add(nhsField);
        panel.add(new JLabel("Gender (M/F):"));
        panel.add(genderField);
        panel.add(new JLabel("Emergency Contact Name:"));
        panel.add(emergencyNameField);
        panel.add(new JLabel("Emergency Contact Phone:"));
        panel.add(emergencyPhoneField);
        panel.add(new JLabel("GP Surgery ID:"));
        panel.add(gpSurgeryField);

        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        saveButton.addActionListener(e -> {
            try {
                Patient patient = new Patient(
                        idField.getText(),
                        firstNameField.getText(),
                        lastNameField.getText(),
                        emailField.getText(),
                        phoneField.getText(),
                        addressField.getText(),
                        postcodeField.getText(),
                        passwordField.getText(),
                        LocalDate.parse(dobField.getText()),
                        nhsField.getText(),
                        genderField.getText(),
                        emergencyNameField.getText(),
                        emergencyPhoneField.getText(),
                        LocalDate.now(),  // registration date
                        gpSurgeryField.getText(),
                        "",  // medical history (empty)
                        nhsField.getText()  // insurance number (same as NHS)
                );

                AppContext.patientRepository.addPatient(patient);
                loadPatients();
                dialog.dispose();
                JOptionPane.showMessageDialog(this, "Patient added successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }


    private void showEditPatientDialog() {
        int selectedRow = patientTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a patient to edit.");
            return;
        }

        String patientId = (String) patientTableModel.getValueAt(selectedRow, 0);
        Patient patient = AppContext.patientRepository.findPatientById(patientId);

        if (patient == null) {
            JOptionPane.showMessageDialog(this, "Patient not found.");
            return;
        }

        JDialog dialog = new JDialog(this, "Edit Patient", true);
        dialog.setSize(400, 500);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(8, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField firstNameField = new JTextField(patient.getFirstName());
        JTextField lastNameField = new JTextField(patient.getLastName());
        JTextField emailField = new JTextField(patient.getEmail());
        JTextField phoneField = new JTextField(patient.getPhone());
        JTextField addressField = new JTextField(patient.getAddress());
        JTextField postcodeField = new JTextField(patient.getPostcode());
        JTextField emergencyNameField = new JTextField(patient.getEmergencyContactName());
        JTextField emergencyPhoneField = new JTextField(patient.getEmergencyContactPhone());

        panel.add(new JLabel("First Name:"));
        panel.add(firstNameField);
        panel.add(new JLabel("Last Name:"));
        panel.add(lastNameField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Phone:"));
        panel.add(phoneField);
        panel.add(new JLabel("Address:"));
        panel.add(addressField);
        panel.add(new JLabel("Postcode:"));
        panel.add(postcodeField);
        panel.add(new JLabel("Emergency Contact Name:")); //later didnt add them add in the report
        panel.add(emergencyNameField);
        panel.add(new JLabel("Emergency Contact Phone:"));
        panel.add(emergencyPhoneField);

        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        saveButton.addActionListener(e -> {
            try {
                // Create updated patient with new values
                Patient updatedPatient = new Patient(
                        patient.getUserId(),
                        firstNameField.getText(),
                        lastNameField.getText(),
                        emailField.getText(),
                        phoneField.getText(),
                        addressField.getText(),
                        postcodeField.getText(),
                        patient.getPassword(),  // Keep existing password
                        patient.getDob(),
                        patient.getNhsNumber(),
                        patient.getGender(),
                        emergencyNameField.getText(),
                        emergencyPhoneField.getText(),
                        patient.getRegistrationDate(),
                        patient.getGpSurgeryId(),
                        patient.getMedicalHistory(),
                        patient.getInsuranceNumber()
                );

                AppContext.patientRepository.updatePatient(updatedPatient);
                loadPatients();
                dialog.dispose();
                JOptionPane.showMessageDialog(this, "Patient updated successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void deleteSelectedPatient() {
        int selectedRow = patientTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a patient to delete.");
            return;
        }

        String patientId = (String) patientTableModel.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this patient?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            userController.deletePatient(patientId);
            loadPatients();
            JOptionPane.showMessageDialog(this, "Patient deleted successfuly");
        }
    }

    //appointment dialog
    private void showAddAppointmentDialog() {
        JDialog dialog = new JDialog(this, "Add New Appointment", true);
        dialog.setSize(400, 500);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(11, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField idField = new JTextField();
        JTextField patientIdField = new JTextField();
        JTextField clinicianIdField = new JTextField();
        JTextField facilityIdField = new JTextField();
        JTextField dateField = new JTextField();
        JTextField timeField = new JTextField();
        JTextField durationField = new JTextField();
        JTextField typeField = new JTextField();
        JTextField statusField = new JTextField("Scheduled");
        JTextField reasonField = new JTextField();
        JTextField notesField = new JTextField();

        panel.add(new JLabel("Appointment ID:"));
        panel.add(idField);
        panel.add(new JLabel("Patient ID:"));
        panel.add(patientIdField);
        panel.add(new JLabel("Clinician ID:"));
        panel.add(clinicianIdField);
        panel.add(new JLabel("Facility ID:"));
        panel.add(facilityIdField);
        panel.add(new JLabel("Date (YYYY-MM-DD):"));
        panel.add(dateField);
        panel.add(new JLabel("Time (HH:MM):"));
        panel.add(timeField);
        panel.add(new JLabel("Duration (minutes):"));
        panel.add(durationField);
        panel.add(new JLabel("Type:"));
        panel.add(typeField);
        panel.add(new JLabel("Status:"));
        panel.add(statusField);
        panel.add(new JLabel("Reason:"));
        panel.add(reasonField);
        panel.add(new JLabel("Notes:"));
        panel.add(notesField);

        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        saveButton.addActionListener(e -> {
            try {
                Appointment appointment = new Appointment(
                        idField.getText(),
                        patientIdField.getText(),
                        clinicianIdField.getText(),
                        facilityIdField.getText(),
                        LocalDate.parse(dateField.getText()),
                        LocalTime.parse(timeField.getText()),
                        Integer.parseInt(durationField.getText()),
                        typeField.getText(),
                        statusField.getText(),
                        reasonField.getText(),
                        notesField.getText(),
                        LocalDate.now(),

                        LocalDate.now()

                );

                appointmentController.addAppointment(appointment);
                loadAppointments();

                dialog.dispose();
                JOptionPane.showMessageDialog(this, "Appointment added successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showEditAppointmentDialog() {
        int selectedRow = appointmentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an appointment to edit.");
            return;
        }

        String appointmentId = (String) appointmentTableModel.getValueAt(selectedRow, 0);

        String[] statuses = {"Scheduled", "Completed", "Cancelled", "No-Show"};
        String newStatus = (String) JOptionPane.showInputDialog(
                this,
                "Select new status:",
                "Update Appointment Status",
                JOptionPane.QUESTION_MESSAGE,
                null,
                statuses,
                statuses[0]
        );

        if (newStatus != null) {
            AppContext.appointmentRepository.updateAppointmentStatus(appointmentId, newStatus);
            loadAppointments();
            JOptionPane.showMessageDialog(this, "Appointment status updated!");
        }
    }

    private void cancelSelectedAppointment() {
        int selectedRow = appointmentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an appointment to cancel.");
            return;
        }

        String appointmentId = (String) appointmentTableModel.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to cancel this appointment?",
                "Confirm Cancel", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            appointmentController.cancelAppointment(appointmentId);
            loadAppointments();
            JOptionPane.showMessageDialog(this, "Appointment cancelled!");
        }
    }

    // ==================== PRESCRIPTION DIALOGS ====================
    private void showAddPrescriptionDialog() {
        JDialog dialog = new JDialog(this, "Issue New Prescription", true);
        dialog.setSize(400, 600);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(13, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField idField = new JTextField();
        JTextField patientIdField = new JTextField();
        JTextField clinicianIdField = new JTextField();
        JTextField appointmentIdField = new JTextField();
        JTextField medicationField = new JTextField();
        JTextField dosageField = new JTextField();
        JTextField frequencyField = new JTextField();
        JTextField durationField = new JTextField();
        JTextField quantityField = new JTextField();
        JTextField instructionsField = new JTextField();
        JTextField pharmacyField = new JTextField();
        JTextField statusField = new JTextField("Active");

        panel.add(new JLabel("Prescription ID:"));
        panel.add(idField);
        panel.add(new JLabel("Patient ID:"));
        panel.add(patientIdField);
        panel.add(new JLabel("Clinician ID:"));
        panel.add(clinicianIdField);
        panel.add(new JLabel("Appointment ID:"));
        panel.add(appointmentIdField);
        panel.add(new JLabel("Medication:"));
        panel.add(medicationField);
        panel.add(new JLabel("Dosage:"));
        panel.add(dosageField);
        panel.add(new JLabel("Frequency:"));
        panel.add(frequencyField);
        panel.add(new JLabel("Duration (days):"));
        panel.add(durationField);
        panel.add(new JLabel("Quantity:"));
        panel.add(quantityField);
        panel.add(new JLabel("Instructions:"));
        panel.add(instructionsField);
        panel.add(new JLabel("Pharmacy:"));
        panel.add(pharmacyField);
        panel.add(new JLabel("Status:"));
        panel.add(statusField);

        JButton saveButton = new JButton("Issue");
        JButton cancelButton = new JButton("Cancel");

        saveButton.addActionListener(e -> {
            try {
                Prescription prescription = new Prescription(
                        idField.getText(),
                        patientIdField.getText(),
                        clinicianIdField.getText(),
                        appointmentIdField.getText(),
                        LocalDate.now(),
                        medicationField.getText(),
                        dosageField.getText(),
                        frequencyField.getText(),
                        Integer.parseInt(durationField.getText()),
                        quantityField.getText(),
                        instructionsField.getText(),
                        pharmacyField.getText(),
                        statusField.getText(),
                        LocalDate.now(),
                        null
                );

                prescriptionController.issuePrescription(prescription);
                loadPrescriptions();
                dialog.dispose();
                JOptionPane.showMessageDialog(this, "Prescription issued successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showPrescriptionDetails() {
        int selectedRow = prescriptionTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a prescription to view.");
            return;
        }

        StringBuilder details = new StringBuilder();
        for (int i = 0; i < prescriptionTableModel.getColumnCount(); i++) {
            details.append(prescriptionTableModel.getColumnName(i))
                    .append(": ")
                    .append(prescriptionTableModel.getValueAt(selectedRow, i))
                    .append("\n");
        }

        JOptionPane.showMessageDialog(this, details.toString(),
                "Prescription Details", JOptionPane.INFORMATION_MESSAGE);
    }

    // ==================== REFERRAL DIALOGS ====================
    private void showAddReferralDialog() {
        JDialog dialog = new JDialog(this, "Create New Referral", true);
        dialog.setSize(450, 600);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(12, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField idField = new JTextField();
        JTextField patientIdField = new JTextField();
        JTextField referringClinicianField = new JTextField();
        JTextField referredToClinicianField = new JTextField();
        JTextField referringFacilityField = new JTextField();
        JTextField referredToFacilityField = new JTextField();
        JTextField urgencyField = new JTextField();
        JTextField reasonField = new JTextField();
        JTextField summaryField = new JTextField();


        JTextField investigationsField = new JTextField();
        JTextField statusField = new JTextField("Pending");
        JTextField notesField = new JTextField();

        panel.add(new JLabel("Referral ID:"));
        panel.add(idField);
        panel.add(new JLabel("Patient ID:"));
        panel.add(patientIdField);
        panel.add(new JLabel("Referring Clinician ID:"));
        panel.add(referringClinicianField);
        panel.add(new JLabel("Referred To Clinician ID:"));
        panel.add(referredToClinicianField);
        panel.add(new JLabel("Referring Facility ID:"));
        panel.add(referringFacilityField);
        panel.add(new JLabel("Referred To Facility ID:"));
        panel.add(referredToFacilityField);
        panel.add(new JLabel("Urgency Level:"));
        panel.add(urgencyField);
        panel.add(new JLabel("Reason:"));
        panel.add(reasonField);
        panel.add(new JLabel("Clinical Summary:"));
        panel.add(summaryField);


        panel.add(new JLabel("Investigations:"));
        panel.add(investigationsField);
        panel.add(new JLabel("Status:"));
        panel.add(statusField);
        panel.add(new JLabel("Notes:"));
        panel.add(notesField);

        JButton saveButton = new JButton("Create");
        JButton cancelButton = new JButton("Cancel");

        saveButton.addActionListener(e -> {
            try {
                Referral referral = new Referral(
                        idField.getText(),
                        patientIdField.getText(),
                        referringClinicianField.getText(),
                        referredToClinicianField.getText(),
                        referringFacilityField.getText(),
                        referredToFacilityField.getText(),
                        LocalDate.now(),
                        urgencyField.getText(),
                        reasonField.getText(),
                        summaryField.getText(),
                        investigationsField.getText(),
                        statusField.getText(),
                        "",
                        notesField.getText(),
                        LocalDate.now(),
                        LocalDate.now()
                );

                referralController.createReferral(referral);
                loadReferrals();
                dialog.dispose();
                JOptionPane.showMessageDialog(this, "Referral created successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showUpdateReferralDialog() {
        int selectedRow = referralTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a referral to update.");
            return;
             }

        String referralId = (String) referralTableModel.getValueAt(selectedRow, 0);

         String[] statuses = {"Pending", "Accepted", "In Progress", "Completed", "Rejected"};
         String newStatus = (String) JOptionPane.showInputDialog(
                this,
                 "Select new status:",
                "Update Referral Status",
                JOptionPane.QUESTION_MESSAGE,
                null,
                statuses,
                statuses[0]
        );

        if (newStatus != null) {
            referralController.updateReferral(referralId, newStatus);
            loadReferrals();
            JOptionPane.showMessageDialog(this, "Referral status updated!");
        }
    }

    private void showReferralDetails() {
        int selectedRow = referralTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a referral to view.");
            return;
        }

        String referralId = (String) referralTableModel.getValueAt(selectedRow, 0);
        Referral referral = AppContext.referralRepository.getReferralById(referralId);

        if (referral == null) {
            JOptionPane.showMessageDialog(this, "Referral not found.");
            return;
        }

        String details = String.format(
                "Referral ID: %s\n" +
                        "Patient ID: %s\n" +
                        "Urgency: %s\n" +
                        "Reason: %s\n" +
                        "Clinical Summary: %s\n" +
                        "Status: %s\n" +
                        "Referred To: %s",
                        referral.getReferralId(),
                referral.getPatientId(),
                referral.getUrgencyLevel(),
                referral.getReferralReason(),
                referral.getClinicalSummary(),
                referral.getStatus(),
                referral.getReferredToClinicianId()
        );

        JOptionPane.showMessageDialog(this, details,
                "Referral Details", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        // Initialize the application context
        AppContext.init();

        // Start the GUI on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            new MainView().setVisible(true);
        });
    }
}
