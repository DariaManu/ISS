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
import java.util.Date;
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
    private TextField genreTextField;
    @FXML
    private TextField searchBookTextField;
    @FXML
    private Button searchBookButton;
    @FXML
    private TextField authorTextField;
    @FXML
    private TextField releaseYearTextField;
    @FXML
    private Button applyFiltersButton;
    @FXML
    private Button getRecommendationButton;
    @FXML
    private Button showBorrowedBooksButton;
    @FXML
    private Button clearFiltersButton;

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
                Borrow borrow = new Borrow(book, libraryUser, new Date());
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

    @Override
    public void bookWasReturned(List<Book> availableBooks) throws Exception {
        Platform.runLater(() -> {
            availableBooksModel.setAll(availableBooks);
        });
    }

    public void showBorrowedBooks(ActionEvent event) {
        loadBorrowedBooks();
    }

    public void searchBook(ActionEvent event) {
        String title = searchBookTextField.getText();
        try {
            List<Book> books = server.searchBookByTitle(title);
            availableBooksModel.setAll(books);
        } catch (Exception exception) {
            showPopUpWindow("Warn", exception.getMessage());
        }
    }

    public void applyFilters(ActionEvent event) {
        String genre = genreTextField.getText();
        String author = authorTextField.getText();
        String publishYear = releaseYearTextField.getText();
        try {
            List<Book> books = server.filterBooks(genre, author, publishYear);
            availableBooksModel.setAll(books);
        } catch (Exception exception) {
            showPopUpWindow("Warn", exception.getMessage());
        }
    }

    public void getRecommendation(ActionEvent event) {
        try {
            Book book = server.recommendBook();
            availableBooksModel.setAll(book);
        } catch (Exception exception) {
            showPopUpWindow("Warn", exception.getMessage());
        }
    }

    public void clearFilters(ActionEvent event) {
        loadAvailableBooks();
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
