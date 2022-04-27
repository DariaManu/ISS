import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class TestRun {
    public static void main(String[] args) {
        LibraryUserHibernateRepository libraryUserRepo = new LibraryUserHibernateRepository();
        BookHibernateRepository bookRepo = new BookHibernateRepository();
        BorrowHibernateRepository borrowRepo = new BorrowHibernateRepository();
        LibrarianHibernateRepository librarianRepo = new LibrarianHibernateRepository();
        //Librarian librarian = librarianRepo.findByUserNameAndPassword("name.library", "pass1");
        String userName = "name.library";
        System.out.println(userName.matches("^\\w+\\.library$"));
    }
}
