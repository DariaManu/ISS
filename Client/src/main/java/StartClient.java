import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Properties;

public class StartClient extends Application {
    private Stage primaryStage;
    private int defaultPort = 55555;
    private static String defaultServer = "localhost";

    @Override
    public void start(Stage stage) throws Exception {
        System.out.println("In start");
        Properties clientProperties = new Properties();
        try {
            clientProperties.load(StartClient.class.getResourceAsStream("/client.properties"));
            System.out.println("Client properties set");
            clientProperties.list(System.out);
        } catch (IOException exception) {
            System.out.println("Cannot find client.properties " + exception);
            return;
        }

        String serverIP = clientProperties.getProperty("server.host", defaultServer);
        int serverPort = defaultPort;
        try {
            serverPort = Integer.parseInt(clientProperties.getProperty("server.port"));
        } catch (NumberFormatException exception) {
            System.out.println("Wrong port number " + exception.getMessage());
            System.out.println("Using default port " + defaultPort);
        }
        System.out.println("Using server IP " + serverIP);
        System.out.println("Using server port " + serverPort);

        IService server = new ServiceProxy(serverIP, serverPort);

        primaryStage = stage;
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("login-window.fxml"));
        Scene scene = new Scene(loader.load());
        LoginController controller = loader.getController();
        controller.setServer(server);
        controller.setPrimaryStage(primaryStage);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
