public class DTOUtils {
    public static FilterBooksDTO getDTO(String genre, String author, String publishYear) {
        return new FilterBooksDTO(genre, author, publishYear);
    }
}
