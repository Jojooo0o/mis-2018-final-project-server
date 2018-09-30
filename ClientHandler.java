import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
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
            while(m_socket.isConnected()){
                BufferedReader br = new BufferedReader(new InputStreamReader(m_socket.getInputStream()));
                while (br.ready() && ((in = br.readLine()) != null)) {
                    //System.out.println("rec data: (" + in + ")");
                    if (in.contains("MouseMove")) {
                        //handleMouseMovement(in);
                    } else if (in.contains("MouseClick")) {
                       handleMouseClick(in);
                    } else if (in.contains("Swipe")) {
                        handleSwipe(in);
                    }
                }
            }
            //br.close(); //---- closes the socket since inputstream is socket -> error in remoteserver
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
        rob.mouseMove((int) (x), (int) (y));
    }

    private void handleMouseClick(String input) {
        System.out.println("MOUSE CLICKED!");
        String[] variables = input.split(":");
        float x = Float.parseFloat(variables[1]);
        float y = Float.parseFloat(variables[2]);
        rob.mouseMove((int) x, (int) y);
        rob.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        rob.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }

    private void handleSwipe(String input) {
        if(input.contains("Left")) {
            rob.keyPress(KeyEvent.VK_LEFT);
            rob.keyRelease(KeyEvent.VK_LEFT);
        } else if(input.contains("Right")) {
            rob.keyPress(KeyEvent.VK_RIGHT);
            rob.keyRelease(KeyEvent.VK_RIGHT);
        } else if(input.contains("Up")) {
            rob.keyPress(KeyEvent.VK_UP);
            rob.keyRelease(KeyEvent.VK_UP);
        } else if(input.contains("Down")) {
            rob.keyPress(KeyEvent.VK_DOWN);
            rob.keyRelease(KeyEvent.VK_DOWN);
        }
    }
}