import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ServiceProxy implements IService{
    private String host;
    private int port;

    private IObserver client;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;

    private BlockingQueue<Response> queueResponse;
    private volatile boolean finished;

    public ServiceProxy(String host, int port) {
        this.host = host;
        this.port = port;
        queueResponse = new LinkedBlockingQueue<Response>();
    }

    @Override
    public LibraryUser loginLibraryUser(LibraryUser libraryUser, IObserver client) throws Exception {
        initializeConnection();
        Request request = new Request.Builder().setRequestType(RequestType.LOGIN).setData(libraryUser).build();
        sendRequest(request);
        Response response = readResponse();
        if (response.getResponseType() == ResponseType.OK) {
            this.client = client;
            return (LibraryUser) response.getData();
        }
        if (response.getResponseType() == ResponseType.ERROR) {
            String error = response.getData().toString();
            closeConnection();
            throw new Exception(error);
        }
        return libraryUser;
    }

    @Override
    public Librarian loginLibrarian(Librarian librarian) throws Exception {
        initializeConnection();
        Request request = new Request.Builder().setRequestType(RequestType.LOGIN_LIBRARIAN).setData(librarian).build();
        sendRequest(request);
        Response response = readResponse();
        if (response.getResponseType() == ResponseType.OK) {
            return (Librarian) response.getData();
        }
        if (response.getResponseType() == ResponseType.ERROR) {
            String error = response.getData().toString();
            closeConnection();
            throw new Exception(error);
        }
        return librarian;
    }

    @Override
    public List<Book> searchLibraryUserAndGetBooks(String email) throws Exception {
        Request request = new Request.Builder().setRequestType(RequestType.SEARCH_LIBRARY_USER).setData(email).build();
        sendRequest(request);
        Response response = readResponse();
        if (response.getResponseType() == ResponseType.OK) {
            List<Book> books = (List<Book>) response.getData();
            return books;
        }
        if (response.getResponseType() == ResponseType.ERROR) {
            String error = response.getData().toString();
            throw new Exception(error);
        }
        return null;
    }

    @Override
    public void logoutLibraryUser(LibraryUser libraryUser) throws Exception {
        Request request = new Request.Builder().setRequestType(RequestType.LOGOUT).setData(libraryUser).build();
        sendRequest(request);
        Response response = readResponse();
        closeConnection();
        if (response.getResponseType() == ResponseType.ERROR) {
            String error = response.getData().toString();
            throw new Exception(error);
        }
    }

    @Override
    public LibraryUser signUpLibraryUser(LibraryUser libraryUser, IObserver client) throws Exception {
        initializeConnection();
        Request request = new Request.Builder().setRequestType(RequestType.SIGNUP).setData(libraryUser).build();
        sendRequest(request);
        Response response = readResponse();
        if(response.getResponseType() == ResponseType.OK) {
            this.client = client;
            return (LibraryUser) response.getData();
        }
        if (response.getResponseType() == ResponseType.ERROR) {
            String error = response.getData().toString();
            closeConnection();
            throw new Exception(error);
        }
        return libraryUser;
    }

    @Override
    public List<Book> getAvailableBooks() throws Exception {
        Request request = new Request.Builder().setRequestType(RequestType.GET_AVAILABLE_BOOKS).build();
        sendRequest(request);
        Response response = readResponse();
        if(response.getResponseType() == ResponseType.ERROR) {
            String error = response.getData().toString();
            throw new Exception(error);
        }
        List<Book> books = (List<Book>) response.getData();
        return books;
    }

    @Override
    public List<Book> getBooksBorrowedByLibraryUser(Integer libraryUserId) throws Exception {
        Request request = new Request.Builder().setRequestType(RequestType.GET_BORROWED_BOOKS).setData(libraryUserId).build();
        sendRequest(request);
        Response response = readResponse();
        if(response.getResponseType() == ResponseType.ERROR) {
            String error = response.getData().toString();
            throw new Exception(error);
        }
        List<Book> books = (List<Book>) response.getData();
        return books;
    }

    @Override
    public void borrowBook(Borrow borrow) throws Exception {
        Request request = new Request.Builder().setRequestType(RequestType.ADD_BORROW).setData(borrow).build();
        sendRequest(request);
        Response response = readResponse();
        if(response.getResponseType() == ResponseType.ERROR) {
            String error = response.getData().toString();
            throw new Exception(error);
        }
    }

    private void closeConnection() {
        finished = true;
        try{
            input.close();
            output.close();
            connection.close();
            client = null;
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private void sendRequest(Request request) throws Exception {
        try {
            output.writeObject(request);
            output.flush();
        } catch (IOException exception) {
            throw new Exception("Error sending object " + exception);
        }
    }

    private Response readResponse() throws Exception {
        Response response = null;
        try {
            response = queueResponse.take();
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
        return response;
    }

    private void initializeConnection() throws Exception {
        try {
            connection = new Socket(host, port);
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            finished = false;
            startReader();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private void startReader() {
        Thread thread = new Thread(new ReaderThread());
        thread.start();
    }

    private void handleUpdate(Response response) {
        if(response.getResponseType() == ResponseType.BOOK_BORROWED) {
            System.out.println("A book was borrowed");
            try {
                List<Book> availableBooks = (List<Book>) response.getData();
                client.bookWasBorrowed(availableBooks);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    private boolean isUpdate(Response response) {
        return response.getResponseType() == ResponseType.BOOK_BORROWED;
    }

    private class ReaderThread implements Runnable {
        public void run() {
            while(!finished) {
                try {
                    Object response = input.readObject();
                    System.out.println("Response received " + response);
                    if (isUpdate((Response) response)) {
                        handleUpdate((Response) response);
                    } else {
                        try {
                            queueResponse.put((Response) response);
                        } catch (InterruptedException exception) {
                            exception.printStackTrace();
                        }
                    }
                } catch (IOException exception) {
                    System.out.println("Reading error " + exception);
                } catch (ClassNotFoundException exception) {
                    System.out.println("Reading error " + exception);
                }
            }
        }
    }
}
