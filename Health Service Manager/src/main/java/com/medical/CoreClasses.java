/**
 * @author : Yuuji
 * CoreClasses.java
 * Latest Update: 12-19-2024
 */

package com.medical;

// Importing necessary utilities for serialization, formatting, and data structures
import java.io.Serial;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * The MedicalFacility class is an abstract representation of a
 * medical facility.
 * It provides basic attributes and methods for facilities such as
 * hospitals and clinics.
 * This class implements Serializable for object serialization,
 * allowing instance to be saved and restored.
 */
abstract class MedicalFacility implements Serializable {
    @Serial
    // Unique identifier for serialization
    private static final long serialVersionUID = 1L; 
    // Unique identifier for the facility
    private int id; 
    // Name of the facility
    private String name; 

    /**
     * Constructs a MedicalFacility with the specified name.
     * 
     * @param name the name of the medical facility
     */
    public MedicalFacility(String name) {
        // Initialize the facility name
        this.name = name; 
    }

    /**
     * Returns the unique identifier of the facility.
     * 
     * @return the facility ID
     */
    public int getId() {
        // Return the facility ID
        return id; 
    }

    /**
     * Sets the unique identifier of the facility.
     * 
     * @param id the facility ID to set
     */
    public void setId(int id) {
        // Set the facility ID
        this.id = id; 
    }

    /**
     * Returns the name of the facility.
     * 
     * @return the facility name
     */
    public String getName() {
        // Return the facility name
        return name; 
    }

    /**
     * Sets the name of the facility.
     * 
     * @param name the facility name to set
     */
    public void setName(String name) {
        // Set the facility name
        this.name = name; 
    }

    /**
     * Abstract method to handle patient visits to the facility.
     * 
     * @param patient the patient visiting the facility
     * @return true if the visit is successful, false otherwise
     */
    // Abstract method to be implemented by subclasses
    public abstract boolean visit(Patient patient); 

    @Override
    public String toString() {
        // String representation of the facility
        return "ID: " + id + ", Name: " + name + ", Type: " + 
               getClass().getSimpleName(); 
    }
}

/**
 * The Hospital class represents a hospital facility, extending
 * MedicalFacility. It manages procedures and patient admissions
 * based on a probability of admission.
 */
class Hospital extends MedicalFacility {
    @Serial
    // Unique identifier for serialization
    private static final long serialVersionUID = 1L; 
    // Probability of admitting a patient
    private double probAdmit; 
    // List of procedures available at the hospital
    private final List<Procedure> procedures = new ArrayList<>();

    /**
     * Constructs a Hospital with the specified name and
     * admission probability.
     * 
     * @param name the name of the hospital
     * @param probAdmit the probability of admitting a patient
     */
    public Hospital(String name, double probAdmit) {
        // Call the superclass constructor to set the name
        super(name); 
        // Initialize the admission probability
        this.probAdmit = probAdmit; 
    }

    /**
     * Adds a procedure to the hospital's list of procedures.
     * 
     * @param procedure the procedure to add
     */
    public void addProcedure(Procedure procedure) {
        // Add the procedure to the list
        procedures.add(procedure); 
        // Reassign IDs after adding
        reassignProcedureIds(); 
    }

    /**
     * Removes a procedure from the hospital's list by its ID.
     * 
     * @param id the ID of the procedure to remove
     */
    public void removeProcedure(int id) {
        // Remove the procedure if the ID matches
        procedures.removeIf(p -> p.getId() == id); 
        // Reassign IDs after removal
        reassignProcedureIds(); 
    }

    /**
     * Reassigns IDs to the procedures in the hospital.
     * This ensures that procedure IDs are sequential 
     * after additions or removals.
     */
    public void reassignProcedureIds() {
        for (int i = 0; i < procedures.size(); i++) {
            // Set new IDs starting from 1
            procedures.get(i).setId(i + 1); 
        }
    }

    /**
     * Returns the list of procedures available at the hospital.
     * 
     * @return the list of procedures
     */
    public List<Procedure> getProcedures() {
        // Return the list of procedures
        return procedures; 
    }

    /**
     * Returns the probability of admitting a patient.
     * 
     * @return the admission probability
     */
    public double getProbAdmit() {
        // Return the admission probability
        return probAdmit; 
    }

    /**
     * Sets the probability of admitting a patient.
     * 
     * @param probAdmit the admission probability to set
     */
    public void setProbAdmit(double probAdmit) {
        // Set the admission probability
        this.probAdmit = probAdmit; 
    }

