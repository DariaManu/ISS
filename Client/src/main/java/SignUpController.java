import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SignUpController {
    private IService server;
    private Stage primaryStage;
    private LibraryUser currentLibraryUser;

    @FXML
    private TextField cnpTextField;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField addressTextField;
    @FXML
    private TextField phoneTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private Button signUpButton;

    public void setServer(IService server) {
        this.server = server;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void signUp(ActionEvent event) {
        String cnp = cnpTextField.getText();
        String name = nameTextField.getText();
        String address = addressTextField.getText();
        String phone = phoneTextField.getText();
        String email = emailTextField.getText();
        try {
            UserInputValidator.validateLibraryUserInfo(cnp, name, address, phone, email);
            currentLibraryUser = new LibraryUser(cnp, name, address, phone, email);
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("user-window.fxml"));
                Scene scene = new Scene(loader.load());
                UserWindowController userWindowController = loader.getController();

                currentLibraryUser = server.signUpLibraryUser(currentLibraryUser, userWindowController);

                userWindowController.setServer(server);
                userWindowController.setLibraryUser(currentLibraryUser);
                userWindowController.setPrimaryStage(primaryStage);
                userWindowController.loadPageData();
                primaryStage.setScene(scene);
            } catch (Exception exception) {
                showPopUpWindow("Sign Up failure", exception.getMessage());
            }
        } catch (Exception exception) {
            showPopUpWindow("Warn", exception.getMessage());
        }

    }

    private void showPopUpWindow(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.NONE, message, ButtonType.OK);
        alert.setTitle(title);
        alert.showAndWait();
    }
}
