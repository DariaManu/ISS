import java.io.Serializable;

public class Book implements Identifiable<Integer>, Serializable {
    private Integer ID;
    private String ISBN;
    private String title;
    private String author;
    private String genre;
    private Integer publishYear;
    private Status status;

    public Book(Integer ID, String ISBN, String title, String author, String genre, Integer publishYear, Status status) {
        this.ID = ID;
        this.ISBN = ISBN;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.publishYear = publishYear;
        this.status = status;
    }

    public Book() {
        this.ID = 0;
        this.ISBN = "";
        this.title = "";
        this.author = "";
        this.genre = "";
        this.publishYear = 0;
        this.status = null;
    }

    public Book(String ISBN, String title, String author, String genre, Integer publishYear, Status status) {
        this.ISBN = ISBN;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.publishYear = publishYear;
        this.status = status;
    }

    @Override
    public Integer getID() {
        return ID;
    }

    @Override
    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Integer getPublishYear() {
        return publishYear;
    }

    public void setPublishYear(Integer publishYear) {
        this.publishYear = publishYear;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Book{" +
                "ID=" + ID +
                ", ISBN='" + ISBN + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", genre='" + genre + '\'' +
                ", publishYear=" + publishYear +
                ", status=" + status +
                '}';
    }
}
