import java.awt.*;
import java.awt.event.InputEvent;
import java.io.*;
import java.net.Socket;

public class ClientHandler extends Thread {

    final Socket m_socket;
    final Robot rob = new Robot();

    public ClientHandler(Socket socket) throws AWTException {
        m_socket = socket;
    }

    @Override
    public void run() {
        String in;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(m_socket.getInputStream()));
            System.out.println("Reading ready to go.");
            while (br.ready() && ((in = br.readLine()) != null)) {
                System.out.println("Line: (" + in + ")");
                if (in.contains("MouseMove")) {
                    handleMouseMovement(in);
                }
                if (in.contains("MouseClick")) {
                    handleMouseClick();
                }
            }
            br.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private void handleMouseMovement(String input) {
        String[] variables = input.split(":");
        float x = Float.parseFloat(variables[1]);
        float y = Float.parseFloat(variables[2]);
        System.out.println("MOUSE MOVEMENT INC: " + x + ";" + y );
        Point mouse_point = MouseInfo.getPointerInfo().getLocation();
        float current_x = mouse_point.x;
        float current_y = mouse_point.y;
        rob.mouseMove((int) (current_x + x), (int) (current_y + y));
    }

    private void handleMouseClick() {
        System.out.println("MOUSE CLICKED!");
        rob.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        rob.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

    }
}