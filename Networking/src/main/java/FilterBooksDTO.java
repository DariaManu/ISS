import java.io.Serializable;

public class FilterBooksDTO implements Serializable {
    private String genre;
    private String author;
    private String publishYear;

    public FilterBooksDTO(String genre, String author, String publishYear) {
        this.genre = genre;
        this.author = author;
        this.publishYear = publishYear;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublishYear() {
        return publishYear;
    }

    public void setPublishYear(String publishYear) {
        this.publishYear = publishYear;
    }

    @Override
    public String toString() {
        return "FilterBooksDTO{" +
                "genre='" + genre + '\'' +
                ", author='" + author + '\'' +
                ", publishYear='" + publishYear + '\'' +
                '}';
    }
}
