import java.net.Socket;

public class ConcurrentServer extends AbstractConcurrentServer {
    private IService service;

    public ConcurrentServer(int port, IService service) {
        super(port);
        this.service = service;
    }

    @Override
    public Thread createWorker(Socket client) {
        ClientWorker worker = new ClientWorker(service, client);
        Thread tw = new Thread(worker);
        return tw;
    }

    @Override
    public void stop() throws ServerException {
        System.out.println("Stopping services...");
    }
}
