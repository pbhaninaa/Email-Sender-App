package JavaApp;

public class UserData {

    private String name;
    private String surname;
    private String occupation;
    private String position;
    private String phone;
    private String email;

    // ✅ No-args constructor
    public UserData() {
    }

    // ✅ All-args constructor
    public UserData(String name, String surname, String occupation,
                    String position, String phone, String email) {
        this.name = name;
        this.surname = surname;
        this.occupation = occupation;
        this.position = position;
        this.phone = phone;
        this.email = email;
    }

    // ✅ Getters & Setters
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
