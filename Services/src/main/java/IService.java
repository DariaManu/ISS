import java.util.List;

public interface IService {
    LibraryUser loginLibraryUser(LibraryUser libraryUser, IObserver client) throws Exception;
    void logoutLibraryUser(LibraryUser libraryUser) throws Exception;
    LibraryUser signUpLibraryUser(LibraryUser libraryUser, IObserver client) throws Exception;
    List<Book> getAvailableBooks() throws Exception;
    List<Book> getBooksBorrowedByLibraryUser(Integer libraryUserId) throws Exception;
    void borrowBook(Borrow borrow) throws Exception;
    Librarian loginLibrarian(Librarian librarian) throws Exception;
    List<Book> searchLibraryUserAndGetBooks(String email) throws Exception;
    void returnBook(Book book) throws Exception;
    void addBook(Book book) throws Exception;
    List<Book> getAllBooks() throws Exception;
    List<Book> searchBookByTitle(String title) throws Exception;
    List<Book> filterBooks(String genre, String author, String publishYear) throws Exception;
    Book recommendBook() throws Exception;
}
