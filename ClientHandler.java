import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.*;
import java.net.Socket;

public class ClientHandler extends Thread {

    final Socket m_socket;
    final Robot rob = new Robot();
    boolean m_exit = false;

    public ClientHandler(Socket socket) throws AWTException {
        m_socket = socket;
    }

    @Override
    public void run() {
        String in;
        try {
            while(m_socket.isConnected() && !m_exit){
                BufferedReader br = new BufferedReader(new InputStreamReader(m_socket.getInputStream()));
                while (br.ready() && ((in = br.readLine()) != null)) {
                    if (in.contains("MouseClick")) {
                       handleMouseClick(in);
                    } else if (in.contains("Swipe")) {
                        handleSwipe(in);
                    } else if (in.contains("exit")) {
                        System.out.println("Client exit");
                        m_exit = true;
                    }
                }
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private void handleMouseClick(String input) {
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