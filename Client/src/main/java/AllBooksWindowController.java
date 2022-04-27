import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

import java.util.List;

public class AllBooksWindowController {
    @FXML
    private ListView<Book> allBooksListView;
    private ObservableList<Book> allBooksModel = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        allBooksListView.setItems(allBooksModel);
        allBooksListView.setCellFactory(new Callback<ListView<Book>, ListCell<Book>>() {
            @Override
            public ListCell<Book> call(ListView<Book> param) {
                return new AllBooksWindowController.BookListCell();
            }
        });
    }

    public void loadBooks(List<Book> books) {
        allBooksModel.setAll(books);
    }

    static class BookListCell extends ListCell<Book> {
        @Override
        protected void updateItem(Book item, boolean empty) {
            super.updateItem(item, empty);
            if (item == null || empty)
                setText(null);
            else
                setText(item.getISBN() + ", " + item.getAuthor() + ", " + item.getTitle() + ", " + item.getGenre()
                + ", " + item.getPublishYear() + ", " + item.getStatus().toString());
        }
    }
}
