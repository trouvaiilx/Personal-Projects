# Medical Health Service Management

## Overview
The Medical Health Service Management application is a Java-based GUI program designed to manage medical facilities, patients, and procedures. It allows users to add, edit, and delete records for hospitals, clinics, and procedures, as well as perform file operations to save and load data. The application is built using Java Swing components and follows a structured approach to manage healthcare data effectively.

## Features
- **Manage Patients**: Add, edit, and delete patient records, including privacy status and balance.
- **Manage Facilities**: Add, edit, and delete hospitals and clinics, including their procedures.
- **Manage Procedures**: Add, edit, and delete medical procedures associated with hospitals.
- **Visit & Procedure Management**: Record patient visits to facilities and perform medical procedures.
- **File Operations**: Save and load application data to and from files.
- **User -Friendly GUI**: Intuitive interface with icons and tooltips for easy navigation.

## Classes
### `MedicalFacility`
- Abstract class representing a medical facility (hospital or clinic).
- Attributes: `id`, `name`.
- Methods: `visit(Patient patient)` (abstract), `getId()`, `setId(int id)`, `getName()`, `setName(String name)`.

### `Hospital`
- Extends `MedicalFacility`.
- Manages procedures and patient admissions based on admission probability.
- Methods: `addProcedure(Procedure procedure)`, `removeProcedure(int id)`, `performProcedure(Patient patient, Procedure procedure)`.

### `Clinic`
- Extends `MedicalFacility`.
- Manages patient visits and charges based on consultation fees.
- Methods: `visit(Patient patient)`.

### `Patient`
- Represents a patient in the health service system.
- Attributes: `id`, `name`, `isPrivate`, `balance`, `currentFacility`, `registeredFacilities`.
- Methods: `registerAt(MedicalFacility facility)`, `isRegisteredAt(MedicalFacility facility)`.

### `Procedure`
- Represents a medical procedure.
- Attributes: `id`, `name`, `description`, `isElective`, `cost`.
- Methods: `getCost()`, `setCost(double cost)`.

### `HealthService`
- Manages collections of medical facilities and patients.
- Methods: `addFacility(MedicalFacility facility)`, `addPatient(Patient patient)`, `removeFacility(int id)`, `removePatient(int id)`.

### `MedicalGUI`
- Main class for the graphical user interface.
- Initializes the application and handles user interactions.
- Methods: `initializeUI()`, `saveData()`, `loadData()`, `refreshPatientTable()`, `refreshFacilityTable()`, `refreshProcedureTable(String hospitalName)`.

## Usage
1. Compile and run the `MedicalGUI` class to start the application.
2. Use the tabs to manage patients, facilities, and procedures.
3. Use the file operations tab to save or load data.
4. Follow the on-screen prompts to add, edit, or delete records.

## Requirements
- Java Development Kit (JDK) 22 or higher.
- Maven for dependency management, specified in the `pom.xml` file.
- No external libraries are required beyond standard Java libraries.

## Author
- Yuuji

## Latest Update
- December 25th, 2024

## License
This project is licensed under the MIT License - see the [LICENSE](https://github.com/trouvaiilx/Personal-Projects/blob/main/LICENSE) file for details.