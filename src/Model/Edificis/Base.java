package Model.Edificis;

import Model.DataContainers.Action;
import Model.DataContainers.ObjectInfo;
import Model.DataContainers.RecuitAMiner;
import Model.DataContainers.Trainable;
import Model.Unitats.Entity;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import static Model.Sketch.entityManager;

public class Base extends Building {

    public static final int price = 200;


    public Base(double x, double y, double sx, double sy, int hp) {
        super(x, y, sx, sy, hp, price);

        Action actions [] = new Action[1];
        actions[0] = new RecuitAMiner(this);

        objectInfo = new ObjectInfo(hp, 10, 10, 10, 10, 0, "prev_castle.png", actions);
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
        g.fill(new Rectangle2D.Double(x - sx / 2.0,y - sy / 2.0,sx,sy));
        // g.fill(new Ellipse2D.Double(x - sx / 2.0,y - sy / 2.0,sx,sy));
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
