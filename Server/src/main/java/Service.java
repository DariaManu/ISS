import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Service implements IService {
    private IBookRepository bookRepo;
    private ILibraryUserRepository libraryUserRepo;
    private IBorrowRepository borrowRepo;
    private Map<String, IObserver> loggedClients;

    private final int defaultThreadsNo = 5;

    public Service(IBookRepository bookRepo, ILibraryUserRepository libraryUserRepo, IBorrowRepository borrowRepo) {
        this.bookRepo = bookRepo;
        this.libraryUserRepo = libraryUserRepo;
        this.borrowRepo = borrowRepo;
        loggedClients = new ConcurrentHashMap<>();
    }

    @Override
    public synchronized LibraryUser loginLibraryUser(LibraryUser libraryUser, IObserver client) throws Exception {
        LibraryUser searchedLibraryUser = libraryUserRepo.findByEmailAndCNP(libraryUser.getEmail(), libraryUser.getCnp());
        if(searchedLibraryUser != null) {
            if(loggedClients.get(libraryUser.getEmail()) != null) {
                throw new Exception("User already logged in!");
            }
            loggedClients.put(libraryUser.getEmail(), client);
        }
        else
            throw new Exception("Incorrect credentials!");
        return searchedLibraryUser;
    }

    @Override
    public void logoutLibraryUser(LibraryUser libraryUser) throws Exception {
        IObserver localClient = loggedClients.remove(libraryUser.getEmail());
        if (localClient == null)
            throw new Exception("User " + libraryUser.getEmail() + " is not logged in!");
    }

    @Override
    public LibraryUser signUpLibraryUser(LibraryUser libraryUser, IObserver client) throws Exception {
        LibraryUser searchedLibraryUser = libraryUserRepo.findByEmailAndCNP(libraryUser.getEmail(), libraryUser.getCnp());
        if(searchedLibraryUser != null) {
            throw new Exception("Invalid cnp and email!");
        }
        libraryUserRepo.add(libraryUser);
        loggedClients.put(libraryUser.getEmail(), client);
        return libraryUserRepo.findByEmailAndCNP(libraryUser.getEmail(), libraryUser.getCnp());
    }

    @Override
    public List<Book> getAvailableBooks() throws Exception {
        return bookRepo.getAvailableBooks();
    }

    @Override
    public List<Book> getBooksBorrowedByLibraryUser(Integer libraryUserId) throws Exception {
        List<Book> borrowedBooks = new ArrayList<>();
        for(Borrow borrow: borrowRepo.getBorrowsByLibraryUserId(libraryUserId)) {
            borrowedBooks.add(borrow.getBook());
        }
        return borrowedBooks;
    }

    @Override
    public void borrowBook(Borrow borrow) throws Exception {
        borrowRepo.add(borrow);
        Book book = borrow.getBook();
        book.setStatus(Status.BORROWED);
        bookRepo.update(book, book.getID());
        notifyBookWasBorrowed();
    }

    private void notifyBookWasBorrowed() {
        List<LibraryUser> libraryUsers = libraryUserRepo.getAll().stream().toList();
        List<Book> availableBooks = bookRepo.getAvailableBooks();
        ExecutorService executor = Executors.newFixedThreadPool(defaultThreadsNo);
        for(LibraryUser libraryUser: libraryUsers) {
            IObserver client = loggedClients.get(libraryUser.getEmail());
            if(client != null) {
                executor.execute(() -> {
                    try {
                        System.out.println("Notifying [" + libraryUser.getEmail() + "] that a book was borrowed.");
                        client.bookWasBorrowed(availableBooks);
                    } catch (Exception exception) {
                        System.out.println("Error notifying library users " + exception);
                    }
                });
            }
        }
        executor.shutdown();
    }

}
