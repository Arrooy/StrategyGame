package Model.Edificis;

import Model.DataContainers.ObjectInfo;
import Model.DataContainers.Trainable;
import Model.MouseSelector;
import Model.UI.Minimap;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Mine extends Building {


    public Mine(double x, double y, double sx, double sy, int hp, int price) {
        super(x, y, sx, sy, hp, price);

        Minimap.add(this);
        MouseSelector.add(this);
        objectInfo = new ObjectInfo(hp, hp, 10, 10, 10, 10, "mine.png");
    }

    @Override
    public void trainCompleted(Trainable t) {

    }

    @Override
    public void update() {

    }

    @Override
    public void render(Graphics2D g) {
        //g.drawImage(AssetManager.getImage("mine.png", (int) sx, (int) sy), (int) x, (int) y, null);

        g.setColor(new Color(0xC8AC33));
        g.fill(new Rectangle2D.Double(x - sx / 2, y - sy / 2, sx, sy));
    }

    @Override
    public ObjectInfo getInfo() {
        return objectInfo;
    }

}
