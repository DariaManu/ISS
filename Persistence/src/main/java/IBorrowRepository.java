import java.util.List;

public interface IBorrowRepository extends IRepository<Integer, Borrow>{
    List<Borrow> getBorrowsByLibraryUserId(Integer libraryUserId);
    void deleteByBookId(Integer bookId);
}
