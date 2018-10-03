package Model.Unitats;

import Model.DataContainers.Action;
import Model.DataContainers.BuildABaseAction;
import Model.DataContainers.ObjectInfo;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Miner extends Entity {

    private double attSpeed = 1;
    private int def = 0;
    private int hp = 2;
    private int maxHp = 2;
    private int dmg = 0;
    private String img;
    private ObjectInfo objectInfo;

    private Color c = new Color(50,180,80);;

    private Action[] actions;

    public Miner(double x, double y, double maxSpeed, double maxAccel) {
        super(x, y, maxSpeed, maxAccel);

        actions = new Action[1];
        actions[0] = new BuildABaseAction(this);

        img = "prev_miner.png";

        objectInfo = new ObjectInfo(hp,maxHp,dmg,def,attSpeed,maxSpeed, img,actions);
    }

    @Override
    public void render(Graphics2D g) {
        objList.forEach((n,p)->{
            g.setColor(Color.red);
            g.draw(new Rectangle2D.Double(p.getX() - s / 2,p.getY() - s / 2,s,s));
        });

        g.setColor(selected ? c.darker() : c);
        g.fill(new Rectangle2D.Double(x - s / 2,y - s/2,s,s));
    }

    @Override
    public ObjectInfo getInfo() {
        return objectInfo;
    }

    @Override
    public Color getMapColor() {
        return c;
    }

    @Override
    public long getTrainingTime() {
        return 1000;
    }
}
