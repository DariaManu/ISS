import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.Collection;

public class LibrarianHibernateRepository implements ILibrarianRepository{
    @Override
    public Librarian findByUserNameAndPassword(String userName, String password) {
        SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
        Librarian librarian = null;
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                librarian = session.createNativeQuery("SELECT * FROM Librarians " +
                        "WHERE username=? AND password=?", Librarian.class)
                        .setParameter(1, userName)
                        .setParameter(2, password)
                        .setMaxResults(1)
                        .uniqueResult();
                transaction.commit();
                return librarian;
            } catch (RuntimeException exception) {
                if (transaction != null) {
                    transaction.rollback();
                }
            }
        } catch (Exception exception) {
            System.out.println("Exception " + exception.getMessage());
        }
        return librarian;
    }

    @Override
    public void add(Librarian elem) {

    }

    @Override
    public void delete(Librarian elem) {

    }

    @Override
    public void update(Librarian elem, Integer integer) {

    }

    @Override
    public Librarian findById(Integer integer) {
        return null;
    }

    @Override
    public Collection<Librarian> getAll() {
        return null;
    }
}
