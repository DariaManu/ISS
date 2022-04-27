import java.io.Serializable;

public class Librarian implements Identifiable<Integer>, Serializable {
    private Integer ID;
    private String userName;
    private String password;

    public Librarian(){
        this.ID = 0;
        this.userName = "";
        this.password = "";
    }

    public Librarian(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public Librarian(Integer ID, String userName, String password) {
        this.ID = ID;
        this.userName = userName;
        this.password = password;
    }

    @Override
    public Integer getID() {
        return ID;
    }

    @Override
    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Librarian{" +
                "ID=" + ID +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
