package View;
import javax.swing.*;
import java.awt.*;

/**
 * Es un JFrame undecored con la capacidad de usar una foto como contenido.
 * Controller modifica la foto pintando encima el contenido del juego.
 */
public class Vista extends JFrame {

    private int width,height;

    private JPanel gamePanel;

    public Vista(int width, int height, String title){
        this.width = width;
        this.height = height;

        gamePanel = new JPanel();
        gamePanel.setBackground(Color.blue);

        finishConfig(title,gamePanel);
    }

    private void finishConfig(String title, JPanel mainPane){

        add(mainPane,BorderLayout.CENTER);

        setTitle(title);

        setMinimumSize(new Dimension(width,height));

        setSize(new Dimension(width,height));

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        requestFocus();
        setUndecorated(true);
        centerInScreen();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }


    public Graphics getGameGraphics() {
        return gamePanel.getGraphics();
    }

    public void centerInScreen() {
        Rectangle screenSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        setLocation(screenSize.width / 2 - width / 2,screenSize.height / 2 - height / 2);
    }

    public JPanel getGamePanel(){
        return gamePanel;
    }
}
