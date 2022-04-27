import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LibraryUserHibernateRepository implements ILibraryUserRepository {
    @Override
    public LibraryUser findByEmailAndCNP(String email, String cnp) {
        SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
        LibraryUser libraryUser = null;
        try(Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                libraryUser = session.createNativeQuery("SELECT * FROM LibraryUsers " +
                        "WHERE email=? and cnp=?", LibraryUser.class)
                        .setParameter(1, email)
                        .setParameter(2, cnp)
                        .setMaxResults(1)
                        .uniqueResult();
                transaction.commit();
                return libraryUser;
            } catch (RuntimeException exception) {
                if (transaction != null)
                    transaction.rollback();
            }
        } catch (Exception exception) {
            System.out.println("Exception " + exception.getMessage());
        }
        return libraryUser;
    }

    @Override
    public void add(LibraryUser elem) {
        SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
        try(Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                session.save(elem);
                transaction.commit();
            } catch (RuntimeException exception) {
                if(transaction != null) {
                    transaction.rollback();
                }
            }
        } catch (Exception exception) {
            System.out.println("Exception " + exception.getMessage());
        }
    }

    @Override
    public void delete(LibraryUser elem) {

    }

    @Override
    public void update(LibraryUser elem, Integer integer) {

    }

    @Override
    public LibraryUser findById(Integer integer) {
        return null;
    }

    @Override
    public Collection<LibraryUser> getAll() {
        SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
        List<LibraryUser> libraryUsers = null;
        try(Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                libraryUsers = session.createNativeQuery("SELECT * FROM LibraryUsers", LibraryUser.class).list();
                transaction.commit();
            } catch (RuntimeException exception) {
                if (transaction != null) {
                    transaction.rollback();
                }
            } catch (Exception exception) {
                System.out.println("Exception " + exception.getMessage());
            }
        }
        return libraryUsers;
    }
}
