package JavaApp;

public class UserData {
    private String name;
    private String surname;
    private String occupation;
    private String position;
    private String phone;

    // Constructors, getters, and setters

    // Constructor
    public UserData(String name, String surname, String occupation, String position, String phone) {
        this.name = name;
        this.surname = surname;
        this.occupation = occupation;
        this.position = position;
        this.phone = phone;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
