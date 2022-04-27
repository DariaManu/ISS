import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class TestRun {
    public static void main(String[] args) {
        LibraryUserHibernateRepository libraryUserRepo = new LibraryUserHibernateRepository();
        BookHibernateRepository bookRepo = new BookHibernateRepository();
        BorrowHibernateRepository borrowRepo = new BorrowHibernateRepository();
        LibraryUser libraryUser = new LibraryUser(3, "6010410125777", "test", "adresa", "0755083455", "ceva@email.com");
        Book book = new Book(10, "isbn", "titlu", "autor", "gen", 2022, Status.AVAILABLE);

        Borrow borrow = new Borrow(book, libraryUser, new Date());

        borrowRepo.add(borrow);
        System.out.println("here");
    }
}
