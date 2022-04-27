import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.List;

public class LibrarianWindowController {
    private IService server;
    private Stage primaryStage;

    @FXML
    private TextField searchTextField;
    @FXML
    private Button searchButton;
    @FXML
    private ListView<Book> borrowedBooksListView;
    private ObservableList<Book> borrowedBooksModel = FXCollections.observableArrayList();
    @FXML
    private Button returnBookButton;
    @FXML
    private Button viewAllBooksButton;
    @FXML
    private TextField isbnTextField;
    @FXML
    private TextField titleTextField;
    @FXML
    private TextField authorTextField;
    @FXML
    private TextField genreTextField;
    @FXML
    private TextField publishYearTextField;
    @FXML
    private Button addBookButton;

    @FXML
    public void initialize() {
        borrowedBooksListView.setItems(borrowedBooksModel);
        borrowedBooksListView.setCellFactory(new Callback<ListView<Book>, ListCell<Book>>() {
            @Override
            public ListCell<Book> call(ListView<Book> param) {
                return new UserWindowController.BookListCell();
            }
        });
    }

    public void setServer(IService server) {
        this.server = server;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    private void loadBorrowedBooks(List<Book> books) {
        borrowedBooksModel.setAll(books);
    }

    public void searchLibraryUser(ActionEvent event) {
        String email = searchTextField.getText();
        try {
            List<Book> books = server.searchLibraryUserAndGetBooks(email);
            loadBorrowedBooks(books);
        } catch (Exception exception) {
            showPopUpWindow("Warn", exception.getMessage());
        }
    }

    public void returnBook(ActionEvent event) {
        Book book = borrowedBooksListView.getSelectionModel().getSelectedItem();
        if (book == null) {
            showPopUpWindow("Warn", "No book selected!");
        } else {
            try {
                server.returnBook(book);
                borrowedBooksModel.remove(book);
            } catch (Exception exception) {
                showPopUpWindow("Warn", exception.getMessage());
            }
        }
    }

    private void showPopUpWindow(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.NONE, message, ButtonType.OK);
        alert.setTitle(title);
        alert.showAndWait();
    }

    public void addBook(ActionEvent event) {
        String ISBN = isbnTextField.getText();
        String title = titleTextField.getText();
        String author = authorTextField.getText();
        String genre = genreTextField.getText();
        Integer publishYear = Integer.valueOf(publishYearTextField.getText());
        try {
            Book book = new Book(ISBN, title, author, genre, publishYear, Status.AVAILABLE);
            server.addBook(book);
        } catch (Exception exception) {
            showPopUpWindow("Warn", exception.getMessage());
        }
    }

    public void showAllBooks(ActionEvent event) {

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
