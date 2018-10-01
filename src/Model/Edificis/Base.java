package Model.Edificis;

import Model.DataContainers.ObjectInfo;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Base extends Edifici {

    public static final int price = 200;

    public Base(double x, double y, double sx, double sy, int hp) {
        super(x, y, sx, sy, hp, price);
    }

    @Override
    public void update() {

    }

    @Override
    public void render(Graphics2D g) {
        g.setColor(selected ? c.darker() : c);
        g.fill(new Rectangle2D.Double(x - sx / 2.0,y - sy / 2.0,sx,sy));
        if(selected){
            g.setColor(Color.blue);
            g.draw(new Rectangle2D.Double(spawnPoint.getX() - 2.5,spawnPoint.getY() - 2.5,5,5));
        }
    }

    @Override
    public ObjectInfo getInfo() {
        return new ObjectInfo(hp,10,10,10,10,10,"prev_castle.png");
    }
}
