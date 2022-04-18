import java.net.Socket;

public abstract class AbstractConcurrentServer extends AbstractServer {

    public AbstractConcurrentServer(int port) {
        super(port);
    }

    @Override
    public void processRequest(Socket client) {
        Thread threadWorker = createWorker(client);
        threadWorker.start();
    }

    public abstract Thread createWorker(Socket client);
}
