package Model.Edificis.DEMASIADO_COMPLICADO;

import Model.Actions.Action;
import Model.Actions.DestroyMeAction;
import Model.DataContainers.ObjectInfo;
import Model.Edificis.Building;
import Model.Unitats.Unit_Training.Trainable;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * Pared para defender el pueblo!
 */

public class WallTower extends Building {

    public static final int PRICE = 50;
    public static final int SX = 35;
    public static final int SY = 35;
    public static final int HP = 100;

    public WallTower(double x, double y, int team) {
        super(x, y, SX, SY, HP, PRICE, team);

        Action actions[] = new Action[1];
        actions[0] = new DestroyMeAction(this);

        objectInfo = new ObjectInfo(HP, HP, 0, 1, 0, 0, team, "prev_wall.png", actions);
    }

    @Override
    public void update() {

    }

    @Override
    public void render(Graphics2D g) {
        g.setColor(Color.blue);
        g.draw(new Rectangle2D.Double(x - sx / 2.0, y - sy / 2.0, sx, sy));
    }

    @Override
    public void trainCompleted(Trainable t) {

    }

    @Override
    public ObjectInfo getInfo() {
        return objectInfo;
    }
}
