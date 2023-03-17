import java.io.*;

public class Student {
    // Attribute
    private String _id;
    private String _name;
    private float _score;
    private String _image;
    private String _address;
    private String _note;

    // Constructor
    public Student(){
        this._id = null;
        this._name = null;
        this._score = 0;
        this._image = null;
        this._address = null;
        this._note = null;
    }

    public Student(String id, String name, float score, String image, String address, String note) {
        this._id = id;
        this._name = name;
        this._score = score;
        this._image = image;
        this._address = address;
        this._note = note;
    }

    // Getter & Setter
    public String ID() {return _id;}
    public String Name() {return _name;}
    public Float Score() {return _score;}
    public String Image() {return _image;}
    public String Address() {return _address;}
    public String Note() {return _note;}

    public void setID(String id) {this._id = id;}
    public void setName(String name) {this._name = name;}
    public void setScore(Float score) {this._score = score;}
    public void setImage(String image) {this._image = image;}
    public void setAddress(String address) {this._address = address;}
    public void setNote(String note) {this._note = note;}

    // Method
    public void getInput() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));

        System.out.print("Enter student ID: ");
        String id = br.readLine();
        this.setID(id);

        System.out.print("Enter student name: ");
        String name = br.readLine();
        this.setName(name);

        System.out.print("Enter student score: ");
        float score = Float.parseFloat(br.readLine());
        this.setScore(score);

        System.out.print("Enter student image URL: ");
        String image = br.readLine();
        this.setImage(image);

        System.out.print("Enter student address: ");
        String address = br.readLine();
        this.setAddress(address);

        System.out.print("Enter student note: ");
        String note = br.readLine();
        this.setNote(note);
    }

    public String toString() {
        return "ID: " + this.ID() + "\n" +
               "Name: " + this.Name() + "\n" +
               "Score: " + this.Score() + "\n" +
               "Image: " + this.Image() + "\n" +
               "Address: " + this.Address() + "\n" +
               "Note: " + this.Note();
    }
}
