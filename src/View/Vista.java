package View;
import javax.swing.*;
import java.awt.*;

/**
 * Es un JFrame undecored con la capacidad de usar una foto como contenido.
 * Controller modifica la foto pintando encima el contenido del juego.
 */
public class Vista extends JFrame {

    private JPanel gamePanel;

    public Vista(String title) {

        gamePanel = new JPanel();
        gamePanel.setBackground(Color.blue);

        finishConfig(title,gamePanel);
    }

    private void finishConfig(String title, JPanel mainPane){

        add(mainPane,BorderLayout.CENTER);

        setTitle(title);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setUndecorated(true);

        requestFocus();
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device = env.getDefaultScreenDevice();
        device.setFullScreenWindow(this);
    }


    public Graphics getGameGraphics() {
        return gamePanel.getGraphics();
    }


    public JPanel getGamePanel(){
        return gamePanel;
    }
}
