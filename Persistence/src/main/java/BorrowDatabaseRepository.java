import java.sql.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.sql.Date;

public class BorrowDatabaseRepository implements IBorrowRepository {
    private JdbcUtils dbUtils;

    public BorrowDatabaseRepository(Properties properties) {
        dbUtils = new JdbcUtils(properties);
    }

    @Override
    public void add(Borrow elem) {
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Borrows " +
                "(book_id, library_user_id, date) VALUES " +
                "(?, ?, ?)")) {
            preparedStatement.setInt(1, elem.getBook().getID());
            preparedStatement.setInt(2, elem.getLibraryUser().getID());
            Instant instant = elem.getDate().toInstant();
            ZoneId zoneId = ZoneId.systemDefault();
            ZonedDateTime zdt = ZonedDateTime.ofInstant(instant, zoneId);
            LocalDate localDate = zdt.toLocalDate();
            preparedStatement.setDate(3, java.sql.Date.valueOf(localDate));
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            System.out.println("Error DB " + exception);
        }
    }

    @Override
    public void delete(Borrow elem) {

    }

    @Override
    public void update(Borrow elem, Integer integer) {

    }

    @Override
    public Borrow findById(Integer integer) {
        return null;
    }

    @Override
    public Collection<Borrow> getAll() {
        Connection connection = dbUtils.getConnection();
        List<Borrow> borrows = new ArrayList<>();
        try(PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Books B1 " +
                "INNER JOIN Borrows B2 ON B1.book_id=B2.book_id " +
                "INNER JOIN LibraryUsers L ON B2.library_user_id=L.library_user_id;")) {
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                while(resultSet.next()) {
                    int bookId = resultSet.getInt("book_id");
                    String ISBN = resultSet.getString("isbn");
                    String title = resultSet.getString("title");
                    String author = resultSet.getString("author");
                    String genre = resultSet.getString("genre");
                    int publishYear = resultSet.getInt("publish_year");
                    Status status = Status.valueOf(resultSet.getString("status"));
                    Book book = new Book(bookId, ISBN, title, author, genre, publishYear, status);

                    int libraryUserId = resultSet.getInt("library_user_id");
                    String cnp = resultSet.getString("cnp");
                    String name = resultSet.getString("name");
                    String address = resultSet.getString("address");
                    String phone = resultSet.getString("phone");
                    String email = resultSet.getString("email");
                    LibraryUser libraryUser = new LibraryUser(libraryUserId, cnp, name, address, phone, email);

                    int borrow_id = resultSet.getInt("borrow_id");
                    Date date = resultSet.getDate("date");
                    Borrow borrow = new Borrow(borrow_id, book, libraryUser, date);
                    borrows.add(borrow);
                }
            }
        } catch (SQLException exception) {
            System.out.println("Error DB " + exception);
        }
        return borrows;
    }

    @Override
    public List<Borrow> getBorrowsByLibraryUserId(Integer libraryUserId) {
        Connection connection = dbUtils.getConnection();
        List<Borrow> borrows = new ArrayList<>();
        try(PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Books B1 " +
                "INNER JOIN Borrows B2 ON B1.book_id=B2.book_id " +
                "INNER JOIN LibraryUsers L ON B2.library_user_id=L.library_user_id " +
                "WHERE L.library_user_id=?;")) {
            preparedStatement.setInt(1, libraryUserId);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                while(resultSet.next()) {
                    int bookId = resultSet.getInt("book_id");
                    String ISBN = resultSet.getString("isbn");
                    String title = resultSet.getString("title");
                    String author = resultSet.getString("author");
                    String genre = resultSet.getString("genre");
                    int publishYear = resultSet.getInt("publish_year");
                    Status status = Status.valueOf(resultSet.getString("status"));
                    Book book = new Book(bookId, ISBN, title, author, genre, publishYear, status);

                    String cnp = resultSet.getString("cnp");
                    String name = resultSet.getString("name");
                    String address = resultSet.getString("address");
                    String phone = resultSet.getString("phone");
                    String email = resultSet.getString("email");
                    LibraryUser libraryUser = new LibraryUser(libraryUserId, cnp, name, address, phone, email);

                    int borrow_id = resultSet.getInt("borrow_id");
                    Date date = resultSet.getDate("date");
                    Borrow borrow = new Borrow(borrow_id, book, libraryUser, date);
                    borrows.add(borrow);
                }
            }
        } catch (SQLException exception) {
            System.out.println("Error DB " + exception);
        }
        return borrows;
    }
}
