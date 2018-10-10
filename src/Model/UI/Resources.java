package Model.UI;

import Model.Sketch;
import Model.UI.FX.AnimatedText;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static Controlador.Controller.gameHeight;

public class Resources {

    private static int gold;

    private static double w, y;
    private static double cx, cy;

    public static void init() {
        gold = 500;
        w = gameHeight / 5.0;
        y = gameHeight - w;
    }

    public synchronized static void add(int inc){
        gold += inc;
        String info = (inc > 0 ? "+ " : "") + inc;
        Sketch.fxTextManager.add(new AnimatedText((int) cx + info.length() * 2, (int) cy, info));
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
        cx = (5 + fm.stringWidth("G:" + gold)) / 2.0;
        cy = y - fm.getAscent() * 2.0;
    }
}