    @Override
    public boolean visit(Patient patient) {
        // Create a random number generator
        Random random = new Random(); 
        // Determine if the patient is admitted based on probability
        boolean admitted = random.nextDouble() <= probAdmit; 

        if (admitted) {
            // Set the current facility for the patient
            patient.setCurrentFacility(this); 
            // Return true if admitted
            return true; 
        }

        // Return false if not admitted
        return false; 
    }

    /**
     * Performs a procedure on a patient, adjusting their balance based 
     * on procedure type and patient status.
     * 
     * @param patient the patient undergoing the procedure
     * @param procedure the procedure to perform
     * @return true if the procedure is performed, false otherwise
     */
    public boolean performProcedure(Patient patient, Procedure procedure) {
        if (patient.getCurrentFacility() != this) {
            // Return false if the patient is not at this hospital
            return false; 
        }
        double procedureCost; // Variable to hold the cost of the procedure
        if (patient.isPrivate()) {
            // Updated costs for private patients
            procedureCost = procedure.isElective() ? 4000 : 2500; 
        } else {
            // Cost for public patients
            procedureCost = procedure.isElective() ? 
                            procedure.getCost() : 0; 
        }
        // Adjust the patient's balance
        patient.addBalance(procedureCost); 
        // Return true indicating the procedure was performed
        return true; 
    }

}

/**
 * The Clinic class represents a clinic facility, extending
 * MedicalFacility. It manages patient visits and charges
 * based on consultation fees and gap percentages.
 */
class Clinic extends MedicalFacility {
    // Fee for a consultation at the clinic
    private double consultationFee; 
    // Additional percentage charged for private patients
    private double gapPercent; 
    // Decimal format for displaying fees
    private static final DecimalFormat df = new DecimalFormat("0.0"); 

    /**
     * Constructs a Clinic with the specified name, consultation fee,
     * and gap percentage.
     * 
     * @param name the name of the clinic
     * @param consultationFee the fee for a consultation
     * @param gapPercent the additional percentage charged
     *        for private patients
     */
    public Clinic(String name, double consultationFee, double gapPercent) {
        // Call the superclass constructor to set the name
        super(name); 
        // Initialize the consultation fee
        this.consultationFee = consultationFee; 
        // Initialize the gap percentage
        this.gapPercent = gapPercent; 
    }

    @Override
    public boolean visit(Patient patient) {
        if (!patient.isRegisteredAt(this)) {
            // Register the patient at the clinic
            patient.registerAt(this); 
            // Set the current facility for the patient
            patient.setCurrentFacility(this); 
            // Return true for first-time visit
            return true; 
        } else {
            // Base charge for consultation
            double charge = consultationFee;
            if (patient.isPrivate()) {
                // Add gap percentage for private patients
                charge += consultationFee * (gapPercent / 100); 
            }
            // Adjust the patient's balance
            patient.addBalance(charge); 
            // Return false for subsequent visits
            return false; 
        }
    }

    /**
     * Returns the consultation fee, formatted to one decimal place.
     * 
     * @return the consultation fee
     */
    public double getConsultationFee() {
        // Return formatted consultation fee
        return Double.parseDouble(df.format(consultationFee)); 
    }

    /**
     * Sets the consultation fee for the clinic.
     * 
     * @param consultationFee the consultation fee to set
     */
    public void setConsultationFee(double consultationFee) {
        // Set the consultation fee
        this.consultationFee = consultationFee; 
    }

    /**
     * Returns the gap percentage charged for private patients.
     * 
     * @return the gap percentage
     */
    public double getGapPercent() {
        // Return the gap percentage
        return gapPercent; 
    }

    /**
     * Sets the gap percentage charged for private patients.
     * 
     * @param gapPercent the gap percentage to set
     */
    public void setGapPercent(double gapPercent) {
        // Set the gap percentage
        this.gapPercent = gapPercent; 
    }
}

/**
 * The Patient class represents a patient in the health service system.
 * It manages patient details, balance, and registered facilities.
 */
class Patient implements Serializable {
    @Serial
    // Unique identifier for serialization
    private static final long serialVersionUID = 1L; 
    // Unique identifier for the patient
    private int id; 
    // Name of the patient
    private String name; 
    // Indicates if the patient is private
    private final boolean isPrivate;
    // Current balance of the patient
    private double balance = 0.0; 
    // Current facility the patient is associated with
    private MedicalFacility currentFacility; 
    // Facilities the patient is registered at
    private final Set<MedicalFacility> registeredFacilities;

