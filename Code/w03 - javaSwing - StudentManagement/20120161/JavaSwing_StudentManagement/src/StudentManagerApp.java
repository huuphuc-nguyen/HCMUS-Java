import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Comparator;

public class StudentManagerApp extends JPanel implements ActionListener, ListSelectionListener {
    private JTextField idTextField;
    private JTextField nameTextField;
    private JTextField imageTextField;
    private JTextField scoreTextField;
    private JTextField addressTextField;
    private JTextField noteTextField;
    private JButton addButton;
    private JButton editButton;
    private JButton removeButton;
    private JButton exportButton;
    private JButton importButton;
    private JButton clearButton;
    private JTable studentTable;
    private DefaultTableModel tableModel;
    private StudentManager studentManager;

    // Layout; Design

    public StudentManagerApp() throws IOException {
        super();
        setPreferredSize(new Dimension(1300,600));
        // Set layout for contain panel
        setLayout(new BorderLayout());

        // Add input fields panel
        JPanel inputFieldsPanel = createInputFieldsPanel();
        add(inputFieldsPanel, BorderLayout.NORTH);

        // Add the buttons panel to the content pane
        JPanel buttonsPanel = createButtonsPanel();
        add(buttonsPanel, BorderLayout.SOUTH);

        // Add table Panel
        JPanel tablePanel = createTablePanel();
        add(tablePanel, BorderLayout.CENTER );

        //students = new ArrayList<>();
        studentManager = new StudentManager(1000);
        studentManager.loadFromFile("students.bin");
        updateStudentTable();
    }

    public JPanel createInputFieldsPanel(){
        JPanel inputFieldsPanel = new JPanel();

        inputFieldsPanel.setLayout(new GridLayout(0,2));
        inputFieldsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inputFieldsPanel.add(new JLabel("ID:"));
        idTextField = new JTextField();
        inputFieldsPanel.add(idTextField);

        inputFieldsPanel.add(new JLabel("Name:"));
        nameTextField = new JTextField();
        inputFieldsPanel.add(nameTextField);

        inputFieldsPanel.add(new JLabel("Image:"));
        imageTextField = new JTextField();
        inputFieldsPanel.add(imageTextField);

        inputFieldsPanel.add(new JLabel("Score:"));
        scoreTextField = new JTextField();
        inputFieldsPanel.add(scoreTextField);

        inputFieldsPanel.add(new JLabel("Address:"));
        addressTextField = new JTextField();
        inputFieldsPanel.add(addressTextField);

        inputFieldsPanel.add(new JLabel("Note:"));
        noteTextField = new JTextField();
        inputFieldsPanel.add(noteTextField);

        return inputFieldsPanel;
    }

    public JPanel createButtonsPanel(){
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout());
        
