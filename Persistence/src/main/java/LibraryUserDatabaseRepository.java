import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

public class LibraryUserDatabaseRepository implements ILibraryUserRepository{
    private JdbcUtils dbUtils;

    public LibraryUserDatabaseRepository(Properties properties) {
        dbUtils = new JdbcUtils(properties);
    }

    @Override
    public void add(LibraryUser elem) {
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO LibraryUsers " +
                "(cnp, name, address, phone, email) VALUES " +
                "(?, ?, ?, ?, ?)")) {
            preparedStatement.setString(1, elem.getCnp());
            preparedStatement.setString(2, elem.getName());
            preparedStatement.setString(3, elem.getAddress());
            preparedStatement.setString(4, elem.getPhone());
            preparedStatement.setString(5, elem.getEmail());
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            System.out.println("Error DB " + exception);
        }
    }

    @Override
    public void delete(LibraryUser elem) {

    }

    @Override
    public void update(LibraryUser elem, Integer integer) {

    }

    @Override
    public LibraryUser findById(Integer integer) {
        return null;
    }

    @Override
    public Collection<LibraryUser> getAll() {
        Connection connection = dbUtils.getConnection();
        List<LibraryUser> libraryUsers = new ArrayList<>();
        try(PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM LibraryUsers")) {
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                while(resultSet.next()) {
                    Integer id = resultSet.getInt("library_user_id");
                    String cnp = resultSet.getString("cnp");
                    String name = resultSet.getString("name");
                    String address = resultSet.getString("address");
                    String phone = resultSet.getString("phone");
                    String email = resultSet.getString("email");
                    LibraryUser libraryUser = new LibraryUser(id, cnp, name, address, phone, email);
                    libraryUsers.add(libraryUser);
                }
            }
        } catch (SQLException exception) {
            System.out.println("Error DB " + exception);
        }
        return libraryUsers;
    }

    @Override
    public LibraryUser findByEmailAndCNP(String email, String cnp) {
        Connection connection = dbUtils.getConnection();
        LibraryUser libraryUser = null;
        try(PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM LibraryUsers " +
                "WHERE email=? AND cnp=?")) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, cnp);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                if(resultSet.next()) {
                    Integer id = resultSet.getInt("library_user_id");
                    String name = resultSet.getString("name");
                    String address = resultSet.getString("address");
                    String phone = resultSet.getString("phone");
                    libraryUser = new LibraryUser(id, cnp, name, address, phone, email);
                }
            }
        } catch (SQLException exception) {
            System.out.println("Error DB " + exception);
        }
        return libraryUser;
    }

    @Override
    public LibraryUser findByEmail(String email) {
        return null;
    }
}
