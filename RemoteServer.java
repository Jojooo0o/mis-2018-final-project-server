import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class RemoteServer {

    private final ServerSocket server;
    private static final int PORT = 8090;

    public RemoteServer(int port) throws IOException {
        server = new ServerSocket(port);
        server.setSoTimeout(180000);
    }

    private void connect() throws IOException, AWTException {
        Socket socket;
        while(true) {
            socket = server.accept();
            while(socket.isConnected()) {
                System.out.println("is connected");
                readData(socket);
                BufferedImage img = captureScreen();
                ImageIO.write(img, "bmp", socket.getOutputStream());
                img.flush();
            }
            socket.close();
            System.out.println("not any more");
        }
    }

    private void readData(Socket socket) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        while(br.ready()) {
            System.out.println(br.readLine());
        }
    }

    private BufferedImage captureScreen() throws AWTException {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle screenRect = new Rectangle(screenSize);
        Robot rob = new Robot();
        return rob.createScreenCapture(screenRect);
    }

    public static void main(String[] args) throws IOException, AWTException {
        RemoteServer server = new RemoteServer(PORT);
        server.connect();
    }
}
