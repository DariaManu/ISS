import java.io.IOException;
import java.util.Properties;

public class StartServer {
    private static int defaultPort = 55555;

    public static void main(String[] args) {
        Properties serverProperties = new Properties();
        try{
            serverProperties.load(StartServer.class.getResourceAsStream("/server.properties"));
            System.out.println("Server properties set");
            serverProperties.list(System.out);
        } catch (IOException exception) {
            System.out.println("Cannot find server properties " + exception);
            return;
        }

        IBookRepository bookRepository = new BookDatabaseRepository(serverProperties);
        ILibraryUserRepository libraryUserRepository = new LibraryUserDatabaseRepository(serverProperties);
        IBorrowRepository borrowRepository = new BorrowDatabaseRepository(serverProperties);
        IService service = new Service(bookRepository, libraryUserRepository, borrowRepository);

        int serverPort = defaultPort;
        try{
            serverPort = Integer.parseInt(serverProperties.getProperty("server.port"));
        } catch (NumberFormatException exception) {
            System.out.println("Wrong port number: " + exception);
            System.out.println("Using default port: " + defaultPort);
        }
        System.out.println("Starting server on port: " + serverPort);

        AbstractServer server = new ConcurrentServer(serverPort, service);
        try {
            server.start();
        } catch (ServerException exception) {
            System.out.println("Error starting the server" + exception.getMessage());
        } finally {
            try {
                server.stop();
            } catch (ServerException exception) {
                System.out.println("Error stopping server " + exception.getMessage());
            }
        }
    }
}