    /**
     * Constructs a Patient with the specified name and privacy status.
     * 
     * @param name the name of the patient
     * @param isPrivate the privacy status of the patient
     */
    public Patient(String name, boolean isPrivate) {
        // Initialize the patient's name
        this.name = name; 
        // Initialize the privacy status
        this.isPrivate = isPrivate; 
        // Initialize the set of registered facilities
        this.registeredFacilities = new HashSet<>(); 
    }

    /**
     * Returns the unique identifier of the patient.
     * 
     * @return the patient ID
     */
    public int getId() {
        // Return the patient ID
        return id; 
    }

    /**
     * Sets the unique identifier of the patient.
     * 
     * @param id the patient ID to set
     */
    public void setId(int id) {
        // Set the patient ID
        this.id = id; 
    }

    /**
     * Returns the name of the patient.
     * 
     * @return the patient name
     */
    public String getName() {
        // Return the patient's name
        return name; 
    }

    /**
     * Sets the name of the patient.
     * 
     * @param name the patient name to set
     */
    public void setName(String name) {
        // Set the patient's name
        this.name = name; 
    }

    /**
     * Returns the privacy status of the patient.
     * 
     * @return true if the patient is private, false otherwise
     */
    public boolean isPrivate() {
        // Return the privacy status
        return isPrivate; 
    }

    /**
     * Returns the current balance of the patient.
     * 
     * @return the patient balance
     */
    public double getBalance() {
        // Return the patient's balance
        return balance; 
    }

    /**
     * Adds an amount to the patient's balance.
     * 
     * @param amount the amount to add
     */
    public void addBalance(double amount) {
        // Increase the patient's balance
        this.balance += amount; 
    }

    /**
     * Registers the patient at a specified medical facility.
     * 
     * @param facility the facility to register at
     */
    public void registerAt(MedicalFacility facility) {
        // Add the facility to the registered set
        registeredFacilities.add(facility); 
    }

    /**
     * Checks if the patient is registered at a specified medical facility.
     * 
     * @param facility the facility to check
     * @return true if registered, false otherwise
     */
    public boolean isRegisteredAt(MedicalFacility facility) {
        // Check if the facility is in the registered set
        return registeredFacilities.contains(facility); 
    }

    /**
     * Returns the current facility the patient is associated with.
     * 
     * @return the current facility
     */
    public MedicalFacility getCurrentFacility() {
        // Return the current facility
        return currentFacility; 
    }

    /**
     * Sets the current facility the patient is associated with.
     * 
     * @param currentFacility the current facility to set
     */
    public void setCurrentFacility(MedicalFacility currentFacility) {
        // Set the current facility
        this.currentFacility = currentFacility; 
    }

    @Override
    public String toString() {
        // String representation of the patient
        return "ID: " + id + ", Name: " + name + ", Private: "
                + isPrivate + ", Balance: " + balance;
    }

}

/**
 * The Procedure class represents a medical procedure.
 * It manages procedure details such as name, description,
 * elective status, and cost.
 */
class Procedure implements Serializable {
    @Serial
    // Unique identifier for serialization
    private static final long serialVersionUID = 1L; 
    // Unique identifier for the procedure
    private int id; 
    // Name of the procedure
    private String name; 
    // Description of the procedure
    private String description; 
    // Indicates if the procedure is elective
    private boolean isElective; 
    // Cost of the procedure
    private double cost; 

    /**
     * Constructs a Procedure with the specified details.
     * 
     * @param name the name of the procedure
     * @param description the description of the procedure
     * @param isElective the elective status of the procedure
     * @param cost the cost of the procedure
     */
    public Procedure
    (String name, String description, boolean isElective, double cost) {
        // Initialize the procedure name
        this.name = name; 
        // Initialize the procedure description
        this.description = description; 
        // Initialize the elective status
        this.isElective = isElective; 
        // Initialize the procedure cost
        this.cost = cost; 
    }

    /**
     * Returns the unique identifier of the procedure.
     * 
     * @return the procedure ID
     */
    public int getId() {
        // Return the procedure ID
        return id; 
    }

    /**
     * Sets the unique identifier of the procedure.
     * 
     * @param id the procedure ID to set
     */
    public void setId(int id) {
        // Set the procedure ID
        this.id = id; 
    }

