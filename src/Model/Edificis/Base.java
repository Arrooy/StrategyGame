package Model.Edificis;

import Model.Actions.Action;
import Model.Actions.Buildings.RecuitAMiner;
import Model.Actions.DestroyMeAction;
import Model.CShape;
import Model.DataContainers.ObjectInfo;
import Model.UI.Map.Minimap;
import Model.UI.Mouse_Area_Selection.MouseSelector;
import Model.Unitats.Unit_Training.Trainable;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/***
 * Centro de mando de un equipo.
 * Se pueden ampliar las acciones para generar distintos obreros / opciones de gestion del pueblo.
 */

public class Base extends Building {

    public static final int PRICE = 1000;
    public static final int SX = 20;
    public static final int SY = 20;
    public static final int HP = 100;

    public Base(double x, double y, int team) {
        super(x, y, SX, SY, HP, PRICE, team);

        Action actions[] = new Action[2];
        actions[0] = new RecuitAMiner(this);
        actions[1] = new DestroyMeAction(this);

        objectInfo = new ObjectInfo(HP, HP, 0, 1, 0, 0, team, "prev_castle.png", actions);
    }

    public Base(double x, double y, int team, boolean createWithoutBuilingManager) {
        this(x, y, team);

        if (createWithoutBuilingManager) {
            Minimap.add(this);
            MouseSelector.add(this);
        }
    }

    @Override
    public void update() {

    }

    public void trainCompleted(Trainable t) {
        traingToSpawnPoint(t);
    }

    @Override
    public void render(Graphics2D g) {
        g.setColor(selected ? c.darker() : c);
        //g.fill(new Rectangle2D.Double(x - sx / 2.0,y - sy / 2.0,sx,sy));

        g.fill(CShape.base(x, y, sx, sy));

        int[] px = new int[3];
        int[] py = new int[3];
        px[0] = (int) ((int) x - sx / 2);
        py[0] = (int) ((int) y + sy / 2);

        px[1] = (int) (x + sx / 2);
        py[1] = (int) (y + sy / 2);

        px[2] = (int) (x);
        py[2] = (int) (y - sy / 2);
        g.fillPolygon(new Polygon(px, py, 3));


        if(selected){
            g.setColor(Color.blue);
            g.draw(new Rectangle2D.Double(spawnPoint.getX() - 2.5,spawnPoint.getY() - 2.5,5,5));
        }
    }

    @Override
    public ObjectInfo getInfo() {
        return objectInfo;
    }
}
