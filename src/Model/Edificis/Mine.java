package Model.Edificis;

import Model.DataContainers.ObjectInfo;
import Model.DataContainers.Trainable;
import Model.MouseSelector;
import Model.UI.Minimap;
import Model.Unitats.Miner;
import Utils.AssetManager;

import java.awt.*;

public class Mine extends Building {

    private ObjectInfo objectInfo;


    public Mine(double x, double y, double sx, double sy, int hp, int price) {
        super(x, y, sx, sy, hp, price);

        Minimap.add(this);
        MouseSelector.add(this);
        objectInfo = new ObjectInfo(hp, 1000, 10, 10, 10, 10, "mine.png");
    }

    @Override
    public void trainComplete(Trainable t) {

    }

    @Override
    public void update() {

    }

    @Override
    public void render(Graphics2D g) {
        g.drawImage(AssetManager.getImage("mine.png", (int) sx, (int) sy), (int) x, (int) y, null);
    }

    @Override
    public ObjectInfo getInfo() {
        return objectInfo;
    }

    public void harvest(Miner miner) {

    }
}
