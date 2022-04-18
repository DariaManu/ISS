import java.io.Serializable;
import java.time.LocalDate;

public class Borrow implements Identifiable<Integer>, Serializable {
    private Integer ID;
    private Book book;
    private LibraryUser libraryUser;
    private LocalDate date;

    public Borrow(Integer ID, Book book, LibraryUser libraryUser, LocalDate date) {
        this.ID = ID;
        this.book = book;
        this.libraryUser = libraryUser;
        this.date = date;
    }

    public Borrow(Book book, LibraryUser libraryUser, LocalDate date) {
        this.book = book;
        this.libraryUser = libraryUser;
        this.date = date;
    }

    @Override
    public Integer getID() {
        return ID;
    }

    @Override
    public void setID(Integer ID) {
    this.ID = ID;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public LibraryUser getLibraryUser() {
        return libraryUser;
    }

    public void setLibraryUser(LibraryUser libraryUser) {
        this.libraryUser = libraryUser;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Borrow{" +
                "ID=" + ID +
                ", book=" + book +
                ", libraryUser=" + libraryUser +
                ", date=" + date +
                '}';
    }
}
