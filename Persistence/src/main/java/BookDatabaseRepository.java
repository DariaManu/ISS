import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

public class BookDatabaseRepository implements IBookRepository{
    private JdbcUtils dbUtils;

    public BookDatabaseRepository(Properties properties) {
        dbUtils = new JdbcUtils(properties);
    }

    @Override
    public void add(Book elem) {
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Books " +
                "(isbn, title, author, genre, publish_year, status) VALUES " +
                "(?, ?, ?, ?, ?, ?)")) {
            preparedStatement.setString(1, elem.getISBN());
            preparedStatement.setString(2, elem.getTitle());
            preparedStatement.setString(3, elem.getAuthor());
            preparedStatement.setString(4, elem.getGenre());
            preparedStatement.setInt(5, elem.getPublishYear());
            preparedStatement.setString(6, elem.getStatus().toString());
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            System.out.println("Error DB " + exception);
        }
    }

    @Override
    public void delete(Book elem) {

    }

    @Override
    public void update(Book elem, Integer bookId) {
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Books " +
                "SET status=? WHERE book_id=?")) {
            preparedStatement.setString(1, elem.getStatus().toString());
            preparedStatement.setInt(2, bookId);
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            System.out.println("Error DB " + exception);
        }
    }

    @Override
    public Book findById(Integer integer) {
        return null;
    }

    @Override
    public Collection<Book> getAll() {
        Connection connection = dbUtils.getConnection();
        List<Book> books = new ArrayList<>();
        try(PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Books")) {
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                while(resultSet.next()) {
                    Integer id = resultSet.getInt("book_id");
                    String ISBN = resultSet.getString("isbn");
                    String title = resultSet.getString("title");
                    String author = resultSet.getString("author");
                    String genre = resultSet.getString("genre");
                    Integer publishYear = resultSet.getInt("publish_year");
                    Status status = Status.valueOf(resultSet.getString("status"));
                    Book book = new Book(id, ISBN, title, author, genre, publishYear, status);
                    books.add(book);
                }
            }
        } catch (SQLException exception) {
            System.out.println("Error DB " + exception);
        }
        return books;
    }

    @Override
    public List<Book> getAvailableBooks() {
        Connection connection = dbUtils.getConnection();
        List<Book> books = new ArrayList<>();
        try(PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Books " +
                "WHERE status='AVAILABLE'")) {
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                while(resultSet.next()) {
                    Integer id = resultSet.getInt("book_id");
                    String ISBN = resultSet.getString("isbn");
                    String title = resultSet.getString("title");
                    String author = resultSet.getString("author");
                    String genre = resultSet.getString("genre");
                    Integer publishYear = resultSet.getInt("publish_year");
                    Status status = Status.valueOf(resultSet.getString("status"));
                    Book book = new Book(id, ISBN, title, author, genre, publishYear, status);
                    books.add(book);
                }
            }
        } catch (SQLException exception) {
            System.out.println("Error DB " + exception);
        }
        return books;
    }
}
