import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class LoginController {
    private IService server;
    private Stage primaryStage;
    private LibraryUser currentLibraryUser;
    private static final String LIBRARIAN_USER = "^\\w+\\.library$";

    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button signupButton;
    @FXML
    private Button loginButton;

    public void setServer(IService server) {
        this.server = server;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void login(ActionEvent event) throws Exception {
        String userName = usernameTextField.getText();
        String password = passwordField.getText();
        if (userName.matches(LIBRARIAN_USER)){
            Librarian librarian = new Librarian(userName, password);
            try {
                librarian = server.loginLibrarian(librarian);
                FXMLLoader loader = new FXMLLoader(getClass().getResource("librarian-window.fxml"));
                Scene scene = new Scene(loader.load());
                LibrarianWindowController librarianWindowController = loader.getController();
                primaryStage.setScene(scene);
            } catch (Exception exception) {
                showPopUpWindow("Authentication failure", exception.getMessage());
            }
        } else {
            currentLibraryUser = new LibraryUser(userName, password);
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("user-window.fxml"));
                Scene scene = new Scene(loader.load());
                UserWindowController userWindowController = loader.getController();

                currentLibraryUser = server.loginLibraryUser(currentLibraryUser, userWindowController);

                userWindowController.setServer(server);
                userWindowController.setPrimaryStage(primaryStage);
                userWindowController.setLibraryUser(currentLibraryUser);
                userWindowController.loadPageData();
                primaryStage.setScene(scene);
            } catch (Exception exception) {
                showPopUpWindow("Authentication failure", exception.getMessage());
            }
        }
    }

    private void showPopUpWindow(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.NONE, message, ButtonType.OK);
        alert.setTitle(title);
        alert.showAndWait();
    }

    public void signUp(ActionEvent event) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("signIn-window.fxml"));
        Scene scene = new Scene(loader.load());
        SignUpController signUpController = loader.getController();
        signUpController.setServer(server);
        signUpController.setPrimaryStage(primaryStage);
        primaryStage.setScene(scene);
    }
}
