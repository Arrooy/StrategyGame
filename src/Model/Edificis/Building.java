package Model.Edificis;

import Model.DataContainers.ObjectInfo;
import Model.DataContainers.TrainTask;
import Model.DataContainers.Trainable;
import Model.*;
import Model.UI.Mappable;
import Model.Unitats.Entity;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class Building implements Representable, Selectable, Mappable, Managable{

    protected double x,y,sx,sy;
    protected int hp;
    protected int price;

    protected Point2D.Double spawnPoint;

    protected boolean selected = false;
    protected Color c = new Color(255,0,255);
    private Double key = Sketch.getNewKey();

    protected ConcurrentLinkedQueue<TrainTask<Entity>> trainQueue;

    protected ObjectInfo objectInfo;

    protected boolean isAlive;

    public Building(double x, double y, double sx, double sy, int hp, int price) {
        this.x = x;
        this.y = y;
        this.sx = sx;
        this.sy = sy;
        this.hp = hp;
        this.price = price;
        this.spawnPoint = new Point2D.Double(x, y);
        trainQueue = new ConcurrentLinkedQueue<>();
        isAlive = true;
    }

    public int getPrice() {
        return price;
    }

    public synchronized boolean getDamage(int damage){
        hp -= damage;
        objectInfo.updateHp(hp);
        if (hp <= 0) isAlive = false;
        return hp <= 0;
    }

    @Override
    public abstract void update();

    @Override
    public abstract void render(Graphics2D g);

    public abstract void trainCompleted(Trainable t);

    public void baseUpdate() {
        update();
        trainProgressBar();
    }

    public void baseRender(Graphics2D g) {
        render(g);
        trainProgressBar(g);
    }

    public void train(Trainable t) {
        trainQueue.add(new TrainTask(t));
    }

    public void trainProgressBar() {
        if (!trainQueue.isEmpty()) {
            TrainTask tt = trainQueue.peek();
            if (System.currentTimeMillis() - tt.getLastTrain() >= tt.getTrainingTime()) {
                trainCompleted(tt.getTrainResult());
                trainQueue.remove();
                if (!trainQueue.isEmpty()) trainQueue.peek().initTrain();
            }
        }
    }

    public void trainProgressBar(Graphics2D g) {
        if (!trainQueue.isEmpty()) {
            TrainTask aux = trainQueue.peek();
            double maxTrainSize = sx + 30;
            int height = 5;
            double gap = sy / 2.0 + 5;
            long trainingTime = aux.getTrainingTime();
            long lastTrain = aux.getLastTrain();


            g.setColor(Color.gray);
            g.fill(new Rectangle2D.Double(x - maxTrainSize / 2, y - gap - height, maxTrainSize, height));

            g.setColor(Color.blue);
            g.fill(new Rectangle2D.Double(x - maxTrainSize / 2, y - gap - height, (System.currentTimeMillis() - lastTrain) * maxTrainSize / trainingTime, height));

            g.setFont(new Font("Arial", Font.PLAIN, 10));
            FontMetrics fm = g.getFontMetrics();
            String a = "" + trainQueue.size();
            g.setColor(Color.black);
            g.drawString(a, (float) (x - fm.stringWidth(a)), (float) (y - gap - 10));
        }
    }

    public void setSpawnPoint(double ox, double oy){
        if (ox >= x && ox <= x + sx && oy >= y && oy <= y + sy) {
            this.spawnPoint.setLocation(getCenterX() + getWidth() * 1.5, getCenterY());
        } else {
            this.spawnPoint.setLocation(ox, oy);
        }
    }

    public Point2D.Double getSpawnPoint(){
        return spawnPoint;
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

    public Double getKey() {
        return key;
    }

    public boolean isAlive() {
        return isAlive;
    }
}
