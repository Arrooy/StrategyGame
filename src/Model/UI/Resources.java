package Model.UI;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static Controlador.Controller.gameHeight;

public class Resources {

    private static int gold;

    public static void init() {
        gold = 500;
    }

    public synchronized static void add(int inc){
        gold += inc;
    }

    public static boolean canAfford(int value){
        return gold - value > 0;
    }

    public static void render(Graphics2D g){
        g.setFont(new Font("Arial",Font.PLAIN,15));
        FontMetrics fm = g.getFontMetrics();
        double w = gameHeight / 5.0, y = gameHeight - w;
        g.setColor(Color.black);
        g.draw(new Rectangle2D.Double(0,y - fm.getAscent() * 2,w,fm.getAscent() * 2));
        g.drawString("G:" + gold,5, (float) (y - fm.getAscent() / 2));
    }
}
