package Model.Edificis;

import Model.CameraControl.WorldManager;
import Model.DataContainers.ObjectInfo;
import Model.MassiveListManager.Managable;
import Model.Representable;
import Model.Sketch;
import Model.UI.Map.Mappable;
import Model.UI.Mouse_Area_Selection.Selectable;
import Model.Unitats.Entity;
import Model.Unitats.Unit_Training.TrainTask;
import Model.Unitats.Unit_Training.Trainable;
import Utils.TeamColors;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.concurrent.ConcurrentLinkedQueue;

import static Model.Sketch.entityManager;


/**
 * Definicion de un edificio basico.
 * Permite:
 * ->tener un spawn point
 * ->recivir daño
 * ->Reclutar (train) unidades
 * Deja opciones como:
 * -> Finalizacion del Training (Trainable)
 * -> Update(); (Renderitzable)
 * -> Render(); (Renderitzable)
 * Para que cada tipo de edificio defina su comportamiento
 */

public abstract class Building implements Representable, Selectable, Mappable, Managable {

    protected double x, y, sx, sy;
    protected int hp;
    protected int price;

    protected Point2D.Double spawnPoint;

    protected boolean selected = false;
    protected Color c, originalColor;
    private Double key = Sketch.getNewKey();

    protected ConcurrentLinkedQueue<TrainTask<Entity>> trainQueue;

    protected ObjectInfo objectInfo;

    protected boolean isAlive;
    protected Shape shape;
    private int team;

    public Building(double x, double y, double sx, double sy, int hp, int price, int team) {
        this.x = x;
        this.y = y;
        this.sx = sx;
        this.sy = sy;
        this.hp = hp;
        this.price = price;
        this.spawnPoint = new Point2D.Double(x, y);
        this.team = team;
        this.c = TeamColors.getMyColor(team);
        this.originalColor = TeamColors.getMyColor(team);

        trainQueue = new ConcurrentLinkedQueue<>();
        isAlive = true;
    }

    public int getPrice() {
        return price;
    }

    public synchronized boolean getDamage(int damage) {
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


    protected void traingToSpawnPoint(Trainable t) {
        Entity aux = (Entity) t;
        double cx = getSpawnPoint().getX(), cy = getSpawnPoint().getY();
        double dir = Math.atan2(cy - y, cx - x);
        aux.setLocation(x + Math.cos(dir) * (sx + aux.getSizeX() + 2) / 2, y + Math.sin(dir) * (sy + aux.getSizeY() + 2) / 2);
        entityManager.add(aux);
        aux.addObjective(new Point2D.Double(this.getSpawnPoint().getX(), this.getSpawnPoint().getY()), true);
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
            g.drawString(a, (float) (x - fm.stringWidth(a) / 2), (float) (y - gap - 10));
        }
    }

    public void setSpawnPoint(double ox, double oy) {
        if (ox >= x && ox <= x + sx && oy >= y && oy <= y + sy) {
            this.spawnPoint.setLocation(getCenterX() + getWidth() * 1.5, getCenterY());
        } else {
            this.spawnPoint.setLocation(ox, oy);
        }
    }

    public Point2D.Double getSpawnPoint() {
        return spawnPoint;
    }

    public void move(double x, double y) {
        this.x = x + WorldManager.xPos();
        this.y = y + WorldManager.yPos();
    }

    public double getWidth() {
        return sx;
    }

    public double getHeigth() {
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

    public double getKey() {
        return key;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public int getTeam() {
        return team;
    }

    public void setColor(Color c) {
        this.c = c;
    }

    public void restoreOriginalColor() {
        c = originalColor;
    }

    public synchronized boolean heal(int i) {
        boolean fullHeal = false;
        hp += i;
        if (hp > objectInfo.getMaxHp()) {
            hp = objectInfo.getMaxHp();
            fullHeal = true;
        }
        objectInfo.updateHp(hp);
        return fullHeal;
    }
}
