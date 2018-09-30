package View;
import javax.swing.*;
import java.awt.*;

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

    private void finishConfig(String title,JPanel mainPane){

        add(mainPane,BorderLayout.CENTER);

        setTitle(title);

        setMinimumSize(new Dimension(width,height));

        setSize(new Dimension(width,height));

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        requestFocus();
        setUndecorated(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        centerInScreen();
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
