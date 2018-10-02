package Controlador;

import Model.Representable;
import Model.Sketch;
import Model.UI.Minimap;
import View.Vista;

import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
public class Controller extends Thread implements KeyListener, MouseInputListener{

    public static int UPDATE_DELAY  = 20; //espera dels upadte en ms

    private Vista vista;

    private Image gameImage;

    public static double mouseX,mouseY;

    public static int gameWidth,gameHeight;

    private boolean gameStarted;

    private Sketch r;

    public Controller(Vista vista, Sketch r) {
        this.vista = vista;
        gameStarted = true;
        this.r = r;
    }

    private void render(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

        g.setColor(Color.white);
        g.setColor(new Color(255,255,255));
        g.fillRect(0,0,gameWidth,gameHeight);
        r.render(g);
    }

    public void frameSizeChanged(int w,int h){
        //RESIZE

        gameWidth = w;
        gameHeight = h;

        gameImage = vista.getGamePanel().createImage(gameWidth, gameHeight);
        Minimap.updateSize();

        System.gc();
    }

    public void exit(int status){

        vista.dispose();
        interrupt();
        System.exit(status);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
    @Override
    public void keyPressed(KeyEvent e) {
        r.keyPressed(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        r.keyReleased(e.getKeyChar());
    }

    @Override
    public void run() {

        // Aquestes variables haurien de sumar 17 sempre
        long updateDurationMillis = 0; // Mesura el temps d'execució d'updte() i render()
        long sleepDurationMillis = 0; // Mesura el temps que hade dormir la iteració per ajustar-se

        mouseX = gameWidth / 2;
        mouseY = gameHeight / 2;
        //Bucle principal d'actualització i renderització del panell
        try {

            //Aquest joc es super obert, podriem fer coses molt guapas amb IA i demes...
            //De moment falta optimitzar algunes coses, pro funcioiona bastant follat.
            //Quan acabem controls li fas un ojaso i ens enxixem eh
            while (true) {

                //S'inicien els temps de la iteració
                long beforeUpdateRender = System.nanoTime();

                render((Graphics2D) gameImage.getGraphics());

                Graphics gameGraphics = vista.getGameGraphics();

                gameGraphics.drawImage(gameImage, 0, 0, null);

                Toolkit.getDefaultToolkit().sync();

                gameGraphics.dispose();

                //Es calcula la durada de U&R i el temps de repos
                updateDurationMillis = (System.nanoTime() - beforeUpdateRender) / 1000000L;
                sleepDurationMillis = Math.max(2, 17 - updateDurationMillis);

                Thread.sleep(sleepDurationMillis);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        r.mousePressed(e.getButton());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        r.mouseReleased(e.getButton());
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if(gameStarted) {
            mouseX = e.getPoint().getX();
            mouseY = e.getPoint().getY();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if(gameStarted) {
            mouseX = e.getPoint().getX();
            mouseY = e.getPoint().getY();
        }
    }
}
