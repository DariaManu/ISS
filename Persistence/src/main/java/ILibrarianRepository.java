public interface ILibrarianRepository extends IRepository<Integer, Librarian> {
    public Librarian findByUserNameAndPassword(String userName, String password);
}
