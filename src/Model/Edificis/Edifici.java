package Model.Edificis;

import Model.Representable;
import Model.Selectable;
import Model.UI.Mappable;
import Model.DataContainers.ObjectInfo;
import Model.WorldManager;

import java.awt.*;
import java.awt.geom.Point2D;

public abstract class Edifici implements Representable, Selectable, Mappable {

    protected double x,y,sx,sy;
    protected int hp;
    protected int price;

    protected Point2D.Double spawnPoint;

    protected boolean selected = false;
    protected Color c = new Color(255,0,255);

    public Edifici(double x, double y, double sx, double sy, int hp,int price) {
        this.x = x;
        this.y = y;
        this.sx = sx;
        this.sy = sy;
        this.hp = hp;
        this.price = price;
        this.spawnPoint = new Point2D.Double(0,0);
    }

    public int getPrice() {
        return price;
    }

    public synchronized boolean getDamage(int damage){
        hp -= damage;
        return hp <= 0;
    }

    public void setSpawnPoint(double ox, double oy){
        this.spawnPoint.setLocation(ox,oy);
    }

    public void move(double x, double y){
        this.x = x + WorldManager.xPos();
        this.y = y + WorldManager.yPos();
    }

    public double getWidth(){
        return sx;
    }

    public double getHeigth(){
        return sy;
    }

    @Override
    public abstract void update();

    @Override
    public abstract void render(Graphics2D g);

    @Override
    public double getCenterX() {
        return x;
    }

    @Override
    public double getCenterY() {
        return y;
    }

    @Override
    public double getMapSizeX() {
        return 4;
    }

    @Override
    public double getMapSizeY() {
        return 4;
    }

    @Override
    public Color getMapColor() {
        return c;
    }

    @Override
    public double getLX() {
        return x - sx / 2.0 - WorldManager.xPos();
    }

    @Override
    public double getRX() {
        return x + sx / 2.0 - WorldManager.xPos();
    }

    @Override
    public double getUY() {
        return y - sy / 2.0 - WorldManager.yPos();
    }

    @Override
    public double getLY() {
        return y + sy / 2.0 - WorldManager.yPos();
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public void setSelected(boolean state) {
        selected = state;
    }

    @Override
    public abstract ObjectInfo getInfo();
}
