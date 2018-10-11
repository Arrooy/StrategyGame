package Model;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * Genera shapes complejas para renderizar con Graphics2D.
 * Seguramente va a ser suplandada por un conjunto de classes de gestion de sprites y animaciones
 */

//TODO: Generar el package de geston de sprites i animaciones. (Threads por un tubo, animator i mejora necesaria de AssetManager)

public class CShape {

    public static Shape mine(double x, double y, double sx, double sy) {

        return new Rectangle2D.Double(x - sx / 2, y - sy / 2, sx, sy);
    }

    public static Shape base(double x, double y, double sx, double sy) {
        int[] px = new int[3];
        int[] py = new int[3];
        px[0] = (int) ((int) x - sx / 2);
        py[0] = (int) ((int) y - sy / 2);

        px[1] = (int) (x + sx / 2);
        py[1] = (int) (y - sy / 2);

        px[2] = (int) (x);
        py[2] = (int) (y + sy / 2);
        return new Polygon(px, py, 3);
    }

    public static Shape miner(double x, double y, double s) {
        return new Rectangle2D.Double(x - s / 2, y - s / 2, s, s);
    }

    public static Shape warrior(double x, double y, double s) {
        int[] px = new int[3];
        int[] py = new int[3];
        px[0] = (int) ((int) x - s / 2);
        py[0] = (int) ((int) y - s / 2);

        px[1] = (int) (x + s / 2);
        py[1] = (int) (y - s / 2);

        px[2] = (int) (x);
        py[2] = (int) (y + s / 2);

        return new Polygon(px, py, 3);
    }

    public static Shape archer(double x, double y, double s) {
        return new Rectangle2D.Double(x - s / 2, y - s / 2, s, s);
    }

}
