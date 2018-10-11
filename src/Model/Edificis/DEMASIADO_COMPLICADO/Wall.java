package Model.Edificis.DEMASIADO_COMPLICADO;

import Model.Actions.Action;
import Model.Actions.DestroyMeAction;
import Model.CameraControl.WorldManager;
import Model.DataContainers.ObjectInfo;
import Model.Edificis.Building;
import Model.Unitats.Unit_Training.Trainable;

import java.awt.*;
import java.awt.geom.Line2D;

public class Wall extends Building {

    public static final int PRICE = 50;
    public static final int SX = 20;
    public static final int SY = 20;
    public static final int HP = 75;

    private double fx, fy;

    public Wall(double x, double y, double fx, double fy, int team) {
        super(x, y, SX, SY, HP, PRICE, team);

        this.fx = fx;
        this.fy = fy;

        Action actions[] = new Action[1];
        actions[0] = new DestroyMeAction(this);

        objectInfo = new ObjectInfo(HP, HP, 0, 1, 0, 0, team, "prev_wall.png", actions);
    }

    @Override
    public void update() {

    }

    @Override
    public void render(Graphics2D g) {
        g.setColor(Color.red);
        g.draw(new Line2D.Double(x, y, fx, fy));
    }

    @Override
    public void trainCompleted(Trainable t) {

    }

    @Override
    public ObjectInfo getInfo() {
        return objectInfo;
    }


    //TODO:REDO!
    @Override
    public double getLX() {
        return x - WorldManager.xPos();
    }

    @Override
    public double getRX() {
        return fx - WorldManager.xPos();
    }

    @Override
    public double getUY() {
        return y - WorldManager.yPos();
    }

    @Override
    public double getLY() {
        return fy - WorldManager.yPos();
    }

}
