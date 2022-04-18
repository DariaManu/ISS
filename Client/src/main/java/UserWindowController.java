import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.time.LocalDate;
import java.util.List;

public class UserWindowController implements IObserver {
    private IService server;
    private Stage primaryStage;
    private LibraryUser libraryUser;

    @FXML
    private Button borrowButton;
    @FXML
    private ListView<Book> availableBooksListView;
    private ObservableList<Book> availableBooksModel = FXCollections.observableArrayList();

    @FXML
    private ListView<Book> borrowedBooksListView;
    private ObservableList<Book> borrowedBooksModel = FXCollections.observableArrayList();

    @FXML
    private Button logoutButton;

    @FXML
    public void initialize() {
        availableBooksListView.setItems(availableBooksModel);
        availableBooksListView.setCellFactory(new Callback<ListView<Book>, ListCell<Book>>() {

            @Override
            public ListCell<Book> call(ListView<Book> param) {
                return new BookListCell();
            }
        });

        borrowedBooksListView.setItems(borrowedBooksModel);
        borrowedBooksListView.setCellFactory(new Callback<ListView<Book>, ListCell<Book>>() {
            @Override
            public ListCell<Book> call(ListView<Book> param) {
                return new BookListCell();
            }
        });
    }

    public void setServer(IService server) {
        this.server = server;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setLibraryUser(LibraryUser libraryUser) {
        this.libraryUser = libraryUser;
    }

    public void loadPageData() {
        loadAvailableBooks();
        loadBorrowedBooks();
    }

    private void loadAvailableBooks() {
        try {
            availableBooksModel.setAll(server.getAvailableBooks());
        } catch (Exception exception) {
            showPopUpWindow("Warn", exception.getMessage());
        }
    }

    private void loadBorrowedBooks() {
        try {
            borrowedBooksModel.setAll(server.getBooksBorrowedByLibraryUser(libraryUser.getID()));
        } catch (Exception exception) {
            showPopUpWindow("Warn", exception.getMessage());
        }
    }


    public void logout(ActionEvent event) {
        try  {
            server.logoutLibraryUser(libraryUser);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login-window.fxml"));
            Scene scene = new Scene(loader.load());
            LoginController loginController = loader.getController();
            loginController.setServer(server);
            loginController.setPrimaryStage(primaryStage);
            primaryStage.setScene(scene);
        } catch (Exception exception) {
            showPopUpWindow("Logout error" , exception.getMessage());
        }
    }

    private void showPopUpWindow(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.NONE, message, ButtonType.OK);
        alert.setTitle(title);
        alert.showAndWait();
    }

    public void borrow(ActionEvent event) {
        if(borrowedBooksModel.size() < 5){
            Book book = availableBooksListView.getSelectionModel().getSelectedItem();
            if(book != null) {
                Borrow borrow = new Borrow(book, libraryUser, LocalDate.now());
                try {
                    server.borrowBook(borrow);
                    borrowedBooksModel.add(book);
                } catch (Exception exception) {
                    showPopUpWindow("Warn", exception.getMessage());
                }
            }
            else {
                showPopUpWindow("Warn", "No book selected");
            }
        }
        else {
            showPopUpWindow("Warn", "Cannot borrow more books!");
        }
    }

    @Override
    public void bookWasBorrowed(List<Book> availableBooks) {
        Platform.runLater(() -> {
            availableBooksModel.setAll(availableBooks);
        });
    }

    static class BookListCell extends ListCell<Book> {
        @Override
        protected void updateItem(Book item, boolean empty) {
            super.updateItem(item, empty);
            if (item == null || empty)
                setText(null);
            else
                setText(item.getAuthor() + ", " + item.getTitle() + ", " + item.getPublishYear());
        }
    }
}
