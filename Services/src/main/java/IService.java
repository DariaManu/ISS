import java.util.List;

public interface IService {
    LibraryUser loginLibraryUser(LibraryUser libraryUser, IObserver client) throws Exception;
    void logoutLibraryUser(LibraryUser libraryUser) throws Exception;
    LibraryUser signUpLibraryUser(LibraryUser libraryUser, IObserver client) throws Exception;
    List<Book> getAvailableBooks() throws Exception;
    List<Book> getBooksBorrowedByLibraryUser(Integer libraryUserId) throws Exception;
    void borrowBook(Borrow borrow) throws Exception;
}