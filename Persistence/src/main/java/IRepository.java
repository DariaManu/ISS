import java.util.Collection;

public interface IRepository<ID, T> {
    void add(T elem);
    void delete(T elem);
    void update(T elem, ID id);
    T findById(ID id);
    Collection<T> getAll();
}
