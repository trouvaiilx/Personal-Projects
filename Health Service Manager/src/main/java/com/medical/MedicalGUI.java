/**
 * @author: Yuuji
 * MedicalGUI.java
 * Latest Update: 12-25-2024
 * <p> 
 * The MedicalGUI class provides a graphical user interface for
 * managing a medical health service. It allows users to manage
 * patients, facilities, and procedures, as well as perform
 * file operations. The GUI is built using Maven and
 * Java Swing components.
 * 
 */

package com.medical;

/*
  Import statements for various AWT (Abstract Window Toolkit), 
  Swing components, and utilities
 */ 
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import javax.swing.ListCellRenderer;
import javax.swing.RowSorter;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.extras.FlatAnimatedLafChange;

public class MedicalGUI {
    private HealthService healthService;

    // Main JFrame and Table Models
    private JFrame frame;
    private DefaultTableModel patientsModel, 
                               facilitiesModel, 
                               proceduresModel;

    // Declare JTable instances
    private JTable patientsTable, facilitiesTable, proceduresTable;

    // Store a reference to the JTabbedPane
    private JTabbedPane tabbedPane; 
    // Declare textArea as a class member
    private JTextArea textArea; 
    // Declare hospitalComboBox as a class member
    private JComboBox<String> hospitalComboBox; 

