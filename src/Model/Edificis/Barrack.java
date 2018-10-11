package Model.Edificis;

import Model.Actions.Action;
import Model.Actions.Buildings.RecuitAWarrior;
import Model.Actions.DestroyMeAction;
import Model.DataContainers.ObjectInfo;
import Model.Unitats.Unit_Training.Trainable;

import java.awt.*;
import java.awt.geom.Rectangle2D;


/***
 * Barraca de soldados. Pensada para generar unidades de cuerpo a cuerpo
 * Se pueden ampliar las acciones para generar distintos soldados.
 */

public class Barrack extends Building {

    public static final int PRICE = 500;
    public static final int SX = 10;
    public static final int SY = 10;
    public static final int HP = 100;

    public Barrack(double x, double y, int team) {
        super(x, y, SX, SY, HP, PRICE, team);

        Action actions[] = new Action[2];
        actions[0] = new RecuitAWarrior(this);
        actions[1] = new DestroyMeAction(this);


        objectInfo = new ObjectInfo(HP, HP, 0, 1, 0, 0, team, "prev_barrack.png", actions);
    }

    @Override
    public void update() {

    }

    @Override
    public void render(Graphics2D g) {
        g.setColor(selected ? c.darker() : c);
        g.fill(new Rectangle2D.Double(x - sx / 2.0, y - sy / 2.0, sx, sy));

        if (selected) {
            g.setColor(Color.blue);
            g.draw(new Rectangle2D.Double(spawnPoint.getX() - 2.5, spawnPoint.getY() - 2.5, 5, 5));
        }
    }

    @Override
    public void trainCompleted(Trainable t) {
        traingToSpawnPoint(t);
    }

    @Override
    public ObjectInfo getInfo() {
        return objectInfo;
    }
}
