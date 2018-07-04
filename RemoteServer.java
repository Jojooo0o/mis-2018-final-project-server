import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class RemoteServer {

    private final ServerSocket server;
    private static final int PORT = 8090;
    Robot rob = new Robot();

    public RemoteServer(int port) throws IOException, AWTException {
        server = new ServerSocket(port);
        //server.setSoTimeout(180000);
    }

    private void connect() throws IOException, AWTException {
        Socket socket;
        rob = new Robot();
        System.out.println("Server started.");
        while(true) {
            socket = server.accept();
            System.out.println("Client connected: " + socket.getInetAddress());
            //Thread t = new ClientHandler(socket);
            //t.start();
            //System.out.println("Thread for reading started");
            while(socket.isConnected()) {
                BufferedImage img = captureScreen();
                ImageIO.write(img, "bmp", socket.getOutputStream());
                img.flush();
            }
            //System.out.println("Client disconnected!");
            socket.close();
        }
    }

    private BufferedImage captureScreen() throws AWTException {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle screenRect = new Rectangle(screenSize);
        return rob.createScreenCapture(screenRect);
    }

    public static void main(String[] args) throws IOException, AWTException {
        RemoteServer server = new RemoteServer(PORT);
        server.connect();
    }
}