    /**
     * The main method to launch the MedicalGUI application.
     * 
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        // Set FlatLaf look and feel
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            JOptionPane.showMessageDialog(
                null, 
                "Failed to set look and feel: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE
            );
            /*
             Optionally log the error if a logging framework is used
             Logger.getLogger(MedicalGUI.class.getName()).log(
             Level.SEVERE,
             null,
             e
             );
            */
        }

        /*
         * Create and show GUI on the Event Dispatch Thread for
         * thread safety
         */
        SwingUtilities.invokeLater(MedicalGUI::new);
    }

    /**
     * Constructs a new MedicalGUI instance, initializing 
     * the health service and UI components.
     */
    public MedicalGUI() {
        healthService = new HealthService();
        initializeSampleData(); // Ensure this is called
        initializeUI();
    }

    /**
     * Initializes the user interface components and sets up 
     * the main application window.
     */
    private void initializeUI() {
        frame = new JFrame("Medical Health Service Management");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Increase frame width to accommodate longer tab titles
        frame.setSize(922, 600); 

        // Set the application icon
        try {
            // Load the application icon image
            Image appIcon = ImageIO.read(
                new File("Health Service Manager/src/main/resources/AppIcon.png")
            );
            frame.setIconImage(appIcon);
        } catch (IOException e) {
            System.err.println(
                "Error: Icon image not found. Please check the file path."
            );
        }

        tabbedPane = new JTabbedPane();
        
        // Set a larger font for the tabbed pane without bold
        // Adjust the size as needed
        Font tabFont = new Font("SansSerif", Font.PLAIN, 16); 
        tabbedPane.setFont(tabFont);

        // Load, resize, and recolor images for tabs
        ImageIcon patientsIcon = resizeAndRecolorIcon(
            new ImageIcon(
                "Health Service Manager/src/main/resources/Patient.png"
            ), 
            20, 
            20, 
            true
        );
        ImageIcon facilitiesIcon = resizeAndRecolorIcon(
            new ImageIcon(
                "Health Service Manager/src/main/resources/Facility.png"
            ), 
            20, 
            20, 
            true
        );
        ImageIcon proceduresIcon = resizeAndRecolorIcon(
            new ImageIcon(
                "Health Service Manager/src/main/resources/Procedure.png"
            ), 
            20, 
            20, 
            true
        );
        ImageIcon visitProcedureIcon = resizeAndRecolorIcon(
            new ImageIcon(
                "Health Service Manager/src/main/resources/VisitProcedure.png"
            ), 
            20, 
            20, 
            true
        );
        ImageIcon fileOperationsIcon = resizeAndRecolorIcon(
            new ImageIcon(
                "Health Service Manager/src/main/resources/FileOperation.png"
            ), 
            20, 
            20, 
            true
        );
        ImageIcon tipsIcon = resizeAndRecolorIcon(
            new ImageIcon(
                "Health Service Manager/src/main/resources/Lightbulb.png"
            ), 
            20, 
            20, 
            false
        ); // No recolor

        tabbedPane.addTab(
            "Manage Patients", 
            patientsIcon, 
            createPatientsPanel()
        );
        tabbedPane.addTab(
            "Manage Facilities", 
            facilitiesIcon, 
            createFacilitiesPanel()
        );
        tabbedPane.addTab(
            "Manage Procedures", 
            proceduresIcon, 
            createProceduresPanel()
        );
        tabbedPane.addTab(
            "Visit & Procedure", 
            visitProcedureIcon, 
            createVisitProcedurePanel()
        );
        tabbedPane.addTab(
            "File Operations", 
            fileOperationsIcon, 
            createFileOperationsPanel()
        );
        // Add the lightbulb icon tab
        tabbedPane.addTab("", tipsIcon, new JPanel()); 

        // Set tab layout policy to scroll if needed
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        /*
         * Add a change listener to show tips
         * when the lightbulb tab is selected
         */
        tabbedPane.addChangeListener(_ -> {
            if (tabbedPane.getSelectedIndex() 
                == tabbedPane.getTabCount() - 1) {
                showTipsPopup();
                // Switch back to the first tab after showing tips
                tabbedPane.setSelectedIndex(0); 
            }
        });

        // Set up the shortcuts
        setupShortcuts(frame.getRootPane());

        frame.add(tabbedPane);

        // Center the frame on the screen
        frame.setLocationRelativeTo(null);

        frame.setVisible(true);

        refreshPatientTable();
        refreshFacilityTable();
        /*
         * Initialize the procedures table
         * with the first hospital's procedures
         */
        if (hospitalComboBox.getItemCount() > 0) {
            refreshProcedureTable(
                    hospitalComboBox.getItemAt(0)
            );
        }
        frame.add(tabbedPane, BorderLayout.CENTER);

        // Create the dark/light switch icon
        JPanel bottomPanel = getBottomPanel();
        // Add to the bottom of the frame
        frame.add(bottomPanel, BorderLayout.SOUTH); 

        // Center the frame on the screen
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Refresh tables
        refreshPatientTable();
        refreshFacilityTable();
        if (hospitalComboBox.getItemCount() > 0) {
            refreshProcedureTable(hospitalComboBox.getItemAt(0));
        }
    }

    private JPanel getBottomPanel() {
        DarkLightSwitchIcon darkLightSwitchIcon = 
            new DarkLightSwitchIcon();
        JToggleButton themeToggleButton = 
            new JToggleButton(darkLightSwitchIcon);

        // Set the action listener for the toggle button
        themeToggleButton.addActionListener(_ -> {
            // Change themes based on toggle button state
            changeThemes(themeToggleButton.isSelected());
        });

        // Customize the toggle button to remove highlight
        themeToggleButton.setFocusPainted(false); // Remove focus outline

        // Add the toggle button to the bottom of the frame
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(themeToggleButton);
        return bottomPanel;
    }

    // Add the changeThemes method from Test.java
    private void changeThemes(boolean dark) {
        if (FlatLaf.isLafDark() != dark) {
            if (!dark) {
                EventQueue.invokeLater(() -> {
                    FlatAnimatedLafChange.showSnapshot();
                    FlatIntelliJLaf.setup();
                    FlatLaf.updateUI();
                    FlatAnimatedLafChange.hideSnapshotWithAnimation();
                });
            } else {
                EventQueue.invokeLater(() -> {
                    FlatAnimatedLafChange.showSnapshot();
                    FlatDarculaLaf.setup();
                    FlatLaf.updateUI();
                    FlatAnimatedLafChange.hideSnapshotWithAnimation();
                });
            }
        }
    }

    /**
     * Initializes sample data for the application, 
     * including facilities and procedures.
     */
    private void initializeSampleData() {
        Hospital mainHospital = new Hospital(
            "Main Hospital", 
            0.5
        );
        healthService.addFacility(mainHospital);

        Procedure electiveProcedure = new Procedure(
            "Knee Replacement", 
            "Surgical replacement of the knee joint", 
            true, 
            1500
        );
        Procedure nonElectiveProcedure = new Procedure(
            "Appendectomy", 
            "Surgical removal of the appendix", 
            false, 
            850
        );
        mainHospital.addProcedure(electiveProcedure);
        mainHospital.addProcedure(nonElectiveProcedure);
        mainHospital.addProcedure(new Procedure(
            "Hip Replacement", 
            "Surgical replacement of the hip joint", 
            true, 
            2000
        ));
        mainHospital.addProcedure(new Procedure(
            "Cataract Surgery", 
            "Removal of cataract from the eye", 
            true, 
            1200
        ));
        mainHospital.addProcedure(new Procedure(
            "Heart Bypass", 
            "Surgical bypass of blocked heart arteries", 
            false, 
            2000
        ));
        mainHospital.addProcedure(new Procedure(
            "Tonsillectomy", 
            "Removal of tonsils", 
            true, 
            600
        ));
        mainHospital.addProcedure(new Procedure(
            "Gallbladder Removal", 
            "Surgical removal of the gallbladder", 
            false, 
            1000
        ));
        mainHospital.addProcedure(new Procedure(
            "Hernia Repair", 
            "Surgical repair of hernia", 
            true, 
            800
        ));
        mainHospital.addProcedure(new Procedure(
            "Colonoscopy", 
            "Examination of the colon", 
            false, 
            400
        ));
        mainHospital.addProcedure(new Procedure(
            "Mammogram", 
            "Breast cancer screening", 
            true, 
            300
        ));

        healthService.addFacility(new Clinic("General Clinic", 
        50, 10));

        // Add other hospitals and their procedures
        Hospital cityHospital = new Hospital(
            "City Hospital", 
            0.6
        );
        cityHospital.addProcedure(new Procedure(
            "Cardiac Checkup", 
            "Comprehensive heart examination", 
            false, 
            1200
        ));
        cityHospital.addProcedure(new Procedure(
            "Liver Transplant", 
            "Surgical replacement of the liver", 
            false, 
            2000
        ));
        cityHospital.addProcedure(new Procedure(
            "Dialysis", 
            "Kidney dialysis treatment", 
            false, 
            500
        ));
        healthService.addFacility(cityHospital);

        healthService.addFacility(new Clinic("Downtown Clinic", 
        60, 15));

        Hospital suburbanHospital = new Hospital(
            "Suburban Hospital", 
            0.4
        );
        suburbanHospital.addProcedure(new Procedure(
            "Dental Cleaning", 
            "Routine dental cleaning", 
            true, 
            100
        ));
        suburbanHospital.addProcedure(new Procedure(
            "Root Canal", 
            "Dental root canal treatment", 
            true, 
            800
        ));
        suburbanHospital.addProcedure(new Procedure(
            "Braces Installation", 
            "Orthodontic braces installation", 
            true, 
            1700
        ));
        healthService.addFacility(suburbanHospital);

        healthService.addFacility(new Clinic("Uptown Clinic", 
        55, 12));

        Hospital ruralHospital = new Hospital(
            "Rural Hospital", 
            0.7
        );
        ruralHospital.addProcedure(new Procedure(
            "Vaccination", 
            "Routine vaccination", 
            true, 
            50
        ));
        ruralHospital.addProcedure(new Procedure(
            "Fracture Treatment", 
            "Treatment for bone fractures", 
            false, 
            700
        ));
        ruralHospital.addProcedure(new Procedure(
            "Childbirth", 
            "Assistance in childbirth", 
            false, 
            2000
        ));
        healthService.addFacility(ruralHospital);

        healthService.addFacility(
            new Clinic("Eastside Clinic", 45, 8)
        );

        Hospital westsideHospital = new Hospital(
            "Westside Hospital", 0.3
        );
        westsideHospital.addProcedure(new Procedure(
            "Eye Exam", 
            "Comprehensive eye examination", 
            true, 
            150
        ));
        westsideHospital.addProcedure(new Procedure(
            "Glaucoma Surgery", 
            "Surgical treatment for glaucoma", 
            false, 
            2000
        ));
        westsideHospital.addProcedure(new Procedure(
            "Lasik Surgery", 
            "Laser eye surgery", 
            true, 
            2000
        ));
        healthService.addFacility(westsideHospital);

        healthService.addFacility(new Clinic("Northside Clinic", 
        65, 20));

        healthService.addPatient(new Patient("Jackie", true));
        healthService.addPatient(new Patient("Chan", false));
        healthService.addPatient(new Patient("Bruce", true));
        healthService.addPatient(new Patient("Lee", false));
        healthService.addPatient(new Patient("Jet", true));
        healthService.addPatient(new Patient("Li", false));
        healthService.addPatient(new Patient("Donnie", true));
        healthService.addPatient(new Patient("Yen", false));
        healthService.addPatient(new Patient("Michelle", true));
        healthService.addPatient(new Patient("Yeoh", false));
    }

    /**
     * Centers the content of the specified column in a JTable.
     * 
     * @param table the JTable whose column content is to be centered
     */
    private void centerAlignColumn(JTable table) {
        DefaultTableCellRenderer centerRenderer = 
            new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(
            SwingConstants.CENTER
        );
        table.getColumnModel()
            .getColumn(0)
            .setCellRenderer(centerRenderer);
    }

    /**
     * Creates and returns the panel for managing patients.
     * 
     * @return a JPanel containing the patient management interface
     */
    private JPanel createPatientsPanel() {
        // Create main panel with BorderLayout
        JPanel panel = new JPanel(new BorderLayout());

        // Initialize table model with column headers
        patientsModel = new DefaultTableModel(
            new String[]{"ID", "Name", "Private", "Balance"}, 
            0
        ) {
            @Override
            public Class<?> getColumnClass(int column) {
                // Define data types for each column:
                // ID -> Integer
                // Name -> String (default)
                // Private -> Boolean
                // Balance -> Double
                return switch (column) {
                    case 0 -> Integer.class;
                    case 2 -> Boolean.class;
                    case 3 -> Double.class;
                    default -> String.class;
                };
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                // Make all cells non-editable
                return false;
            }
        };
        patientsTable = new JTable(patientsModel);
        customizeTable(patientsTable); // Apply customizations
        
        TableRowSorter<DefaultTableModel> sorter = 
            new TableRowSorter<>(patientsModel);
        patientsTable.setRowSorter(sorter);
        
        panel.add(new JScrollPane(patientsTable), 
                   BorderLayout.CENTER);

        /* 
         * Set specific widths for columns
         * (ID=50, Name=auto, Private=70, Balance=100)
         */
        setColumnWidths(patientsTable, 50, 0, 80, 100);

        // Set row height for better readability
        // Set the desired row height
        patientsTable.setRowHeight(30); 

        // Center align the ID column
        centerAlignColumn(patientsTable);

        // Create buttons with updated font size
        JPanel buttonPanel = getPatientButtonPanel();

        panel.add(buttonPanel, 
                   BorderLayout.NORTH);

        return panel;
    }

    private JPanel getPatientButtonPanel() {
        Font buttonFont = new Font("SansSerif", Font.PLAIN, 14);

        JButton refreshButton = new JButton("Refresh");
        refreshButton.setFont(buttonFont);
        refreshButton.addActionListener(_ -> refreshPatientTable());

        JPanel mainButtonPanel = getMainPatientButtonPanel(buttonFont);

        // Create a panel for the refresh button
        JPanel refreshPanel = new JPanel(new BorderLayout());
        refreshPanel.add(refreshButton, 
                          BorderLayout.EAST);

        // Combine both panels
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.add(mainButtonPanel, 
                        BorderLayout.CENTER);
        buttonPanel.add(refreshPanel, 
                         BorderLayout.EAST);
        return buttonPanel;
    }

    private JPanel getMainPatientButtonPanel(Font buttonFont) {
        JButton addButton = new JButton("Add Patient");
        addButton.setFont(buttonFont);
        addButton.addActionListener(_ -> addPatient());

        JButton deleteButton = new JButton("Delete Patient");
        deleteButton.setFont(buttonFont);
        deleteButton.addActionListener(_ -> deletePatient());

        JButton editButton = new JButton("Edit Patient");
        editButton.setFont(buttonFont);
        editButton.addActionListener(_ -> editPatient());

        // Create a panel for main buttons
        JPanel mainButtonPanel = new JPanel(new GridLayout(1, 3));
        mainButtonPanel.add(addButton);
        mainButtonPanel.add(deleteButton);
        mainButtonPanel.add(editButton);
        return mainButtonPanel;
    }

    /**
     * Adds a new patient to the health service.
     */
    private void addPatient() {
        while (true) {
            // Create input fields for patient details
            JTextField nameField = new JTextField();
            // Set preferred width
            nameField.setPreferredSize(new Dimension(100, 25));
            JCheckBox privateCheck = new JCheckBox("Private Patient");

            // Create and configure the input panel with GridBagLayout
            JPanel inputPanel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            // Add padding
            gbc.insets = new Insets(5, 5, 5, 5);

            // Add labels and fields to the input panel
            gbc.anchor = GridBagConstraints.WEST; // Align to the left
            gbc.gridx = 0; // Column for labels
            gbc.gridy = 0; // Row for name
            inputPanel.add(new JLabel("Name: "), gbc);
            gbc.gridx = 1; // Column for input field
            inputPanel.add(nameField, gbc);

            gbc.gridx = 0; // Column for private label
            gbc.gridy = 1; // Row for private checkbox
            inputPanel.add(privateCheck, gbc);

            // Show dialog and get user response
            int option = JOptionPane.showConfirmDialog(
                frame, 
                inputPanel, 
                "Add Patient", 
                JOptionPane.OK_CANCEL_OPTION
            );
            if (option == JOptionPane.CANCEL_OPTION || 
                option == JOptionPane.CLOSED_OPTION) {
                // Exit if the user cancels
                return; 
            }

            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(
                    frame, 
                    "Name cannot be empty.", 
                    "Input Error", 
                    JOptionPane.ERROR_MESSAGE
                );
            } else {
                healthService.addPatient(
                    new Patient(name, privateCheck.isSelected())
                );
                // Refresh the table after adding
                refreshPatientTable(); 
                // Exit the loop if input is valid
                break; 
            }
        }
    }

    /**
     * Deletes a selected patient from the health service.
     */
    private void deletePatient() {
        // Get list of all patients from health service
        List<Patient> patients = healthService.getPatients();
        
        // Create dropdown with patient names
        JComboBox<String> patientComboBox = new JComboBox<>(
            patients.stream()
                    .map(Patient::getName)
                    .toArray(String[]::new)
        );

        // Show dialog for user to select patient to delete
        int option = JOptionPane.showConfirmDialog(
            frame, 
            patientComboBox, 
            "Select Patient to Delete", 
            JOptionPane.OK_CANCEL_OPTION
        );
                                                 
        if (option == JOptionPane.OK_OPTION) {
            // Get selected patient name from dropdown
            String selectedName =
                    (String) patientComboBox.getSelectedItem();
            
            // Find matching patient object
            Patient patient = patients.stream()
                                      .filter(p -> p.getName()
                                                     .equals(selectedName))
                                      .findFirst()
                                      .orElse(null);
                                    
            if (patient != null) {
                // Remove patient and update UI
                healthService.removePatient(patient.getId());
                refreshPatientTable();
                JOptionPane.showMessageDialog(
                    frame, 
                    "Patient deleted."
                );
            }
        }
    }

    /**
     * Edits the details of a selected patient.
     */
    private void editPatient() {
        // Get list of all patients from health service
        List<Patient> patients = healthService.getPatients();
        
        // Create dropdown with patient names
        JComboBox<String> patientComboBox = new JComboBox<>(
            patients.stream()
                    .map(Patient::getName)
                    .toArray(String[]::new)
        );

        // Show dialog for user to select patient to edit
        int option = JOptionPane.showConfirmDialog(
            frame, 
            patientComboBox, 
            "Select Patient to Edit", 
            JOptionPane.OK_CANCEL_OPTION
        );
        
        if (option == JOptionPane.OK_OPTION) {
            // Get selected patient name from dropdown
            String selectedName =
                    (String) patientComboBox.getSelectedItem();
            
            // Find matching patient object
            Patient patient = patients.stream()
                                      .filter(p -> p.getName()
                                                     .equals(selectedName))
                                      .findFirst()
                                      .orElse(null);
                                    
            if (patient != null) {
                /*
                 * Keep prompting for valid input
                 * until successful or cancelled
                 */
                while (true) {
                    /*
                     * Create input fields pre-populated
                     * with current values
                     */
                    JTextField nameField =
                            new JTextField(patient.getName());
                    // Set preferred width
                    nameField.setPreferredSize(new Dimension(100, 25));
                    
                    /*
                     * Create and configure the
                     * input panel with GridBagLayout
                     */
                    JPanel inputPanel = new JPanel(new GridBagLayout());
                    GridBagConstraints gbc = new GridBagConstraints();
                    // Add padding
                    gbc.insets = new Insets(5, 5, 5, 5);

                    // Add labels and fields to the input panel
                    gbc.anchor = GridBagConstraints.WEST; // Align Left
                    gbc.gridx = 0; // Column for labels
                    gbc.gridy = 0; // Row for name
                    inputPanel.add(new JLabel("New Name: "), gbc);
                    gbc.gridx = 1; // Column for input field
                    inputPanel.add(nameField, gbc);

                    // Show edit dialog
                    int editOption = JOptionPane.showConfirmDialog(
                        frame, 
                        inputPanel, 
                        "Edit Patient", 
                        JOptionPane.OK_CANCEL_OPTION
                    );
                    
                    if (editOption == JOptionPane.CANCEL_OPTION || 
                        editOption == JOptionPane.CLOSED_OPTION) {
                        return; // Exit if the user cancels
                    }

                    // Get and validate new name
                    String newName = nameField.getText().trim();
                    if (newName.isEmpty()) {
                        JOptionPane.showMessageDialog(
                            frame, 
                            "Name cannot be empty.", 
                            "Input Error", 
                            JOptionPane.ERROR_MESSAGE
                        );
                    } else {
                        // Update patient name and refresh display
                        patient.setName(newName);
                        refreshPatientTable();
                        break; // Exit the loop if input is valid
                    }
                }
            } else {
                JOptionPane.showMessageDialog(
                    frame, 
                    "Patient not found."
                );
            }
        }
    }

    /**
     * Refreshes the patient table to reflect the current data.
     */
    private void refreshPatientTable() {
        // Clear existing rows
        patientsModel.setRowCount(0); 
        for (Patient patient : healthService.getPatients()) {
            patientsModel.addRow(new Object[]{
                patient.getId(),
                patient.getName(),
                patient.isPrivate(),
                patient.getBalance()
            });
        }
        // Center align the ID column
        centerAlignColumn(patientsTable); 
    }

    /**
     * Creates and returns the panel for managing facilities.
     * 
     * @return a JPanel containing the facility management interface
     */
    private JPanel createFacilitiesPanel() {
        // Create main panel with BorderLayout
        JPanel panel = new JPanel(new BorderLayout());

        // Create table model with ID, Name and Type columns
        facilitiesModel = new DefaultTableModel(
            new String[]{"ID", "Name", "Type"}, 
            0
        ) {
            @Override
            public Class<?> getColumnClass(int column) {
                // Return Integer for ID column, String for others
                return column == 0 ? Integer.class : String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                // Prevent direct editing of cells
                return false;
            }
        };

        // Create facilities table with the model
        facilitiesTable = new JTable(facilitiesModel);
        // Apply customizations
        customizeTable(facilitiesTable); 

        // Add sorting capability to the table
        TableRowSorter<DefaultTableModel> sorter = 
            new TableRowSorter<>(facilitiesModel);
        facilitiesTable.setRowSorter(sorter);

        // Add table to panel inside a scroll pane
        panel.add(
            new JScrollPane(facilitiesTable), 
            BorderLayout.CENTER
        );

        // Set specific widths for columns (ID=50, Name=auto, Type=100)
        setColumnWidths(facilitiesTable, 50, 0, 100);

        // Set row height for better readability
        // Set the desired row height
        facilitiesTable.setRowHeight(30); 

        // Center align the ID column
        centerAlignColumn(facilitiesTable);

        // Create buttons with updated font size
        JPanel buttonPanel = getFacilityPanel();

        panel.add(buttonPanel, BorderLayout.NORTH);

        return panel;
    }

    private JPanel getFacilityPanel() {
        Font buttonFont = new Font("SansSerif", Font.PLAIN, 14);

        JButton refreshButton = new JButton("Refresh");
        refreshButton.setFont(buttonFont);
        refreshButton.addActionListener(_ -> refreshFacilityTable());

        JPanel mainButtonPanel = getMainFacilityButtonPanel(buttonFont);

        // Create a panel for the refresh button
        JPanel refreshPanel = new JPanel(new BorderLayout());
        refreshPanel.add(refreshButton, BorderLayout.EAST);

        // Combine both panels
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.add(mainButtonPanel, BorderLayout.CENTER);
        buttonPanel.add(refreshPanel, BorderLayout.EAST);
        return buttonPanel;
    }

    private JPanel getMainFacilityButtonPanel(Font buttonFont) {
        JButton addButton = new JButton("Add Facility");
        addButton.setFont(buttonFont);
        addButton.addActionListener(_ -> addFacility());

        JButton deleteButton = new JButton("Delete Facility");
        deleteButton.setFont(buttonFont);
        deleteButton.addActionListener(_ -> deleteFacility());

        JButton editButton = new JButton("Edit Facility");
        editButton.setFont(buttonFont);
        editButton.addActionListener(_ -> editFacility());

        // Create a panel for main buttons
        JPanel mainButtonPanel = new JPanel(new GridLayout(1, 3));
        mainButtonPanel.add(addButton);
        mainButtonPanel.add(deleteButton);
        mainButtonPanel.add(editButton);
        return mainButtonPanel;
    }

    /**
     * Adds a new facility to the health service.
     */
    private void addFacility() {
        // Keep prompting until valid input is received or user cancels
        while (true) {
            // Show facility type selection dialog
            String[] facilityTypes = {"Hospital", "Clinic"};
            String selectedType = (String) JOptionPane.showInputDialog(
                frame, 
                "Select Facility Type:", 
                "Add Facility",
                JOptionPane.PLAIN_MESSAGE, 
                null, 
                facilityTypes, 
                facilityTypes[0]
            );

            // Exit if user cancels facility type selection
            if (selectedType == null) {
                return;
            }

            // Create input fields for facility name
            JTextField nameField = new JTextField();
            nameField.setPreferredSize(new Dimension(150, 25));
            JPanel inputPanel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            // Add padding
            gbc.insets = new Insets(5, 5, 5, 5);

            // Add labels and fields to the input panel
            gbc.anchor = GridBagConstraints.WEST; // Align to the left
            gbc.gridx = 0; // Column for labels
            gbc.gridy = 0; // Row for name
            inputPanel.add(new JLabel("Name: "), gbc);
            gbc.gridx = 1; // Column for input field
            inputPanel.add(nameField, gbc);

            // Handle Hospital-specific input
            if ("Hospital".equals(selectedType)) {
                // Add admission probability field for hospitals
                JTextField probAdmitField = new JTextField();
                // Set preferred width
                probAdmitField.setPreferredSize(new Dimension(150, 25));
                gbc.gridx = 0; // Column for admission probability label
                gbc.gridy = 1; // Row for admission probability
                inputPanel.add(new JLabel("Admission Probability: "), gbc);
                gbc.gridx = 1; // Column for admission probability input
                inputPanel.add(probAdmitField, gbc);

                // Show hospital input dialog
                int option = JOptionPane.showConfirmDialog(
                    frame,
                    inputPanel,
                    "Add Hospital", 
                    JOptionPane.OK_CANCEL_OPTION
                );
                if (option == JOptionPane.CANCEL_OPTION || 
                    option == JOptionPane.CLOSED_OPTION) {
                    // Exit if user cancels
                    return; 
                }

                // Get and validate input values
                String name = nameField.getText().trim();
                String probAdmitText = probAdmitField.getText().trim();
                if (name.isEmpty() || probAdmitText.isEmpty()) {
                    JOptionPane.showMessageDialog(
                        frame, 
                        "Fields cannot be empty.", 
                        "Input Error", 
                        JOptionPane.ERROR_MESSAGE
                    );
                } else {
                    try {
                        // Create and add new hospital if input is valid
                        double probAdmit =
                                Double.parseDouble(probAdmitText);
                        Hospital hospital = new Hospital(name, probAdmit);
                        healthService.addFacility(hospital);
                        refreshFacilityTable();
                        // Refresh the combo box
                        refreshHospitalComboBox();
                        // Exit loop on successful addition
                        break; 
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(
                            frame, 
                            "Invalid input for admission probability."
                        );
                    }
                }
            } 
            // Handle Clinic-specific input
            else if ("Clinic".equals(selectedType)) {
                // Add consultation fee and gap percent fields for clinics
                JTextField consultationFeeField = new JTextField();
                consultationFeeField.setPreferredSize
                        (new Dimension(150, 25));
                JTextField gapPercentField = new JTextField();
                gapPercentField.setPreferredSize(new Dimension(150, 25));
                gbc.gridx = 0; // Column for consultation fee label
                gbc.gridy = 1; // Row for consultation fee
                inputPanel.add(new JLabel("Consultation Fee: "), gbc);
                gbc.gridx = 1; // Column for consultation fee input
                inputPanel.add(consultationFeeField, gbc);
                gbc.gridx = 0; // Column for gap percent label
                gbc.gridy = 2; // Row for gap percent
                inputPanel.add(new JLabel("Gap Percent: "), gbc);
                gbc.gridx = 1; // Column for gap percent input
                inputPanel.add(gapPercentField, gbc);

                // Show clinic input dialog
                int option = JOptionPane.showConfirmDialog(
                    frame,
                    inputPanel,
                    "Add Clinic", 
                    JOptionPane.OK_CANCEL_OPTION
                );
                if (option == JOptionPane.CANCEL_OPTION || 
                    option == JOptionPane.CLOSED_OPTION) {
                    // Exit if user cancels
                    return; 
                }

                // Get and validate input values
                String name = nameField.getText().trim();
                String consultationFeeText = 
                    consultationFeeField.getText().trim();
                String gapPercentText = 
                    gapPercentField.getText().trim();
                if (name.isEmpty() || 
                    consultationFeeText.isEmpty() || 
                    gapPercentText.isEmpty()) {
                    JOptionPane.showMessageDialog(
                        frame, 
                        "Fields cannot be empty.", 
                        "Input Error", 
                        JOptionPane.ERROR_MESSAGE
                    );
                } else {
                    try {
                        // Create and add new clinic if input is valid
                        double consultationFee = 
                            Double.parseDouble(consultationFeeText);
                        double gapPercent = 
                            Double.parseDouble(gapPercentText);
                        Clinic clinic = new Clinic(
                            name, 
                            consultationFee, 
                            gapPercent
                        );
                        healthService.addFacility(clinic);
                        refreshFacilityTable();
                        // Refresh the combo box
                        refreshHospitalComboBox();
                        // Exit loop on successful addition
                        break; 
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(
                            frame, 
                            "Invalid input for consultation " +
                                    "fee or gap percent."
                        );
                    }
                }
            }
        }
    }

    /**
     * Deletes a selected facility from the health service.
     */
    private void deleteFacility() {
        // Get the ID of the facility to delete
        int facilityId = selectFacilityId();
        // Exit if no facility is selected
        if (facilityId == -1) return; 

        // Find the facility with the given ID
        MedicalFacility facility = healthService.getFacilities()
            .stream()
            .filter(f -> f.getId() == facilityId)
            .findFirst()
            .orElse(null);
        
        if (facility != null) {
            // Build confirmation message
            StringBuilder message = new StringBuilder(
                "Are you sure you want to delete the facility: " + 
                facility.getName() + "?");

            // If facility is a hospital, show its procedures
            if (facility instanceof Hospital hospital) {
                List<Procedure> procedures = hospital.getProcedures();
                if (!procedures.isEmpty()) {
                    // List all procedures if any exist
                    message.append("\nExisting Procedures:\n");
                    for (Procedure procedure : procedures) {
                        message.append("- ")
                            .append(procedure.getName())
                            .append("\n");
                    }
                } else {
                    message.append("\nNo procedures available.");
                }
            }

            // Show confirmation dialog to user
            int option = JOptionPane.showConfirmDialog(
                frame, 
                message.toString(), 
                "Confirm Delete", 
                JOptionPane.YES_NO_OPTION);
            
            if (option == JOptionPane.YES_OPTION) {
                // Remove facility if user confirms
                healthService.removeFacility(facilityId);
                refreshFacilityTable();
                
                /*
                 * If deleted facility was a hospital,
                 * refresh procedures table
                 */
                if (facility instanceof Hospital) {
                    if (hospitalComboBox.getItemCount() > 0) {
                        // Refresh with first available hospital
                        refreshProcedureTable(
                                hospitalComboBox.getItemAt(0));
                    }
                }
                JOptionPane.showMessageDialog(
                    frame, 
                    "Facility deleted."
                );
            }
        } else {
            // Show error if facility not found
            JOptionPane.showMessageDialog(
                frame, 
                "Facility not found."
            );
        }
    }

    /**
     * Edits the details of a selected facility.
     */
    private void editFacility() {
        // Get the ID of the facility to edit
        int facilityId = selectFacilityId();
        // Exit if no facility is selected
        if (facilityId == -1) return; 

        // Find the facility with the given ID
        MedicalFacility facility = healthService.getFacilities()
            .stream()
            .filter(f -> f.getId() == facilityId)
            .findFirst()
            .orElse(null);
        
        if (facility != null) {
            /*
             * Keep prompting for valid input
             * until successful or user cancels
             */
            while (true) {
                // Create input fields
                JTextField nameField = new JTextField(facility.getName());
                JTextField feeField = new JTextField();
                JTextField gapPercentField = new JTextField();
                JPanel inputPanel = new JPanel(new GridLayout(0, 2));
                inputPanel.add(new JLabel("New Name: "));
                inputPanel.add(nameField);

                // Add appropriate fields based on facility type
                switch (facility) {
                    case Hospital hospital -> {
                        inputPanel.add(
                            new JLabel("Admission Probability: ")
                        );
                        feeField.setText(
                            String.valueOf(hospital.getProbAdmit())
                        );
                        inputPanel.add(feeField);
                    }
                    case Clinic clinic -> {
                        inputPanel.add(
                            new JLabel("Consultation Fee: ")
                        );
                        feeField.setText(
                            String.valueOf(clinic.getConsultationFee())
                        );
                        inputPanel.add(feeField);
                        inputPanel.add(new JLabel("Gap Percent: "));
                        gapPercentField.setText(
                            String.valueOf(clinic.getGapPercent())
                        );
                        inputPanel.add(gapPercentField);
                    }
                    default -> {
                    }
                }

                // Show edit dialog to user
                int option = JOptionPane.showConfirmDialog(
                    frame, 
                    inputPanel, 
                    "Edit Facility", 
                    JOptionPane.OK_CANCEL_OPTION
                );
                if (option == JOptionPane.CANCEL_OPTION ||
                    option == JOptionPane.CLOSED_OPTION) {
                    return; // Exit if the user cancels
                }

                // Get input values
                String newName = nameField.getText().trim();
                String feeText = feeField.getText().trim();
                String gapPercentText = gapPercentField.getText().trim();

                // Validate input fields are not empty
                if (newName.isEmpty() || feeText.isEmpty() ||
                    (facility instanceof Clinic
                            && gapPercentText.isEmpty())) {
                    JOptionPane.showMessageDialog(
                        frame, 
                        "Fields cannot be empty.", 
                        "Input Error", 
                        JOptionPane.ERROR_MESSAGE
                    );
                } else {
                    try {
                        // Update facility name
                        facility.setName(newName);

                        // Update type-specific fields
                        switch (facility) {
                            case Hospital hospital -> {
                                double probAdmit =
                                    Double.parseDouble(feeText);
                                hospital.setProbAdmit(probAdmit);
                            }
                            case Clinic clinic -> {
                                double consultationFee =
                                    Double.parseDouble(feeText);
                                double gapPercent =
                                    Double.parseDouble(gapPercentText);
                                clinic.setConsultationFee(consultationFee);
                                clinic.setGapPercent(gapPercent);
                            }
                            default -> {
                            }
                        }
                        // Refresh display and exit loop
                        refreshFacilityTable();
                        break;
                    } catch (NumberFormatException e) {
                        // Show error for invalid number format
                        JOptionPane.showMessageDialog(
                            frame,
                            "Invalid input for fee or gap percent."
                        );
                    }
                }
            }
        } else {
            // Show error if facility not found
            JOptionPane.showMessageDialog(frame, "Facility not found.");
        }
    }

    /**
     * Refreshes the facility table to reflect the current data.
     */
    private void refreshFacilityTable() {
        // Clear existing rows
        facilitiesModel.setRowCount(0); 
        for (MedicalFacility facility : healthService.getFacilities()) {
            facilitiesModel.addRow(new Object[]{
                facility.getId(),
                facility.getName(),
                facility.getClass().getSimpleName()
            });
        }
        // Center align the ID column
        centerAlignColumn(facilitiesTable); 
    }

    /**
     * Creates and returns the panel for managing procedures.
     * 
     * @return a JPanel containing the procedure management interface
     */
    private JPanel createProceduresPanel() {
        // Create main panel with BorderLayout
        JPanel panel = new JPanel(new BorderLayout());
        
        /*
         * Initialize the JComboBox for
         * selecting hospitals. This will
         * be used to filter procedures by hospital
        */
        hospitalComboBox = new JComboBox<>();
        healthService.getFacilities().forEach(facility -> {
            if (facility instanceof Hospital) {
                hospitalComboBox.addItem(facility.getName());
            }
        });
        
        /*
         * Set font for the JComboBox to match
         * application style. Using SansSerif
         * 14pt for better readability
        */
        Font comboBoxFont = new Font("SansSerif", Font.PLAIN, 14);
        hospitalComboBox.setFont(comboBoxFont);
        
        /*
         * Add an action listener to dynamically update
         * the procedures table when a different hospital
         * is selected from the dropdown
        */
        hospitalComboBox.addActionListener(_ ->
            refreshProcedureTable(
                (String) hospitalComboBox.getSelectedItem()
            )
        );
        
        // Initialize the table model with columns for procedure details
        // Column order: ID, Name, Description, Elective status, Cost
        proceduresModel = new DefaultTableModel(
            new String[] {
                "ID", 
                "Name", 
                "Description", 
                "Elective", 
                "Cost"
            }, 
            0
        ) {
            @Override
            /*
              Define data types for each column to enable 
              proper sorting and rendering
             */
            public Class<?> getColumnClass(int column) {
                return switch (column) {
                    // ID column
                    case 0 -> Integer.class;  
                    // Elective column
                    case 3 -> Boolean.class;  
                    // Cost column
                    case 4 -> Double.class;   
                    // Name and Description columns
                    default -> String.class;  
                };
            }

            @Override
            // Prevent direct editing of cells in the table
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        // Create and configure the procedures table
        proceduresTable = new JTable(proceduresModel);
        // Apply standard table customizations
        customizeTable(proceduresTable); 
        
        // Add sorting capability to the table
        TableRowSorter<DefaultTableModel> sorter = 
            new TableRowSorter<>(proceduresModel);
        proceduresTable.setRowSorter(sorter);
        
        // Add table to panel inside a scroll pane for scrolling capability
        panel.add(new JScrollPane(proceduresTable), BorderLayout.CENTER);

        /*
          Set specific widths for columns (ID=50, Name=150, 
          Description=auto, Elective=70, Cost=100)
         */
        setColumnWidths(proceduresTable, 50, 150, 0, 80, 100);

        // Add word wrap for description column
        proceduresTable.getColumnModel()
            .getColumn(2)
            .setCellRenderer(new WordWrapCellRenderer());

        // Set row height to accommodate wrapped text
        proceduresTable.setRowHeight(50);

        // Center align the ID column
        centerAlignColumn(proceduresTable);

        // Create buttons with updated font size
        // Slightly larger font
        JPanel buttonPanel = getProcedureButtonPanel();

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(hospitalComboBox, BorderLayout.NORTH);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);
        panel.add(topPanel, BorderLayout.NORTH);

        return panel;
    }

    private JPanel getProcedureButtonPanel() {
        Font buttonFont = new Font("SansSerif", Font.PLAIN, 14);

        JButton refreshButton = new JButton("Refresh");
        refreshButton.setFont(buttonFont);
        refreshButton.addActionListener(_ ->
            refreshProcedureTable(
                (String) hospitalComboBox.getSelectedItem()
            )
        );

        JPanel mainButtonPanel = getMainProcedureButtonPanel(buttonFont);

        // Create a panel for the refresh button
        JPanel refreshPanel = new JPanel(new BorderLayout());
        refreshPanel.add(refreshButton, BorderLayout.EAST);

        // Combine both panels
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.add(mainButtonPanel, BorderLayout.CENTER);
        buttonPanel.add(refreshPanel, BorderLayout.EAST);
        return buttonPanel;
    }

    private JPanel getMainProcedureButtonPanel(Font buttonFont) {
        JButton addButton = new JButton("Add Procedure");
        addButton.setFont(buttonFont);
        addButton.addActionListener(_ -> addProcedure());

        JButton deleteButton = new JButton("Delete Procedure");
        deleteButton.setFont(buttonFont);
        deleteButton.addActionListener(_ -> deleteProcedure());

        JButton editButton = new JButton("Edit Procedure");
        editButton.setFont(buttonFont);
        editButton.addActionListener(_ -> editProcedure());

        // Create a panel for main buttons
        JPanel mainButtonPanel = new JPanel(new GridLayout(1, 3));
        mainButtonPanel.add(addButton);
        mainButtonPanel.add(deleteButton);
        mainButtonPanel.add(editButton);
        return mainButtonPanel;
    }

    /**
     * Refreshes the procedure table for the specified hospital.
     * 
     * @param hospitalName the name of the hospital whose procedures 
     * are to be displayed
     */
    private void refreshProcedureTable(String hospitalName) {
        // Clear existing rows
        proceduresModel.setRowCount(0); 
        healthService.getFacilities().forEach(facility -> {
            if (facility instanceof Hospital 
                && facility.getName().equals(hospitalName)) {
                for (Procedure procedure : ((Hospital) facility)
                    .getProcedures()) {
                    proceduresModel.addRow(new Object[]{
                        procedure.getId(),
                        procedure.getName(),
                        procedure.getDescription(),
                        procedure.isElective(),
                        procedure.getCost()
                    });
                }
            }
        });
        // Center align the ID column
        centerAlignColumn(proceduresTable); 
    }

    /**
     * Adds a new procedure to the selected hospital.
     */
    private void addProcedure() {
        // Loop until valid input is received or user cancels
        while (true) {
            // Create input fields for procedure details
            JTextField nameField = new JTextField();
            JTextArea descriptionArea = new JTextArea(5, 30);
            descriptionArea.setLineWrap(true);
            descriptionArea.setWrapStyleWord(true);
            JScrollPane descriptionPane = new JScrollPane(descriptionArea);
            JTextField costField = new JTextField();
            JCheckBox electiveCheck = new JCheckBox("Elective");

            // Set preferred size for nameField to match descriptionPane
            nameField.setPreferredSize(new Dimension(344, 25));

            // Create and configure the input panel with GridBagLayout
            JPanel inputPanel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);

            // Add labels and fields to the input panel
            gbc.anchor = GridBagConstraints.WEST; // Align to the left
            gbc.gridx = 0; // Column for labels
            gbc.gridy = 0; // Row for name
            inputPanel.add(new JLabel("Name: "), gbc);
            gbc.gridx = 1; // Column for input field
            inputPanel.add(nameField, gbc);

            gbc.gridx = 0; // Column for description label
            gbc.gridy = 1; // Row for description
            inputPanel.add(new JLabel("Description: "), gbc);
            gbc.gridx = 1; // Column for description input
            inputPanel.add(descriptionPane, gbc);

            gbc.gridx = 0; // Column for cost label
            gbc.gridy = 2; // Row for cost
            inputPanel.add(new JLabel("Cost: "), gbc);
            gbc.gridx = 1; // Column for cost input
            inputPanel.add(costField, gbc);

            gbc.gridx = 0; // Column for elective label
            gbc.gridy = 3; // Row for elective checkbox
            inputPanel.add(electiveCheck, gbc);

            // Show dialog and get user response
            int option = JOptionPane.showConfirmDialog(
                frame, 
                inputPanel, 
                "Add Procedure", 
                JOptionPane.OK_CANCEL_OPTION
            );
            if (option == JOptionPane.CANCEL_OPTION || 
                option == JOptionPane.CLOSED_OPTION) {
                // Exit if the user cancels
                return; 
            }

            // Get and validate input values
            String name = nameField.getText().trim();
            String description = descriptionArea.getText().trim();
            String costText = costField.getText().trim();
            if (name.isEmpty() || 
                description.isEmpty() || 
                costText.isEmpty()) {
                JOptionPane.showMessageDialog(
                    frame, 
                    "Fields cannot be empty.", 
                    "Input Error", 
                    JOptionPane.ERROR_MESSAGE
                );
            } else {
                try {
                    // Parse cost and get selected hospital
                    double cost = Double.parseDouble(costText);
                    String selectedHospitalName = 
                        (String) hospitalComboBox.getSelectedItem();
                    
                    // Find target hospital from facilities list
                    Hospital targetHospital = (Hospital) healthService
                        .getFacilities()
                        .stream()
                        .filter(f -> f instanceof Hospital && 
                                     f.getName().equals
                                             (selectedHospitalName))
                        .findFirst()
                        .orElse(null);

                    if (targetHospital != null) {
                        // Add new procedure and refresh display
                        targetHospital.addProcedure(new Procedure(
                            name, 
                            description, 
                            electiveCheck.isSelected(), 
                            cost
                        ));
                        // Refresh the table after adding
                        refreshProcedureTable(selectedHospitalName); 
                        // Exit the loop if input is valid
                        break; 
                    } else {
                        JOptionPane.showMessageDialog(
                            frame, 
                            "No hospital found to add the procedure."
                        );
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(
                        frame, 
                        "Invalid cost input."
                    );
                }
            }
        }
    }

    /**
     * Deletes a selected procedure from the selected hospital.
     */
    private void deleteProcedure() {
        // Get the currently selected hospital name from the combo box
        String selectedHospitalName = 
            (String) hospitalComboBox.getSelectedItem();
        if (selectedHospitalName == null) {
            // Show error if no hospital is selected
            JOptionPane.showMessageDialog(
                frame, 
                "No hospital selected.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        /*
         * Find the selected hospital object from
         * the health service facilities
         */
        Hospital selectedHospital =
                (Hospital) healthService.getFacilities()
            .stream()
            .filter(f -> f instanceof Hospital && 
                         f.getName().equals(selectedHospitalName))
            .findFirst()
            .orElse(null);

        if (selectedHospital != null) {
            // Get the ID of the procedure to delete
            int procedureId = selectProcedureId(selectedHospital);
            // Exit if no procedure is selected
            if (procedureId == -1) return; 

            // Remove the procedure and update the UI
            selectedHospital.removeProcedure(procedureId);
            // Refresh the table with the current hospital's procedures
            refreshProcedureTable(selectedHospitalName); 
            JOptionPane.showMessageDialog(frame, "Procedure deleted.");
        } else {
            // Show error if hospital not found
            JOptionPane.showMessageDialog(frame, "Hospital not found.");
        }
    }

    /**
     * Edits the details of a selected procedure.
     */
    private void editProcedure() {
        // Get the currently selected hospital name from the combo box
        String selectedHospitalName = 
            (String) hospitalComboBox.getSelectedItem();
        if (selectedHospitalName == null) {
            JOptionPane.showMessageDialog(
                frame, 
                "No hospital selected.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // Find the selected hospital from the health service facilities
        Hospital selectedHospital =
                (Hospital) healthService.getFacilities()
            .stream()
            .filter(f -> f instanceof Hospital && 
                         f.getName().equals(selectedHospitalName))
            .findFirst()
            .orElse(null);

        if (selectedHospital != null) {
            // Get the ID of the procedure to edit
            int procedureId = selectProcedureId(selectedHospital);
            // Exit if no procedure is selected
            if (procedureId == -1) return; 

            // Find the procedure with the matching ID
            Procedure procedure = selectedHospital.getProcedures().stream()
                .filter(p -> p.getId() == procedureId)
                .findFirst()
                .orElse(null);

            if (procedure != null) {
                // Keep prompting for input until valid or cancelled
                while (true) {
                    /*
                     * Create input fields pre-populated
                     * with current values
                     */
                    JTextField nameField = new JTextField(
                        procedure.getName()
                    );
                    JTextArea descriptionArea = new JTextArea(
                        procedure.getDescription(), 5, 30
                    );
                    descriptionArea.setLineWrap(true);
                    descriptionArea.setWrapStyleWord(true);
                    JScrollPane descriptionPane = new JScrollPane(
                        descriptionArea
                    );
                    JTextField costField = new JTextField(
                        String.valueOf(procedure.getCost())
                    );
                    JCheckBox electiveCheck = new JCheckBox(
                        "Elective", procedure.isElective()
                    );

                    /*
                     * Create and configure the
                     * input panel with GridBagLayout
                     */
                    JPanel inputPanel = new JPanel(new GridBagLayout());
                    GridBagConstraints gbc = new GridBagConstraints();
                    // Add padding
                    gbc.insets = new Insets(5, 5, 5, 5);

                    // Add labels and fields to the input panel
                    gbc.anchor = GridBagConstraints.WEST; // Align Left
                    gbc.gridx = 0; // Column for labels
                    gbc.gridy = 0; // Row for name
                    inputPanel.add(new JLabel("New Name: "), gbc);
                    gbc.gridx = 1; // Column for input field
                    inputPanel.add(nameField, gbc);

                    gbc.gridx = 0; // Column for description label
                    gbc.gridy = 1; // Row for description
                    inputPanel.add(new JLabel("New Description: "), gbc);
                    gbc.gridx = 1; // Column for description input
                    inputPanel.add(descriptionPane, gbc);

                    gbc.gridx = 0; // Column for cost label
                    gbc.gridy = 2; // Row for cost
                    inputPanel.add(new JLabel("New Cost: "), gbc);
                    gbc.gridx = 1; // Column for cost input
                    inputPanel.add(costField, gbc);

                    gbc.gridx = 0; // Column for elective label
                    gbc.gridy = 3; // Row for elective checkbox
                    inputPanel.add(electiveCheck, gbc);

                    // Show dialog and get user response
                    int option = JOptionPane.showConfirmDialog(
                        frame, 
                        inputPanel, 
                        "Edit Procedure", 
                        JOptionPane.OK_CANCEL_OPTION
                    );
                    if (option == JOptionPane.CANCEL_OPTION 
                        || option == JOptionPane.CLOSED_OPTION) {
                        return; // Exit if the user cancels
                    }

                    // Get values from input fields
                    String newName = nameField.getText().trim();
                    String newDescription =
                            descriptionArea.getText().trim();
                    String costText = costField.getText().trim();
                    
                    // Validate input fields are not empty
                    if (newName.isEmpty() || newDescription.isEmpty() 
                        || costText.isEmpty()) {
                        JOptionPane.showMessageDialog(
                            frame, 
                            "Fields cannot be empty.", 
                            "Input Error", 
                            JOptionPane.ERROR_MESSAGE
                        );
                    } else {
                        try {
                            // Update procedure with new values
                            procedure.setName(newName);
                            procedure.setDescription(newDescription);
                            procedure.setElective(
                                electiveCheck.isSelected()
                            );
                            procedure.setCost(
                                Double.parseDouble(costText)
                            );
                            refreshProcedureTable(selectedHospitalName);
                            // Exit the loop if input is valid
                            break; 
                        } catch (NumberFormatException e) {
                            JOptionPane.showMessageDialog(
                                frame, 
                                "Invalid cost input."
                            );
                        }
                    }
                }
            } else {
                JOptionPane.showMessageDialog(
                    frame, 
                    "Procedure not found."
                );
            }
        } else {
            JOptionPane.showMessageDialog(
                frame, 
                "Hospital not found."
            );
        }
    }

    /**
     * Creates and returns the panel for managing visits 
     * and procedures.
     * 
     * @return a JPanel containing the visit and procedure 
     * management interface
     */
    private JPanel createVisitProcedurePanel() {
        // Create main panel with BorderLayout for overall layout
        JPanel panel = new JPanel(new BorderLayout());
        
        // Initialize text area for displaying visit/procedure information
        textArea = new JTextArea();
        // Make text area read-only
        textArea.setEditable(false); 
        
        // Set monospaced font for better readability of structured text
        textArea.setFont(
            // 14pt bold monospace font
            new Font("Monospaced", Font.BOLD, 14) 
        );

        // Add text area to scroll pane and place in center of panel
        panel.add(
            new JScrollPane(textArea), 
            BorderLayout.CENTER
        );

        // Set consistent font size for all buttons
        Font buttonFont = new Font("SansSerif", Font.PLAIN, 14);

        // Create and configure visit button
        // Get pre-configured visit button
        JButton visitButton = getVisitButton(textArea); 
        visitButton.setFont(buttonFont);

        // Create and configure procedure button
        JButton performProcedureButton = 
            new JButton("Perform Procedure");
        performProcedureButton.setFont(buttonFont);
        performProcedureButton.addActionListener(
            _ -> performProcedure()
        );

        // Create button panel with 1 row, 2 columns grid layout
        JPanel buttonPanel = new JPanel(
            // Added 3px horizontal gap
            new GridLayout(1, 2, 3, 0)
        );
        buttonPanel.add(visitButton);
        buttonPanel.add(performProcedureButton);

        // Add button panel to top of main panel
        panel.add(buttonPanel, BorderLayout.NORTH);

        return panel;
    }

    /**
     * Selects a procedure ID from the specified hospital.
     * 
     * @param hospital the hospital from which to select a procedure
     * @return the ID of the selected procedure, or -1 if 
     * no selection is made
     */
    private int selectProcedureId(Hospital hospital) {
        // Get list of procedures from the hospital
        List<Procedure> procedures = hospital.getProcedures();
        
        /*
          Create combo box with procedure names using stream to map 
          Procedure objects to names
         */
        JComboBox<String> procedureComboBox = new JComboBox<>(
            procedures.stream()
                .map(Procedure::getName)
                .toArray(String[]::new)
        );

        // Show dialog with combo box for procedure selection
        int option = JOptionPane.showConfirmDialog(
            frame, 
            procedureComboBox, 
            "Select Procedure", 
            JOptionPane.OK_CANCEL_OPTION
        );
        
        if (option == JOptionPane.OK_OPTION) {
            // Get selected procedure name from combo box
            String selectedName = 
                (String) procedureComboBox.getSelectedItem();
            
            /*
              Find matching procedure object by 
              filtering procedures list using stream 
              and lambda expression
             */
            Procedure procedure = procedures.stream()
                .filter(p -> p.getName().equals(selectedName))
                .findFirst()
                .orElse(null);
            
            if (procedure != null) {
                // Return ID if procedure found
                return procedure.getId(); 
            }
        }
        // Return -1 if cancelled or no selection made
        return -1; 
    }

    /**
     * Performs a selected procedure on a selected patient.
     */
    private void performProcedure() {
        // Get selected patient ID, return if none selected
        int patientId = selectPatientId();
        // Exit if no patient is selected
        if (patientId == -1) return; 

        /*
          Create panel for hospital and procedure selection 
          with 2x2 grid layout
         */
        JPanel inputPanel = new JPanel(new GridLayout(2, 2));

        // Create and add hospital selection components
        // Local to avoid name conflict
        JComboBox<String> localHospitalComboBox = new JComboBox<>(); 
        inputPanel.add(new JLabel("Hospital:"));
        inputPanel.add(localHospitalComboBox);

        /*
          Create procedure selection combo box with 
          custom renderer to display procedure names
         */
        JComboBox<Procedure> procedureListComboBox = 
            new JComboBox<>();
        procedureListComboBox.setRenderer(
            // Custom renderer for procedure display
            new ProcedureListCellRenderer() 
        );
        inputPanel.add(new JLabel("Procedure:"));
        inputPanel.add(procedureListComboBox);

        /*
          Populate hospital dropdown with all hospitals from 
          health service using stream and lambda expression
         */
        healthService.getFacilities().stream()
            .filter(f -> f instanceof Hospital)
            .forEach(f -> 
                localHospitalComboBox.addItem(f.getName())
            );

        /*
          Add listener to update procedures when hospital 
          selection changes
         */
        localHospitalComboBox.addActionListener(
            _ -> updateProcedureList(
                localHospitalComboBox, 
                procedureListComboBox
            )
        );

        /*
          Initialize procedure list with procedures from
          first hospital using updateProcedureList method
         */
        updateProcedureList(
            localHospitalComboBox, 
            procedureListComboBox
        );

        // Show selection dialog and handle user response
        int option = JOptionPane.showConfirmDialog(
            frame, 
            inputPanel, 
            "Select Procedure", 
            JOptionPane.OK_CANCEL_OPTION
        );
        if (option == JOptionPane.CANCEL_OPTION || 
            option == JOptionPane.CLOSED_OPTION) {
            // Exit if user cancels or closes dialog
            return; 
        }

        // Validate procedure selection
        Procedure selectedProcedure = 
            (Procedure) procedureListComboBox.getSelectedItem();
        if (selectedProcedure == null) {
            JOptionPane.showMessageDialog(
                frame, 
                "No procedure selected.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // Find selected patient and hospital objects
        Patient patient = healthService.getPatients().stream()
            .filter(p -> p.getId() == patientId)
            .findFirst()
            .orElse(null);
        
        Hospital hospital =
                (Hospital) healthService.getFacilities().stream()
            .filter(f -> f instanceof Hospital && 
                         f.getName().equals
                                 (localHospitalComboBox.getSelectedItem()))
            .findFirst()
            .orElse(null);

        // Attempt to perform procedure and log result
        if (patient != null && hospital != null) {
            boolean success = hospital.performProcedure
                    (patient, selectedProcedure);
            textArea.append("> Procedure " + selectedProcedure.getName() + 
                (success ? " performed" : " could not be performed") + 
                " on patient " + patient.getName() + ".\n");
        } else {
            textArea.append("> Patient or Hospital not found.\n");
        }
    }

    /**
     * Updates the procedure list in the combo box based on the selected
     * hospital. First clears the existing procedures, then gets the
     * selected hospital name, finds the matching Hospital facility,
     * and populates the procedure combo box with all procedures
     * available at that hospital.
     * 
     * @param localHospitalComboBox the JComboBox containing hospital
     *                              names to select from
     * 
     * @param procedureListComboBox the JComboBox to be populated
     *                              with the selected hospital's
     *                              procedures
     */
    private void updateProcedureList(
        JComboBox<String> localHospitalComboBox, 
        JComboBox<Procedure> procedureListComboBox
    ) {
        // Clear existing procedures
        procedureListComboBox.removeAllItems(); 
        String selectedHospitalName = 
            (String) localHospitalComboBox.getSelectedItem();
        healthService.getFacilities().stream()
            .filter(f -> f instanceof Hospital 
                && f.getName().equals(selectedHospitalName))
            .findFirst()
            .ifPresent(hospital -> 
                ((Hospital) hospital).getProcedures()
                    .forEach(procedureListComboBox::addItem)
            );
    }

    /**
     * Returns a JButton configured for visiting a medical facility.
     * The button opens a dialog allowing users to select a patient 
     * and facility, then records the visit and displays the result.
     * <p> 
     * The visit process includes:
     * - Selecting a patient by ID
     * - Choosing facility type (Hospital or Clinic)
     * - Selecting a specific facility
     * - Recording the visit and displaying appropriate message
     * <p> 
     * For Hospitals: Shows if patient was admitted
     * For Clinics: Shows if patient had first 'visit' or follow-up
     *              consultation
     * 
     * @param textArea the JTextArea where visit results will be displayed
     * @return a configured JButton for facility visits
     */
    private JButton getVisitButton(JTextArea textArea) {
        JButton visitButton = new JButton("Visit Facility");
        visitButton.addActionListener(_ -> {
            // Get patient ID, exit if none selected
            int patientId = selectPatientId();
            if (patientId == -1) return;

            /*
             * Create selection panel with
             * GridLayout for aligned components
             */
            JPanel inputPanel = new JPanel(new GridLayout(2, 2));

            // Add facility type selector
            JComboBox<String> facilityTypeComboBox = new JComboBox<>(
                new String[]{"Hospital", "Clinic"}
            );
            inputPanel.add(new JLabel("Facility Type:"));
            inputPanel.add(facilityTypeComboBox);

            // Add facility name selector
            JComboBox<String> facilityListComboBox = new JComboBox<>();
            inputPanel.add(new JLabel("Facility:"));
            inputPanel.add(facilityListComboBox);

            // Link facility type to facility list updates
            facilityTypeComboBox.addActionListener(
                _ -> updateFacilityList(
                    facilityTypeComboBox, 
                    facilityListComboBox
                )
            );

            // Populate initial facility list
            updateFacilityList(
                facilityTypeComboBox, 
                facilityListComboBox
            );

            // Show dialog and handle cancellation
            int option = JOptionPane.showConfirmDialog(
                frame, 
                inputPanel, 
                "Select Facility", 
                JOptionPane.OK_CANCEL_OPTION
            );
            if (option == JOptionPane.CANCEL_OPTION || 
                option == JOptionPane.CLOSED_OPTION) {
                return;
            }

            // Validate facility selection
            String selectedFacilityName = 
                (String) facilityListComboBox.getSelectedItem();
            if (selectedFacilityName == null) {
                JOptionPane.showMessageDialog(
                    frame, 
                    "No facility selected.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            // Find patient and facility objects
            Patient patient = healthService.getPatients().stream()
                .filter(p -> p.getId() == patientId)
                .findFirst()
                .orElse(null);
                
            MedicalFacility facility =
                    healthService.getFacilities().stream()
                .filter(f -> f.getName().equals(selectedFacilityName))
                .findFirst()
                .orElse(null);
            
            // Process visit if both patient and facility exist
            if (patient != null && facility != null) {
                boolean firstTime = facility.visit(patient);
                
                // Display appropriate message based on facility type
                if (facility instanceof Hospital) {
                    textArea.append("> Patient " + patient.getName() +
                        (firstTime ? " was admitted at " : 
                        " was not admitted at ") + 
                        facility.getName() + "\n");
                } else if (facility instanceof Clinic) {
                    textArea.append("> Patient " + patient.getName() +
                        (firstTime ? " was admitted at " : 
                        " was charged for a consultation at ") + 
                        facility.getName() + "\n");
                }
                
                // Return to Visit & Procedure tab
                tabbedPane.setSelectedIndex(3); 
            } else {
                textArea.append("> Patient or Facility not found.\n");
            }
        });
        return visitButton;
    }

    /**
     * Updates the facility list in the dropdown based on the selected 
     * facility type. This method filters facilities to only show those
     * matching the selected type (either Hospital or Clinic) and
     * populates the facility list combo box with the names of the
     * matching facilities.
     * 
     * @param facilityTypeComboBox the JComboBox containing facility types
     *                             (Hospital/Clinic)
     * 
     * @param facilityListComboBox the JComboBox to be populated with
     *                             filtered facility names
     */
    private void updateFacilityList(
        JComboBox<String> facilityTypeComboBox, 
        JComboBox<String> facilityListComboBox
    ) {
        // Clear existing items from the facility list
        facilityListComboBox.removeAllItems();
        
        // Get the currently selected facility type
        String selectedType = (String) facilityTypeComboBox
            .getSelectedItem();
        
        // Filter facilities by type and add their names to the combo box
        healthService.getFacilities().stream()
            .filter(f ->
                    {
                        assert selectedType != null;
                        return (selectedType.equals("Hospital") && 
                         f instanceof Hospital) || 
                        (selectedType.equals("Clinic") && 
                         f instanceof Clinic);
                    }
            )
            .forEach(f -> 
                facilityListComboBox.addItem(f.getName())
            );
    }

    /**
     * Creates and returns the panel for file operations.
     * This panel contains buttons for saving and loading
     * application data.
     * The buttons are styled with custom icons and labels
     * for better user experience.
     * <p> 
     * Layout:
     * - Uses GridBagLayout for precise component positioning
     * - Save and Load buttons are arranged horizontally
     * - Labels are placed below their respective buttons
     * <p> 
     * Button styling:
     * - Custom icons sized to 70x70 pixels
     * - Transparent background
     * - No borders for modern look
     * - Bold labels with 14pt font
     * 
     * @return a JPanel containing the file operations interface
     * with Save and Load functionality
     */
    private JPanel createFileOperationsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        // Add padding around components
        gbc.insets = new Insets(5, 5, 5, 5); 
        gbc.anchor = GridBagConstraints.CENTER;

        // Load and resize icons for buttons
        ImageIcon saveIcon = resizeIcon(
            new ImageIcon(
                "src/main/resources/Save.png"
            )
        );
        ImageIcon loadIcon = resizeIcon(
            new ImageIcon(
                "src/main/resources/Load.png"
            )
        );

        // Create and configure Save button
        JButton saveButton = new JButton();
        saveButton.setIcon(saveIcon);
        // Set transparent background
        saveButton.setContentAreaFilled(false); 
        // Remove border
        saveButton.setBorderPainted(false); 
        // Remove focus outline
        saveButton.setFocusPainted(false); 
        // Add action listener for save data
        saveButton.addActionListener(_ -> saveData());

        // Create and configure Load button
        JButton loadButton = new JButton();
        loadButton.setIcon(loadIcon);
        // Transparent background
        loadButton.setContentAreaFilled(false); 
        // Remove border
        loadButton.setBorderPainted(false); 
        // Remove focus outline
        loadButton.setFocusPainted(false); 
        loadButton.addActionListener(_ -> loadData());

        // Configure labels with custom font
        Font labelFont = new Font("SansSerif", Font.BOLD, 14);
        JLabel saveLabel = new JLabel("Save");
        saveLabel.setFont(labelFont);
        JLabel loadLabel = new JLabel("Load");
        loadLabel.setFont(labelFont);

        // Add Save button and its label
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(saveButton, gbc);
        gbc.gridy = 1;
        panel.add(saveLabel, gbc);

        // Add Load button and its label
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(loadButton, gbc);
        gbc.gridy = 1;
        panel.add(loadLabel, gbc);

        return panel;
    }

    /**
     * Resizes an icon to the specified dimensions.
     * This method takes an ImageIcon and scales it to the given
     * width and height, ensuring a smooth scaling process.
     *
     * @param icon the ImageIcon to be resized
     * @return a new ImageIcon that has been resized to the
     * specified dimensions
     */
    private ImageIcon resizeIcon(ImageIcon icon) {
        // Retrieve the image from the ImageIcon
        Image img = icon.getImage(); 
        // Scale the image smoothly
        Image resizedImage = img.getScaledInstance(
                70,
                70, 
            Image.SCALE_SMOOTH
        ); 
        // Return the resized ImageIcon
        return new ImageIcon(resizedImage); 
    }

    /**
     * Saves the current health service data to a file.
     * This method opens a file chooser dialog for the user to specify the 
     * location and name of the file where the data will be saved. If the 
     * user approves the selection, the health service data is serialized 
     * and written to the specified file.
     */
    private void saveData() {
        // Create a file chooser for saving files
        JFileChooser fileChooser = new JFileChooser(); 
        
        // Set the dialog title
        fileChooser.setDialogTitle("Specify a file to save"); 
        
        // Show save dialog and capture user selection
        int userSelection = fileChooser.showSaveDialog(frame); 

        // Check if the user approved the selection
        if (userSelection == JFileChooser.APPROVE_OPTION) { 
            // Get the selected file
            File fileToSave = fileChooser.getSelectedFile(); 
            try (ObjectOutputStream oos = new ObjectOutputStream(
                    new FileOutputStream(fileToSave))) {
                // Serialize and save the health service data
                oos.writeObject(healthService); 
                
                // Notify user of success
                JOptionPane.showMessageDialog(frame, 
                    "Data saved successfully to " + 
                    fileToSave.getAbsolutePath()); 
            } catch (IOException e) {
                // Show error message if saving fails
                JOptionPane.showMessageDialog(frame, 
                    "Error saving data: " + e.getMessage());
            }
        }
    }

    /**
     * Loads health service data from a file.
     * This method opens a file chooser dialog for the user to select a 
     * file from which to load the health service data. If the user 
     * approves the selection, the data is deserialized and the relevant 
     * tables are refreshed to reflect the loaded data.
     */
    private void loadData() {
        // Create a file chooser for loading files
        JFileChooser fileChooser = new JFileChooser(); 
        
        // Set the dialog title
        fileChooser.setDialogTitle("Select a file to load"); 
        
        // Show open dialog and capture user selection
        int userSelection = fileChooser.showOpenDialog(frame); 

        // Check if the user approved the selection
        if (userSelection == JFileChooser.APPROVE_OPTION) { 
            // Get the selected file
            File fileToLoad = fileChooser.getSelectedFile(); 
            try (ObjectInputStream ois = new ObjectInputStream(
                    new FileInputStream(fileToLoad))) {
                // Deserialize and load the health service data
                healthService = (HealthService) ois.readObject(); 
                
                // Refresh the patient table to show updated data
                refreshPatientTable(); 
                
                // Refresh the facility table to show updated data
                refreshFacilityTable(); 
                
                /*
                  Use the class member hospitalComboBox directly to 
                  refresh procedure table
                 */
                if (hospitalComboBox.getItemCount() > 0) { 
                    // Check if there are items in the combo box
                    refreshProcedureTable(
                        /*
                          Refresh procedure table based on the first 
                          hospital in the hospital combo box
                         */
                            hospitalComboBox.getItemAt(0)
                    ); 
                }
                
                // Notify user of success
                JOptionPane.showMessageDialog(frame, 
                    "Data loaded successfully from " + 
                    fileToLoad.getAbsolutePath()); 
            } catch (IOException | ClassNotFoundException e) {
                // Show error message if loading fails
                JOptionPane.showMessageDialog(frame, 
                    "Error loading data: " + e.getMessage());
            }
        }
    }

    /**
     * Sets the preferred and maximum widths for columns in a JTable.
     * This method iterates through the provided widths and applies 
     * them to the corresponding columns of the specified JTable.
     * 
     * @param table the JTable whose columns are to be resized
     * @param widths the desired widths for each column
     */
    private void setColumnWidths(JTable table, int... widths) {
        for (int i = 0; i < widths.length; i++) {
            // Set the preferred width for the column
            table.getColumnModel()
                 .getColumn(i)
                 .setPreferredWidth(widths[i]);
            
            // If width is greater than 0, set the maximum width as well
            if (widths[i] > 0) {
                table.getColumnModel()
                     .getColumn(i)
                     .setMaxWidth(widths[i]);
            }
        }
    }

    /**
     * A custom cell renderer for word wrapping in table cells.
     * This renderer allows text to wrap within the cell, improving 
     * readability for long text entries.
     */
    static class WordWrapCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(
                JTable table, 
                Object value,
                boolean isSelected, 
                boolean hasFocus, 
                int row, 
                int column) {
            
            JTextArea textArea = new JTextArea();
            // Enable word wrapping
            textArea.setWrapStyleWord(true); 
            // Enable line wrapping
            textArea.setLineWrap(true); 
            // Make the text area opaque
            textArea.setOpaque(true); 
            // Set the text area content
            textArea.setText((String) value); 
            
            /*
             * Preferred size to control width based on
             * the column width
             */
            textArea.setSize(
                table.getColumnModel().getColumn(column).getWidth(), 
                Short.MAX_VALUE
            );
            // Adjust size to preferred size
            textArea.setSize(textArea.getPreferredSize()); 
            
            /*
             * Set background and foreground colors
             * based on selection state
             */
            if (isSelected) {
                textArea.setBackground(
                    table.getSelectionBackground()
                );
                textArea.setForeground(
                    table.getSelectionForeground()
                );
            } else {
                textArea.setBackground(
                    table.getBackground()
                );
                textArea.setForeground(
                    table.getForeground()
                );
            }

            // Draw the border to simulate grid lines
            textArea.setBorder(
                BorderFactory.createMatteBorder(
                    1, 1, 1, 1, 
                    table.getGridColor()
                )
            );
            
            // Return the customized text area
            return textArea; 
        }
    }

    /**
     * Selects a patient ID from the list of patients.
     * This method displays a dialog with a combo box for the 
     * user to select a patient.
     * 
     * @return the ID of the selected patient, or -1 if no 
     * selection is made
     */
    private int selectPatientId() {
        // Retrieve the list of patients
        List<Patient> patients = healthService.getPatients();
        
        // Create a combo box with patients
        JComboBox<Patient> patientComboBox = new JComboBox<>(
            patients.toArray(Patient[]::new)
        );
        
        // Set custom renderer for the combo box
        patientComboBox.setRenderer(new PatientListCellRenderer());

        // Show dialog
        int option = JOptionPane.showConfirmDialog(
            frame, 
            patientComboBox, 
            "Select Patient", 
            JOptionPane.OK_CANCEL_OPTION
        );
        
        // Check if the user approved the selection
        if (option == JOptionPane.OK_OPTION) {
            // Get the selected patient
            Patient selectedPatient = 
                (Patient) patientComboBox.getSelectedItem();
            if (selectedPatient != null) {
                // Return the selected patient's ID
                return selectedPatient.getId();
            }
        }
        
        // Return -1 if no selection is made
        return -1;
    }

    /**
     * A custom list cell renderer for displaying patients 
     * with icons.
     * This renderer displays each patient with their name and an 
     * icon indicating their privacy status.
     */
    class PatientListCellRenderer extends JPanel 
        implements ListCellRenderer<Patient> {
        // Label for displaying the patient's name
        private final JLabel textLabel; 
        // Label for displaying the privacy icon
        private final JLabel iconLabel; 
        // Icon for private patients
        private final Icon checkedIcon; 
        // Icon for non-private patients
        private final Icon uncheckedIcon; 

        public PatientListCellRenderer() {
            // Set layout for the panel
            setLayout(new BorderLayout()); 
            // Initialize the text label
            textLabel = new JLabel(); 
            // Initialize the icon label
            iconLabel = new JLabel(); 
            
            // Load and process icons for patient privacy status
            checkedIcon = resizeAndRecolorIcon(
                new ImageIcon(
                    "src/main/resources/Checked.png"
                ), 
                16, 
                16, 
                true
            );
            uncheckedIcon = resizeAndRecolorIcon(
                new ImageIcon(
                    "src/main/resources/Unchecked.png"
                ), 
                16, 
                16, 
                true
            );

            // Add text label to the center
            add(textLabel, BorderLayout.CENTER); 
            // Add icon label to the east
            add(iconLabel, BorderLayout.EAST); 
        }

        @Override
        public Component getListCellRendererComponent(
            JList<? extends Patient> list, 
            Patient value, 
            int index, 
            boolean isSelected, 
            boolean cellHasFocus
        ) {
            // Display only the patient's name
            textLabel.setText(value.getName()); 
            // Use checked icon for private patients
            iconLabel.setIcon(
                value.isPrivate() ? checkedIcon : uncheckedIcon
            ); 

            /*
             * Set background and foreground colors
             * based on selection state
             */
            if (isSelected) {
                setBackground(
                    list.getSelectionBackground()
                );
                textLabel.setForeground(
                    list.getSelectionForeground()
                );
            } else {
                setBackground(
                    list.getBackground()
                );
                textLabel.setForeground(
                    list.getForeground()
                );
            }
            // Make the panel opaque
            setOpaque(true); 
            // Return the customized panel
            return this; 
        }
    }

    /**
     * Selects a facility ID from the list of facilities.
     * This method displays a dialog with a combo box for the 
     * user to select a facility.
     * 
     * @return the ID of the selected facility, or -1 if no 
     * selection is made
     */
    private int selectFacilityId() {
        // Retrieve the list of facilities
        List<MedicalFacility> facilities = 
            healthService.getFacilities();
        
        // Create a combo box with facility names
        JComboBox<String> facilityComboBox = new JComboBox<>(
            facilities.stream()
                      .map(MedicalFacility::getName)
                      .toArray(String[]::new)
        );

        // Show dialog
        int option = JOptionPane.showConfirmDialog(
            frame, 
            facilityComboBox, 
            "Select Facility", 
            JOptionPane.OK_CANCEL_OPTION
        );

        // Check if the user approved the selection
        if (option == JOptionPane.OK_OPTION) {
            // Get the selected facility name
            String selectedName = 
                (String) facilityComboBox.getSelectedItem();
            
            // Find the facility by name
            MedicalFacility facility = facilities.stream()
                .filter(f -> f.getName().equals(selectedName))
                .findFirst()
                .orElse(null);
            
            if (facility != null) {
                // Return the selected facility's ID
                return facility.getId();
            }
        }
        // Return -1 if no selection is made
        return -1;
    }

    /**
     * Sets up keyboard shortcuts for the application.
     * This method binds specific key combinations to actions 
     * within the application for improved usability.
     * 
     * @param component the JComponent to which shortcuts are added
     */
    private void setupShortcuts(JComponent component) {
        // Refresh shortcut
        KeyStroke refreshKeyStroke = KeyStroke.getKeyStroke(
            KeyEvent.VK_R, 
            Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()
        );
        // Bind refresh action
        component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(refreshKeyStroke, "refresh");
        component.getActionMap().put("refresh", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Refresh the patient table
                refreshPatientTable();
                // Refresh the facility table
                refreshFacilityTable();
                // Refresh the procedures table for the selected hospital
                if (hospitalComboBox.getItemCount() > 0) {
                    // Refresh procedure table
                    refreshProcedureTable(
                        (String) hospitalComboBox.getSelectedItem()
                    );
                }
                // Notify user of refresh
                JOptionPane.showMessageDialog(
                    frame, 
                    "Data refreshed."
                );
            }
        });

        // Add shortcut
        KeyStroke addKeyStroke = KeyStroke.getKeyStroke(
            KeyEvent.VK_A, 
            Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()
        );
        // Bind add action
        component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(addKeyStroke, "add"); 
        component.getActionMap().put("add", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Determine which tab is selected
                switch (tabbedPane.getSelectedIndex()) { 
                    // Add patient
                    case 0 -> addPatient(); 
                    // Add facility
                    case 1 -> addFacility(); 
                    // Add procedure
                    case 2 -> addProcedure(); 
                }
            }
        });

        // Delete shortcut
        KeyStroke deleteKeyStroke = KeyStroke.getKeyStroke(
            KeyEvent.VK_D, 
            Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()
        );
        // Bind delete action
        component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(deleteKeyStroke, "delete"); 
        component.getActionMap().put("delete", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Determine which tab is selected
                switch (tabbedPane.getSelectedIndex()) { 
                    // Delete patient
                    case 0 -> deletePatient(); 
                    // Delete facility
                    case 1 -> deleteFacility(); 
                    // Delete procedure
                    case 2 -> deleteProcedure(); 
                }
            }
        });

        // Edit shortcut
        KeyStroke editKeyStroke = KeyStroke.getKeyStroke(
            KeyEvent.VK_E, 
            Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()
        );
        // Bind edit action
        component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(editKeyStroke, "edit"); 
        component.getActionMap().put("edit", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Determine which tab is selected
                switch (tabbedPane.getSelectedIndex()) { 
                    // Edit patient
                    case 0 -> editPatient(); 
                    // Edit facility
                    case 1 -> editFacility(); 
                    // Edit procedure
                    case 2 -> editProcedure(); 
                }
            }
        });
    }

    /**
     * Customizes the appearance of a JTable.
     * This method sets various visual properties of the 
     * JTable to enhance its usability and appearance.
     * 
     * @param table the JTable to be customized
     */
    private void customizeTable(JTable table) {
        // Enable grid lines for better visual separation of cells
        table.setShowGrid(true);
        
        // Set grid color to light gray
        table.setGridColor(new Color(200, 200, 200)); 
        
        // Set row height for better readability
        table.setRowHeight(25);

        // Customize table header appearance
        JTableHeader header = table.getTableHeader();
        // Set header font
        header.setFont(new Font("SansSerif", Font.BOLD, 14)); 
        // Darker gray header background
        header.setBackground(new Color(180, 180, 180)); 
        // Black text for header
        header.setForeground(Color.BLACK); 

        // Customize selection colors for improved visibility
        table.setSelectionBackground(
            // Light blue selection background
            new Color(184, 207, 229) 
        );
        // Black text on selection
        table.setSelectionForeground(Color.BLACK); 

        /*
          Set custom renderer for the header to change arrow color 
          for sorting indicators
         */
        header.setDefaultRenderer(
                new CustomHeaderRenderer(
                        table.getTableHeader().getDefaultRenderer()
                )
        );
    }

    /**
     * A custom header renderer for changing the appearance of 
     * table headers.
     * This renderer allows for custom sorting icons to be 
     * displayed in the table headers.
     */
    static class CustomHeaderRenderer extends DefaultTableCellRenderer {
        // Delegate renderer for default behavior
        private final TableCellRenderer delegate; 

        public CustomHeaderRenderer(TableCellRenderer delegate) {
            // Store the delegate renderer
            this.delegate = delegate; 
        }

        @Override
        public Component getTableCellRendererComponent(
            JTable table, 
            Object value, 
            boolean isSelected, 
            boolean hasFocus, 
            int row, 
            int column
        ) {
            // Get default rendering
            Component c = delegate.getTableCellRendererComponent(
                table, 
                value, 
                isSelected, 
                hasFocus, 
                row, 
                column
            ); 
            if (c instanceof JLabel label) {
                // Set text color to black
                label.setForeground(Color.BLACK); 
                // Set custom icon for sorting
                label.setIcon(getIcon(table, column)); 
            }
            // Return the customized component
            return c; 
        }

        private Icon getIcon(JTable table, int column) {
            if (table.getRowSorter() == null) {
                // No sorting available
                return null; 
            }
            // Get current sort keys
            List<? extends RowSorter.SortKey> sortKeys = 
                table.getRowSorter().getSortKeys(); 
            // Check if the column is sorted
            if (!sortKeys.isEmpty() && 
                sortKeys.getFirst().getColumn() == column) { 
                return switch (sortKeys.getFirst().getSortOrder()) {
                    case ASCENDING -> new ArrowIcon(
                            SwingConstants.NORTH,
                            Color.BLACK
                    ); 
                    case DESCENDING -> new ArrowIcon(
                            SwingConstants.SOUTH,
                            Color.BLACK
                    ); 
                    default -> null; 
                };
            }
            // No icon if not sorted
            return null; 
        }
    }

    /**
     * An icon representing an arrow, used for sorting indicators.
     * This class provides a visual representation of sorting 
     * direction in table headers.
     */
    static class ArrowIcon implements Icon {
        // Direction of the arrow (up or down)
        private final int direction; 
        // Color of the arrow
        private final Color color; 

        public ArrowIcon(int direction, Color color) {
            // Set the direction
            this.direction = direction; 
            // Set the color
            this.color = color; 
        }

        @Override
        public void paintIcon(
            Component c, 
            Graphics g, 
            int x, 
            int y
        ) {
            // Set the color for the arrow
            g.setColor(color); 
            // Get the width of the icon
            int size = getIconWidth(); 
            // Calculate the midpoint
            int mid = size / 2; 
            
            // X coordinates for the arrow
            int[] xPoints = {x, x + size, x + mid}; 
            
            // Y coordinates based on direction
            int[] yPoints = direction == SwingConstants.NORTH 
                ? new int[]{y + size, y + size, y} 
                : new int[]{y, y, y + size}; 
            
            // Draw the arrow
            g.fillPolygon(xPoints, yPoints, 3); 
        }

        @Override
        public int getIconWidth() {
            // Width of the icon
            return 8; 
        }

        @Override
        public int getIconHeight() {
            // Height of the icon
            return 8; 
        }
    }

    /**
     * Resizes and optionally recolors an icon.
     * This method allows for the resizing of icons 
     * and can change their color if specified.
     * 
     * @param icon the ImageIcon to be resized and recolored
     * @param width the desired width
     * @param height the desired height
     * @param recolor whether to recolor the icon
     * @return a resized and recolored ImageIcon
     */
    private ImageIcon resizeAndRecolorIcon(
        ImageIcon icon, 
        int width, 
        int height, 
        boolean recolor
    ) {
        // Get the image from the icon
        Image img = icon.getImage(); 
        
        // Check if the image loaded correctly
        if (img.getWidth(null) == -1 || 
            img.getHeight(null) == -1) {
            System.err.println(
                "Error: Image not loaded correctly. " +
                "Please check the file path."
            );
            // Return the original icon if loading fails
            return icon; 
        }

        // Create a buffered image
        BufferedImage bufferedImage = new BufferedImage(
            img.getWidth(null), 
            img.getHeight(null), 
            BufferedImage.TYPE_INT_ARGB
        ); 

        // Draw the image on the buffered image
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.drawImage(img, 0, 0, null);
        g2d.dispose();

        if (recolor) {
            // Iterate over each pixel and set it to black
            for (int y = 0; y < bufferedImage.getHeight(); y++) {
                for (int x = 0; x < bufferedImage.getWidth(); x++) {
                    int rgba = bufferedImage.getRGB(x, y);
                    // Preserve the alpha channel
                    int alpha = (rgba >> 24) & 0xff; 
                    // Only change non-transparent pixels
                    if (alpha != 0) { 
                        // Set to black with preserved alpha
                        bufferedImage.setRGB(
                            x, 
                            y, 
                            (alpha << 24) 
                        ); 
                    }
                }
            }
        }

        // Resize the image
        Image resizedImage = bufferedImage.getScaledInstance(
            width, 
            height, 
            Image.SCALE_SMOOTH
        );
        // Return the resized icon
        return new ImageIcon(resizedImage); 
    }

    /**
     * Displays a popup with tips for using the application.
     * This method provides users with helpful information on 
     * how to navigate and utilize the app effectively.
     */
    private void showTipsPopup() {
        String tips = 
        """
        Tips for Using the App:
        
        - Manage Patients: Add, edit, or delete patient records.
        - Manage Facilities: Add, edit, or delete facilities.
        - Manage Procedures: Add, edit, or delete procedures for hospitals.
        - Visit & Procedure: Record patient visits and perform procedures.
        - File Operations: Save or load data.
        
        Shortcuts:
        - Ctrl/Cmd + A: Add new entry
        - Ctrl/Cmd + D: Delete selected entry
        - Ctrl/Cmd + E: Edit selected entry
        - Ctrl/Cmd + R: Refresh data
        
        Sorting:
        - Click on column headers to sort data.
        - Click again to toggle between ascending and descending order.
        """;

        // Show tips in a dialog
        JOptionPane.showMessageDialog(
            frame, 
            tips, 
            "App Tips", 
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    /**
     * A custom list cell renderer for displaying procedures 
     * with icons.
     * This renderer displays each procedure with its name 
     * and an icon indicating its elective status.
     */
    class ProcedureListCellRenderer 
        extends JPanel 
        implements ListCellRenderer<Procedure> {
        // Label for displaying the procedure's name
        private final JLabel textLabel; 
        // Label for displaying the elective icon
        private final JLabel iconLabel; 
        // Icon for elective procedures
        private final Icon checkedIcon; 
        // Icon for non-elective procedures
        private final Icon uncheckedIcon; 

        public ProcedureListCellRenderer() {
            // Set layout for the panel
            setLayout(new BorderLayout()); 
            // Initialize the text label
            textLabel = new JLabel(); 
            // Initialize the icon label
            iconLabel = new JLabel(); 
            
            // Load and process icons for procedure elective status
            checkedIcon = resizeAndRecolorIcon(
                new ImageIcon(
                    "src/main/resources/Checked.png"
                ), 
                16, 
                16, 
                true
            );
            uncheckedIcon = resizeAndRecolorIcon(
                new ImageIcon(
                    "src/main/resources/Unchecked.png"
                ), 
                16, 
                16, 
                true
            );

            // Add text label to the center
            add(textLabel, BorderLayout.CENTER); 
            // Add icon label to the east
            add(iconLabel, BorderLayout.EAST); 
        }

        @Override
        public Component getListCellRendererComponent(
            JList<? extends Procedure> list, 
            Procedure value, 
            int index, 
            boolean isSelected, 
            boolean cellHasFocus
        ) {
            // Display only the procedure's name
            textLabel.setText(value.getName()); 
            // Use checked icon for elective procedures
            iconLabel.setIcon(
                value.isElective() 
                    ? checkedIcon 
                    : uncheckedIcon
            ); 

            /*
             * Set background and foreground colors
             * based on selection state
             */
            if (isSelected) {
                setBackground(list.getSelectionBackground());
                textLabel.setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                textLabel.setForeground(list.getForeground());
            }
            // Make the panel opaque
            setOpaque(true); 
            // Return the customized panel
            return this; 
        }
    }

    private void refreshHospitalComboBox() {
        // Clear existing items
        hospitalComboBox.removeAllItems();
        healthService.getFacilities().forEach(facility -> {
            if (facility instanceof Hospital) {
                // Add hospital names
                hospitalComboBox.addItem(facility.getName());
            }
        });
    }
}
