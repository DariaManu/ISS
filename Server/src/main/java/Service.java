import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Service implements IService {
    private IBookRepository bookRepo;
    private ILibraryUserRepository libraryUserRepo;
    private IBorrowRepository borrowRepo;
    private ILibrarianRepository librarianRepo;
    private Map<String, IObserver> loggedClients;

    private final int defaultThreadsNo = 5;

    public Service(IBookRepository bookRepo, ILibraryUserRepository libraryUserRepo, IBorrowRepository borrowRepo) {
        this.bookRepo = bookRepo;
        this.libraryUserRepo = libraryUserRepo;
        this.borrowRepo = borrowRepo;
        loggedClients = new ConcurrentHashMap<>();
    }

    public Service(IBookRepository bookRepo, ILibraryUserRepository libraryUserRepo, IBorrowRepository borrowRepo, ILibrarianRepository librarianRepo) {
        this.bookRepo = bookRepo;
        this.libraryUserRepo = libraryUserRepo;
        this.borrowRepo = borrowRepo;
        this.librarianRepo = librarianRepo;
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

    @Override
    public Librarian loginLibrarian(Librarian librarian) throws Exception {
        Librarian searchedLibrarian = librarianRepo.findByUserNameAndPassword(librarian.getUserName(), librarian.getPassword());
        if (searchedLibrarian == null) {
            throw new Exception("Incorrect credentials!");
        }
        return searchedLibrarian;
    }

    @Override
    public List<Book> searchLibraryUserAndGetBooks(String email) throws Exception {
        LibraryUser libraryUser = libraryUserRepo.findByEmail(email);
        if (libraryUser != null) {
            List<Book> booksBorrowed = getBooksBorrowedByLibraryUser(libraryUser.getID());
            return booksBorrowed;
        } else {
            throw new Exception("Incorrect credentials");
        }
    }

    @Override
    public void returnBook(Book book) throws Exception {
        borrowRepo.deleteByBookId(book.getID());
        book.setStatus(Status.AVAILABLE);
        bookRepo.update(book, book.getID());
        notifyBookWasReturned();
    }

    @Override
    public void addBook(Book book) throws Exception {
        bookRepo.add(book);
        notifyBookWasReturned();
    }

    @Override
    public List<Book> getAllBooks() throws Exception {
        return bookRepo.getAll().stream().toList();
    }

    @Override
    public List<Book> searchBookByTitle(String title) throws Exception {
        return bookRepo.getBooksByTitle(title);
    }

    private List<Book> filterByGenre(List<Book> books, String genre) {
        books = books.stream().filter((x) -> x.getGenre().equals(genre)).collect(Collectors.toList());
        return books;
    }

    private List<Book> filterByAuthor(List<Book> books, String author) {
        books = books.stream().filter((x) -> x.getAuthor().equals(author)).collect(Collectors.toList());
        return books;
    }

    private List<Book> filterByPublishYear(List<Book> books, String publishYear) {
        Integer year = Integer.valueOf(publishYear);
        books = books.stream().filter((x) -> x.getPublishYear().equals(year)).collect(Collectors.toList());
        return books;
    }

    @Override
    public List<Book> filterBooks(String genre, String author, String publishYear) throws Exception {
        List<Book> filteredBooks = bookRepo.getAvailableBooks();
        if (!genre.equals(""))
            filteredBooks = filterByGenre(filteredBooks, genre);
        if (!author.equals(""))
            filteredBooks = filterByAuthor(filteredBooks, author);
        if (!publishYear.equals(""))
            filteredBooks = filterByPublishYear(filteredBooks, publishYear);
        return filteredBooks;
    }

    @Override
    public Book recommendBook() throws Exception {
        List<Book> books = bookRepo.getAvailableBooks();
        if(books.isEmpty())
            throw new Exception("There are no available books!");
        Collections.shuffle(books);
        return books.get(0);
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

    private void notifyBookWasReturned() {
        List<LibraryUser> libraryUsers = libraryUserRepo.getAll().stream().toList();
        List<Book> availableBooks = bookRepo.getAvailableBooks();
        ExecutorService executor = Executors.newFixedThreadPool(defaultThreadsNo);
        for (LibraryUser libraryUser: libraryUsers) {
            IObserver client = loggedClients.get(libraryUser.getEmail());
            if(client != null) {
                executor.execute(() -> {
                    try {
                        System.out.println("Notifying [" + libraryUser.getEmail() + "] that a book was returned.");
                        client.bookWasReturned(availableBooks);
                    } catch (Exception exception) {
                        System.out.println("Error notifying library users " + exception);
                    }
                });
            }
        }
    }

}
