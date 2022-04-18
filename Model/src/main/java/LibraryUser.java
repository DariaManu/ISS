import java.io.Serializable;

public class LibraryUser implements Identifiable<Integer>, Serializable {
    private Integer ID;
    private String cnp;
    private String name;
    private String address;
    private String phone;
    private String email;

    public LibraryUser(Integer ID, String cnp, String name, String address, String phone, String email) {
        this.ID = ID;
        this.cnp = cnp;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
    }

    public LibraryUser() {
        this.ID = 0;
        this.cnp = "";
        this.name = "";
        this.address = "";
        this.phone = "";
        this.email = "";
    }

    public LibraryUser(String cnp, String name, String address, String phone, String email) {
        this.cnp = cnp;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
    }

    public LibraryUser(String email, String cnp) {
        this.email = email;
        this.cnp = cnp;
    }

    @Override
    public Integer getID() {
        return ID;
    }

    @Override
    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getCnp() {
        return cnp;
    }

    public void setCnp(String cnp) {
        this.cnp = cnp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    @Override
    public String toString() {
        return "LibraryUser{" +
                "ID=" + ID +
                ", cnp='" + cnp + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