        JPanel buttonsPanel2 = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));

        clearButton = new JButton("Clear Input");
        clearButton.addActionListener(this);
        buttonsPanel2.add(clearButton);
        buttonsPanel2.add(Box.createRigidArea(new Dimension(20,0)));

        addButton = new JButton("Add Student");
        addButton.addActionListener(this);
        buttonsPanel2.add(addButton);
        buttonsPanel2.add(Box.createRigidArea(new Dimension(20,0)));

        removeButton = new JButton("Remove Student");
        removeButton.addActionListener(this);
        buttonsPanel2.add(removeButton);
        buttonsPanel2.add(Box.createRigidArea(new Dimension(20,0)));

        editButton = new JButton("Edit Student");
        editButton.addActionListener(this);
        buttonsPanel2.add(editButton);
        buttonsPanel2.add(Box.createRigidArea(new Dimension(20,0)));

        importButton = new JButton("Import CSV");
        importButton.addActionListener(this);
        buttonsPanel2.add(importButton);
        buttonsPanel2.add(Box.createRigidArea(new Dimension(20,0)));

        exportButton = new JButton("Export CSV");
        exportButton.addActionListener(this);
        buttonsPanel2.add(exportButton);

        buttonsPanel.add(buttonsPanel2);

        return buttonsPanel;
    }

    private JPanel createTablePanel() {
        // Create the table panel
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        
        tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Name");
        tableModel.addColumn("Image");
        tableModel.addColumn("Score");
        tableModel.addColumn("Address");
        tableModel.addColumn("Note");

        studentTable = new JTable(tableModel);
        studentTable.getSelectionModel().addListSelectionListener(this);
        studentTable.setRowHeight(30);

        // Add sort function
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        studentTable.setRowSorter(sorter);
        sorter.setSortable(0, true); // ID column
        sorter.setComparator(0, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return Integer.compare(Integer.parseInt(s1), Integer.parseInt(s2));
            }
        });
        
        sorter.setSortable(1, false);
        sorter.setSortable(2, false);
        sorter.setSortable(3, true); // Score column

        sorter.setComparator(3, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return Float.compare(Float.parseFloat(s1), Float.parseFloat(s2));
            }
        });

        sorter.setSortable(4, false);
        sorter.setSortable(5, false);

        JScrollPane scrollPane = new JScrollPane(studentTable);

        tablePanel.add(scrollPane, BorderLayout.CENTER);

        return tablePanel;
    }

    private void clearInputFields() {
        idTextField.setText("");
        nameTextField.setText("");
        imageTextField.setText("");
        scoreTextField.setText("");
        addressTextField.setText("");
        noteTextField.setText("");
    }

    private void addToBinFile() throws IOException{
        studentManager.writeToFile();
    }

    private void updateStudentTable() {
        tableModel.setRowCount(0);
        Student[] students = studentManager.getStudentsList();
        for (int i = 0; i < studentManager.getAmountStudent(); i++) {
            Object[] rowData = {students[i].ID(), students[i].Name(), students[i].Image(), students[i].Score(),
                    students[i].Address(), students[i].Note()};
            tableModel.addRow(rowData);
        }
    }

    // Component; Handle events

    // Table; select row event

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) {
            return;
        }

        int index = studentTable.getSelectedRow();
        if (index != -1) {
            String id = (String) tableModel.getValueAt(index, 0);
                String name = (String) tableModel.getValueAt(index, 1);
                String image = (String) tableModel.getValueAt(index, 2);
                String address = (String) tableModel.getValueAt(index, 4);
                String note = (String) tableModel.getValueAt(index, 5);
                float score = (float) tableModel.getValueAt(index, 3);

                idTextField.setText(id);
                nameTextField.setText(name);
                imageTextField.setText(image);
                addressTextField.setText(address);
                noteTextField.setText(note);
                scoreTextField.setText(Float.toString(score));
        }
    }

    // Button; clicking event

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton){
            if (studentTable.getSelectedRow() == -1) {
                String id = idTextField.getText();
                String name =  nameTextField.getText();
                String image =  imageTextField.getText();
                String address = addressTextField.getText();
                String note = noteTextField.getText();
                float score = 0;

                if (id.isEmpty() || name.isEmpty() || image.isEmpty() || address.isEmpty() || note.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                }
                else {
                    try{
                        score = Float.parseFloat(scoreTextField.getText());
    
                        Student student = new Student(id, name, score, image, address, note);
                        studentManager.addStudent(student);
    
                        updateStudentTable();
    
                        try {
                            addToBinFile();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
    
                        clearInputFields();
                    } catch (NumberFormatException err) {
                        JOptionPane.showMessageDialog(null, "Wrong Input Format", "Error", JOptionPane.ERROR_MESSAGE);
                        scoreTextField.setText("");
                        scoreTextField.requestFocus();
                    }
                }
            }
            else{
                JOptionPane.showMessageDialog(null, "Student existed", "Error", JOptionPane.ERROR_MESSAGE);
            }

             
        } // btn Add clicked

        if (e.getSource() == removeButton){
            int index = studentTable.getSelectedRow();
            
            if (index != -1){
                // Get value of ID
                String selectedID = (String) tableModel.getValueAt(index, 0);
                
                // Find student in array
                Student selectedStudent = studentManager.findStudent(selectedID);

                // Remove student
                studentManager.removeStudent(selectedStudent);

                // Update UI
                updateStudentTable();
                clearInputFields();

                // Write to file
                try {
                    studentManager.writeToFile();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        } // btn Remove Clicked

        if (e.getSource() == editButton){
            int index = studentTable.getSelectedRow();

            if (index != -1){
                // Get old ID to search, in case that ID is editted
                String oldID = (String) tableModel.getValueAt(index, 0);

                String newID = idTextField.getText();
                String newName =  nameTextField.getText();
                String newImage =  imageTextField.getText();
                String newAddress = addressTextField.getText();
                String newNote = noteTextField.getText();
                float newScore = 0;

                if (newID.isEmpty() || newName.isEmpty() || newImage.isEmpty() || newAddress.isEmpty() || newNote.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                }
                else {
                    try{
                        newScore = Float.parseFloat(scoreTextField.getText());
    
                        Student studentToEdit = studentManager.findStudent(oldID);
    
                        studentToEdit.setID(newID);
                        studentToEdit.setName(newName);
                        studentToEdit.setScore(newScore);
                        studentToEdit.setImage(newImage);
                        studentToEdit.setAddress(newAddress);
                        studentToEdit.setNote(newNote);
    
                        updateStudentTable();
    
                        try {
                            addToBinFile();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
    
                        clearInputFields();
                        
                    } catch (NumberFormatException err) {
                        JOptionPane.showMessageDialog(null, "Wrong Input Format", "Error", JOptionPane.ERROR_MESSAGE);
                        scoreTextField.setText("");
                        scoreTextField.requestFocus();
                    }
                }
            }
        } // btn Edit Cliked
        
        if (e.getSource() == clearButton){
            studentTable.clearSelection();
            clearInputFields();
        }

        if (e.getSource() == exportButton) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Export Table as CSV");

            int userSelection = fileChooser.showSaveDialog(StudentManagerApp.this);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                try {
                    FileWriter writer = new FileWriter(fileToSave + ".csv");
                    for (int i = 0; i < tableModel.getColumnCount(); i++) {
                        writer.write(tableModel.getColumnName(i) + ",");
                    }
                    writer.write("\n");
                    for (int i = 0; i < tableModel.getRowCount(); i++) {
                        for (int j = 0; j < tableModel.getColumnCount(); j++) {
                            writer.write(tableModel.getValueAt(i, j) + ",");
                        }
                        writer.write("\n");
                    }
                    writer.close();
                    JOptionPane.showMessageDialog(StudentManagerApp.this, "Table exported successfully to CSV file");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(StudentManagerApp.this, "Error exporting table to CSV file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        if (e.getSource() == importButton){
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Choose CSV file to import");

            FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV Files", "csv");
            fileChooser.setFileFilter(filter);

            int userSelection = fileChooser.showOpenDialog(StudentManagerApp.this);

            if (userSelection == JFileChooser.APPROVE_OPTION){
                File fileToImport = fileChooser.getSelectedFile();
                studentManager.loadFromCSVFile(fileToImport.getAbsolutePath());
                updateStudentTable();
                try {
                    addToBinFile();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        }
    }

    // App; Show GUi

    private static void createAndShowGUI() throws IOException{
        // 1. Set tương tác
        JFrame.setDefaultLookAndFeelDecorated(true);

        JFrame frame = new JFrame("Student Manager");

        // 2. Set exit
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 3. Tạo Content Panel cho Frame
        StudentManagerApp newContentPane = new StudentManagerApp();

        // 4. Set Content Panel vừa tạo cho Frame
        newContentPane.setOpaque(true);
        frame.setContentPane(newContentPane);

        // 5. Show frame
        frame.pack();
        frame.setVisible(true);
    }
    public static void main(String[] args) throws Exception {
        createAndShowGUI();
    }

    

    
}
