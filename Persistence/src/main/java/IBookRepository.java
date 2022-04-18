import java.util.List;

public interface IBookRepository extends IRepository<Integer, Book>{
    List<Book> getAvailableBooks();
}