    /**
     * Returns the name of the procedure.
     * 
     * @return the procedure name
     */
    public String getName() {
        // Return the procedure name
        return name; 
    }

    /**
     * Sets the name of the procedure.
     * 
     * @param name the procedure name to set
     */
    public void setName(String name) {
        // Set the procedure name
        this.name = name; 
    }

    /**
     * Returns the description of the procedure.
     * 
     * @return the procedure description
     */
    public String getDescription() {
        // Return the procedure description
        return description; 
    }

    /**
     * Sets the description of the procedure.
     * 
     * @param description the procedure description to set
     */
    public void setDescription(String description) {
        // Set the procedure description
        this.description = description; 
    }

    /**
     * Returns the elective status of the procedure.
     * 
     * @return true if the procedure is elective, false otherwise
     */
    public boolean isElective() {
        // Return the elective status
        return isElective; 
    }

    /**
     * Sets the elective status of the procedure.
     * 
     * @param isElective the elective status to set
     */
    public void setElective(boolean isElective) {
        // Set the elective status
        this.isElective = isElective; 
    }

    /**
     * Returns the cost of the procedure.
     * 
     * @return the procedure cost
     */
    public double getCost() {
        // Return the procedure cost
        return cost; 
    }

    /**
     * Sets the cost of the procedure.
     * 
     * @param cost the procedure cost to set
     */
    public void setCost(double cost) {
        // Set the procedure cost
        this.cost = cost; 
    }

    @Override
    public String toString() {
        // String representation of the procedure
        return "ID: " + id + ", Name: " + name + ", Description: "
                + description + ", Elective: " + isElective
                + ", Cost: $" + cost;
    }
}

/**
 * The HealthService class manages medical facilities and patients.
 * It provides methods to add, remove, and display facilities and patients.
 */
class HealthService implements Serializable {
    @Serial
    // Unique identifier for serialization
    private static final long serialVersionUID = 1L;
    // List of facilities managed by the health service
    private final List<MedicalFacility> facilities = new ArrayList<>();
    // List of patients managed by the health service
    private final List<Patient> patients = new ArrayList<>();

    /**
     * Constructs a HealthService with the specified name.
     *
     */
    public HealthService() {

    }

    /**
     * Adds a medical facility to the health service.
     * 
     * @param facility the facility to add
     */
    public void addFacility(MedicalFacility facility) {
        // Add the facility to the list
        facilities.add(facility); 
        // Reassign IDs after adding
        reassignFacilityIds(); 
    }

    /**
     * Adds a patient to the health service.
     * 
     * @param patient the patient to add
     */
    public void addPatient(Patient patient) {
        // Add the patient to the list
        patients.add(patient); 
        // Reassign IDs after adding
        reassignPatientIds(); 
    }

    /**
     * Removes a medical facility from the health service by its ID.
     * 
     * @param id the ID of the facility to remove
     */
    public void removeFacility(int id) {
        // Remove the facility if the ID matches
        facilities.removeIf(f -> f.getId() == id); 
        // Reassign IDs after removal
        reassignFacilityIds(); 
    }

    /**
     * Removes a patient from the health service by their ID.
     * 
     * @param id the ID of the patient to remove
     */
    public void removePatient(int id) {
        // Remove the patient if the ID matches
        patients.removeIf(p -> p.getId() == id); 
        // Reassign IDs after removal
        reassignPatientIds(); 
    }

    /**
     * Reassigns IDs to the facilities in the health service.
     * This ensures that facility IDs are sequential after
     * additions or removals.
     */
    public void reassignFacilityIds() {
        for (int i = 0; i < facilities.size(); i++) {
            // Set new IDs starting from 1
            facilities.get(i).setId(i + 1); 
        }
    }

    /**
     * Reassigns IDs to the patients in the health service.
     * This ensures that patient IDs are sequential after
     * additions or removals.
     */
    public void reassignPatientIds() {
        for (int i = 0; i < patients.size(); i++) {
            // Set new IDs starting from 1
            patients.get(i).setId(i + 1); 
        }
    }

    /**
     * Returns the list of facilities managed by the health service.
     * 
     * @return the list of facilities
     */
    public List<MedicalFacility> getFacilities() {
        // Return the list of facilities
        return facilities; 
    }

    /**
     * Returns the list of patients managed by the health service.
     * 
     * @return the list of patients
     */
    public List<Patient> getPatients() {
        // Return the list of patients
        return patients; 
    }

}