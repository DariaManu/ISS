import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.xml.crypto.dsig.spec.ExcC14NParameterSpec;
import java.util.Collection;
import java.util.List;

public class BorrowHibernateRepository implements IBorrowRepository {
    @Override
    public List<Borrow> getBorrowsByLibraryUserId(Integer libraryUserId) {
        SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
        List<Borrow> borrows = null;
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                /*borrows = session.createNativeQuery("SELECT * FROM Borrows " +
                        "WHERE library_user_id=?", Borrow.class)
                        .setParameter(1, libraryUserId)
                        .list();*/
                borrows = session.createQuery("select b from Borrow b " +
                        "join fetch b.book " +
                        "join b.libraryUser l join fetch b.libraryUser where l.ID = :libraryUserId")
                                .setParameter("libraryUserId", libraryUserId)
                                        .list();
                transaction.commit();
                return borrows;
            } catch (RuntimeException exception) {
                if (transaction != null) {
                    transaction.rollback();
                }
            }
        } catch (Exception exception) {
            System.out.println("Exception " + exception);
        }
        return borrows;
    }

    @Override
    public void deleteByBookId(Integer bookId) {
        SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                int result = session.createQuery("delete Borrow where book_id = :bookId")
                        .setParameter("bookId", bookId).executeUpdate();
                transaction.commit();
            } catch (RuntimeException exception) {
                if (transaction != null) {
                    transaction.rollback();
                }
            }
        } catch (Exception exception) {
            System.out.println("Exception " + exception.getMessage());
        }
    }

    @Override
    public void add(Borrow elem) {
        SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                session.save(elem);
                transaction.commit();
            } catch (RuntimeException exception) {
                if (transaction != null) {
                    transaction.rollback();
                }
            }
        } catch (Exception exception) {
            System.out.println("Exception " + exception.getMessage());
        }
    }

    @Override
    public void delete(Borrow elem) {

    }

    @Override
    public void update(Borrow elem, Integer integer) {

    }

    @Override
    public Borrow findById(Integer integer) {
        return null;
    }

    @Override
    public Collection<Borrow> getAll() {
        SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
        List<Borrow> borrows = null;
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                //borrows = session.createNativeQuery("SELECT * FROM Borrows", Borrow.class).list();
                borrows = session.createQuery("select bor from Borrow bor " +
                        "join fetch bor.book " +
                        "join fetch bor.libraryUser").list();
                transaction.commit();
                return borrows;
            } catch (RuntimeException exception) {
                if (transaction != null) {
                    transaction.rollback();
                }
            }
        } catch (Exception exception) {
            System.out.println("Exception " + exception.getMessage());
        }
        return borrows;
    }
}
