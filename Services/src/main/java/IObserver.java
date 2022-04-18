import java.util.List;

public interface IObserver {
    void bookWasBorrowed(List<Book> availableBooks) throws Exception;
}
