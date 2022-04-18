public interface ILibraryUserRepository extends IRepository<Integer, LibraryUser>{
    LibraryUser findByEmailAndCNP(String email, String cnp);
}
