package Model.Edificis;

import Model.Actions.Action;
import Model.Actions.RecuitAMiner;
import Model.CShape;
import Model.DataContainers.ObjectInfo;
import Model.UI.Map.Minimap;
import Model.UI.Mouse_Area_Selection.MouseSelector;
import Model.Unitats.Entity;
import Model.Unitats.Unit_Training.Trainable;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import static Model.Sketch.entityManager;

public class Base extends Building {

    public static final int PRICE = 100;
    public static final int SX = 20;
    public static final int SY = 20;
    public static final int HP = 100;

    public Base(double x, double y, int team) {
        super(x, y, SX, SY, HP, PRICE, team);

        Action actions [] = new Action[1];
        actions[0] = new RecuitAMiner(this);

        objectInfo = new ObjectInfo(HP, HP, 0, 1, 0, 0, "prev_castle.png", actions);
    }

    public Base(double x, double y, int team, boolean createWithoutBuilingManager) {
        this(x, y, team);

        if (createWithoutBuilingManager) {
            Minimap.add(this);
            MouseSelector.add(this);
        }
    }

    private double dist(double x, double y, Point2D p) {
        return Math.sqrt(Math.pow(x - p.getX(), 2) + Math.pow(y - p.getY(), 2));
    }
    @Override
    public void update() {

    }

    public void trainCompleted(Trainable t) {
        Entity aux = (Entity) t;
        double cx = getSpawnPoint().getX(), cy = getSpawnPoint().getY();
        double dir = Math.atan2(cy - y, cx - x);
        aux.setLocation(x + Math.cos(dir) * (sx + aux.getSizeX() + 2) / 2, y + Math.sin(dir) * (sy + aux.getSizeY() + 2) / 2);
        entityManager.add(aux);
        aux.addObjective(new Point2D.Double(this.getSpawnPoint().getX(), this.getSpawnPoint().getY()));
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
