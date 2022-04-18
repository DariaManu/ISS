import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.List;

public class ClientWorker implements Runnable, IObserver {
    private IService service;
    private Socket connection;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;

    private static Response okResponse = new Response.Builder().setResponseType(ResponseType.OK).build();

    public ClientWorker(IService service, Socket connection) {
        this.service = service;
        this.connection = connection;
        try {
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            connected = true;
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void run() {
        while(connected) {
            try {
                Object request = input.readObject();
                Response response = handleRequest((Request)request);
                if(response != null) {
                    sendResponse(response);
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            } catch (ClassNotFoundException exception) {
                exception.printStackTrace();
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException exception) {
            System.out.println("Error " + exception);
        }
    }

    private Response handleRequest(Request request) {
        Response response = null;
        String handlerName = "handle"+(request).getRequestType();
        System.out.println("HandlerName" + handlerName);
        try {
            Method method = this.getClass().getDeclaredMethod(handlerName, Request.class);
            response = (Response) method.invoke(this, request);
            System.out.println("Method " + handlerName + " invoked");
        } catch (NoSuchMethodException exception) {
            exception.printStackTrace();
        } catch (InvocationTargetException exception) {
            exception.printStackTrace();
        } catch (IllegalAccessException exception) {
            exception.printStackTrace();
        }
        return response;
    }

    private void sendResponse(Response response) throws IOException {
        System.out.println("Sending response " + response);
        output.writeObject(response);
        output.flush();
    }

    private Response handleLOGIN(Request request) {
        System.out.println("Login request...");
        LibraryUser libraryUser = (LibraryUser) request.getData();
        try {
            libraryUser = service.loginLibraryUser(libraryUser, this);
            return new Response.Builder().setResponseType(ResponseType.OK).setData(libraryUser).build();
        } catch (Exception exception) {
            return new Response.Builder().setResponseType(ResponseType.ERROR).setData(exception.getMessage()).build();
        }
    }

    private Response handleLOGOUT(Request request) {
        System.out.println("Logout request...");
        LibraryUser libraryUser = (LibraryUser) request.getData();
        try {
            service.logoutLibraryUser(libraryUser);
            return okResponse;
        } catch (Exception exception) {
            return new Response.Builder().setResponseType(ResponseType.ERROR).setData(exception.getMessage()).build();
        }
    }

    private Response handleSIGNUP(Request request) {
        System.out.println("Sign up request...");
        LibraryUser libraryUser = (LibraryUser) request.getData();
        try {
            libraryUser = service.signUpLibraryUser(libraryUser, this);
            return new Response.Builder().setResponseType(ResponseType.OK).setData(libraryUser).build();
        } catch (Exception exception) {
            return new Response.Builder().setResponseType(ResponseType.ERROR).setData(exception.getMessage()).build();
        }
    }

    private Response handleGET_AVAILABLE_BOOKS(Request request) {
        System.out.println("GetAvailableBooks request...");
        try {
            List<Book> books = service.getAvailableBooks();
            return new Response.Builder().setResponseType(ResponseType.GET_AVAILABLE_BOOKS).setData(books).build();
        } catch (Exception exception) {
            return new Response.Builder().setResponseType(ResponseType.ERROR).setData(exception.getMessage()).build();
        }
    }

    private Response handleGET_BORROWED_BOOKS(Request request) {
        System.out.println("GetBorrowedBooks request");
        Integer libraryUserId = (Integer) request.getData();
        try{
            List<Book> books = service.getBooksBorrowedByLibraryUser(libraryUserId);
            return new Response.Builder().setResponseType(ResponseType.GET_BORROWED_BOOKS).setData(books).build();
        } catch (Exception exception) {
            return new Response.Builder().setResponseType(ResponseType.ERROR).setData(exception.getMessage()).build();
        }
    }

    private Response handleADD_BORROW(Request request) {
        System.out.println("AddBorrow request");
        Borrow borrow = (Borrow) request.getData();
        try{
            service.borrowBook(borrow);
            return okResponse;
        } catch (Exception exception) {
            return new Response.Builder().setResponseType(ResponseType.ERROR).setData(exception.getMessage()).build();
        }
    }

    @Override
    public void bookWasBorrowed(List<Book> availableBooks) throws Exception{
        Response response = new Response.Builder().setResponseType(ResponseType.BOOK_BORROWED).setData(availableBooks).build();
        System.out.println("A book was borrowed");
        try {
            sendResponse(response);
        } catch (IOException exception) {
            throw new Exception("Sending error: " + exception);
        }
    }
}