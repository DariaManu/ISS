import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.Collection;
import java.util.List;

public class BookHibernateRepository implements IBookRepository {
    @Override
    public List<Book> getAvailableBooks() {
        SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
        List<Book> books = null;
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                books = session.createNativeQuery("SELECT * FROM Books " +
                        "WHERE status='AVAILABLE'", Book.class).list();
                transaction.commit();
                return books;
            } catch (RuntimeException exception) {
                if (transaction != null) {
                    transaction.rollback();
                }
            }
        } catch (Exception exception) {
            System.out.println("Exception " + exception.getMessage());
        }
        return books;
    }

    @Override
    public List<Book> getBooksByTitle(String title) {
        SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
        List<Book> books = null;
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                books = session.createNativeQuery("SELECT * FROM Books " +
                                "WHERE title=? AND status='AVAILABLE'", Book.class)
                        .setParameter(1, title)
                        .list();
                transaction.commit();
                return books;
            } catch (RuntimeException exception) {
                if (transaction != null) {
                    transaction.rollback();
                }
            }
        } catch (Exception exception) {
            System.out.println("Exception " + exception.getMessage());
        }
        return books;
    }

    @Override
    public void add(Book elem) {
        SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
        try(Session session = sessionFactory.openSession()) {
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
    public void delete(Book elem) {

    }

    @Override
    public void update(Book elem, Integer id) {
        SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                Book book = (Book) session.load(Book.class, Integer.valueOf(id));
                book.setStatus(elem.getStatus());
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
    public Book findById(Integer integer) {
        return null;
    }

    @Override
    public Collection<Book> getAll() {
        SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
        List<Book> books = null;
        try(Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                books = session.createNativeQuery("SELECT * FROM Books", Book.class).list();
                transaction.commit();
                return books;
            } catch (RuntimeException exception) {
                if (transaction != null) {
                    transaction.rollback();
                }
            }
        } catch (Exception exception) {
            System.out.println("Exception " + exception);
        }
        return books;
    }
}
