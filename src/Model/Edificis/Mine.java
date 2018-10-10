package Model.Edificis;

import Model.CShape;
import Model.DataContainers.ObjectInfo;
import Model.UI.Map.Minimap;
import Model.UI.Mouse_Area_Selection.MouseSelector;
import Model.Unitats.Unit_Training.Trainable;

import java.awt.*;

public class Mine extends Building {

    public static final int PRICE = 0;
    public static final int SX = 20;
    public static final int SY = 20;
    public static final int HP = 10000;

    public Mine(double x, double y) {
        super(x, y, SX, SY, HP, PRICE, 0);

        Minimap.add(this);
        MouseSelector.add(this);
        objectInfo = new ObjectInfo(HP, HP, 0, 0, 0, 0, "mine.png");
    }

    @Override
    public void trainCompleted(Trainable t) {

    }

    @Override
    public void update() {

    }

    @Override
    public void render(Graphics2D g) {

        g.setColor(c);
        g.fill(CShape.mine(x, y, sx, sy));
    }

    @Override
    public ObjectInfo getInfo() {
        return objectInfo;
    }

}
