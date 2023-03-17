import java.io.*;

public class StudentManager {
    // Attribute
    private Student[] _students;
    private int _capacity;
    private int _count;

    // Constructor
    public StudentManager(int capacity){
        _students = new Student[capacity];
        _capacity = capacity;
        _count = 0;
    }

    // Method

    public void printList() {
        for (int i = 0; i < _count; i++){
            System.out.println(i + 1 + ".");
            System.out.println(_students[i].toString());
        }
    }

    public void addStudent(Student student){
        if (_count < _capacity){
            _students[_count] = student;
            _count++;
        }
        else {
            System.out.println("Maximum capacity reached. Cannot add more students.");
        }
    }

    public void removeStudent(Student student){
        int index = -1;

        for (int i = 0; i < _count; i++){
            if (_students[i].ID().equals(student.ID())) {
                index = i;
                break;
            }
        }

        if (index >= 0){
            // Shifting
            for (int i = index; i < _count - 1; i++){
                _students[i] = _students[i+1];
            }

            // Remove the last slot
            _students[_count - 1] = null;
            _count--;
        }
        else{
            System.out.println("Student not found");
        }
    }

    public Student findStudent(String id){
        for (int i = 0; i < _count; i++){
            if (_students[i].ID().equals(id)) {
                return _students[i];
            }
        }
        return null;
    }

    public void editStudent(String id, String newName, float newScore, String newImage, String newAddress, String newNote) {
        Student studentToEdit = findStudent(id);
        if (studentToEdit != null) {
            studentToEdit.setName(newName);
            studentToEdit.setScore(newScore);
            studentToEdit.setImage(newImage);
            studentToEdit.setAddress(newAddress);
            studentToEdit.setNote(newNote);
        } 
    }

    public Student[] getStudentsList(){return _students;}

    public int getAmountStudent() {return _count;}

    public int Capacity() {return _capacity;}

    public void setCapacity(int capacity) {
        if (this._capacity < capacity){
            this._capacity = capacity;
        }
        else{
            System.out.println("Invalid capacity.");
        }
    }

    // Read & Write File

    // Bin File

    public void writeToFile() throws IOException{
        FileOutputStream fos = new FileOutputStream("students.bin");
        DataOutputStream dos = new DataOutputStream(fos);

        for (int i = 0; i < _count; i++) {
            dos.writeUTF(_students[i].ID());
            dos.writeUTF(_students[i].Name());
            dos.writeFloat(_students[i].Score());
            dos.writeUTF(_students[i].Image());
            dos.writeUTF(_students[i].Address());
            dos.writeUTF(_students[i].Note());
        }

        dos.close();
    }

    public void loadFromFile(String filename) throws IOException {
        FileInputStream fis = new FileInputStream(filename);
        DataInputStream dis = new DataInputStream(fis);
    
        // Clear existing student data
        _count = 0;
        _students = new Student[_capacity];
    
        // Read data from the file and create new student objects
        while (dis.available() > 0) {
            String id = dis.readUTF();
            String name = dis.readUTF();
            float score = dis.readFloat();
            String image = dis.readUTF();
            String address = dis.readUTF();
            String note = dis.readUTF();
    
            Student s = new Student(id, name, score, image, address, note);
            _students[_count] = s;
            _count++;
        }
    
        dis.close();
    }

    // CSV File

    public void loadFromCSVFile(String FilePath) {
        BufferedReader reader = null;
        
        // Clear existing student data
        _count = 0;
        _students = new Student[_capacity];

        try{
            reader = new BufferedReader(new FileReader(FilePath));
            
            // Skip the heading
            reader.readLine();

            String line = "";
            while ((line = reader.readLine()) != null){
                String[] data = line.split(",");
                String id = data[0].trim();
                String name = data[1].trim();
                String image = data[2].trim();
                float score = Float.parseFloat(data[3].trim());
                String address = data[4].trim();
                String note = data[5].trim();
                Student student = new Student(id, name, score, image, address, note);
                addStudent(student);
            }
            reader.close();
        }catch (IOException e) {
            System.out.println("Error loading data from CSV file: " + e.getMessage());
        }catch (NumberFormatException e) {
            System.out.println("Invalid data format in CSV file: " + e.getMessage());
        }
    }
}
