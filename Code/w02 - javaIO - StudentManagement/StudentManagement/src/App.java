public class App {
    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        StudentManager sm = new StudentManager(5);
        Student s1 = new Student("1", "John Doe", 8.5f, "http://example.com/image1.jpg", "123 Main St", "Good student");
        Student s2 = new Student("2", "Jane Smith", 7.5f, "http://example.com/image2.jpg", "456 Oak St", "Average student");
        sm.addStudent(s1);
        sm.addStudent(s2);
        sm.writeToFile();

        //sm.loadFromFile("students.bin");
        
        sm.printList();
    }
}
