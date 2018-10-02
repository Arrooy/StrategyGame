package Model.Edificis;

import Model.DataContainers.ObjectInfo;
import Utils.AssetManager;

import java.awt.*;

public class Mine extends Building {

    public Mine(double x, double y, double sx, double sy, int hp, int price) {
        super(x, y, sx, sy, hp, price);
    }

    @Override
    public void update() {

    }

    @Override
    public void render(Graphics2D g) {
        //g.drawImage(AssetManager.getImage("mine.png",s,s));
    }

    @Override
    public ObjectInfo getInfo() {
        return new ObjectInfo(1,1,1,1,1,1,"mine.png");
    }
}
