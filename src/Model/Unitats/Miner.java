package Model.Unitats;

import Model.DataContainers.Action;
import Model.DataContainers.BuildABaseAction;
import Model.DataContainers.ObjectInfo;
import Model.Edificis.Base;
import Model.Edificis.Building;
import Model.Edificis.Mine;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import static Controlador.Controller.gameWidth;
import static Model.Sketch.buildingsManager;
import static Model.Sketch.minimapManager;

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

    private long lastHarvest;
    private Mine actualMine;
    private Mine lastMine;

    private int goldInHand;
    private long harvestTime = 2000;
    private int harvestAmount = 10;

    public Miner(double x, double y, double maxSpeed, double maxAccel) {
        super(x, y, maxSpeed, maxAccel);

        actions = new Action[1];
        actions[0] = new BuildABaseAction(this);

        img = "prev_miner.png";

        goldInHand = 0;
        objectInfo = new ObjectInfo(hp,maxHp,dmg,def,attSpeed,maxSpeed, img,actions);
        lastHarvest = 0;
    }

    @Override
    public void update() {
        if (actualMine != null && System.currentTimeMillis() - lastHarvest >= harvestTime) {
            if (actualMine.isAlive()) {
                int hp = actualMine.getInfo().getHp();

                if (actualMine.getDamage(harvestAmount)) {
                    goldInHand = hp;
                    buildingsManager.remove(actualMine);
                    minimapManager.remove(actualMine);


                    lastMine = null;
                } else {
                    if (objList.isEmpty()) {
                        double minDist = gameWidth * 2;
                        Base minDistBase = null;
                        for (Building b : buildingsManager.getObjects()) {
                            if (b instanceof Base) {
                                double distance = dist(b.getCenterX(), b.getCenterY(), x, y);
                                if (distance < minDist) {
                                    minDist = distance;
                                    minDistBase = (Base) b;
                                }
                            }
                        }
                        if (minDistBase != null) {
                            addObjective(new Point2D.Double(minDistBase.getCenterX(), minDistBase.getCenterY()));
                        }
                    }
                    goldInHand = harvestAmount;
                    lastMine = actualMine;
                }

            } else {
                lastMine = null;
            }
            cantMoveImLoading = false;
            actualMine = null;
        }
    }

    @Override
    public void render(Graphics2D g) {
        objList.forEach((n,p)->{
            g.setColor(Color.red);
            g.draw(new Rectangle2D.Double(p.getX() - s / 2,p.getY() - s / 2,s,s));
        });

        g.setColor(selected ? c.darker() : c);
        g.fill(new Rectangle2D.Double(x - s / 2,y - s/2,s,s));

        if (goldInHand != 0) {
            g.setColor(new Color(200, 171, 0));
            g.fill(new Rectangle2D.Double(x, y, s / 2, s / 2));
        }

        if (actualMine != null) {
            double maxTrainSize = s + 30;
            int height = 5;
            double gap = s / 2.0 + 5;
            long trainingTime = harvestTime;
            long lastTrain = lastHarvest;


            g.setColor(Color.gray);
            g.fill(new Rectangle2D.Double(x - maxTrainSize / 2, y - gap - height, maxTrainSize, height));

            g.setColor(Color.blue);
            g.fill(new Rectangle2D.Double(x - maxTrainSize / 2, y - gap - height, (System.currentTimeMillis() - lastTrain) * maxTrainSize / trainingTime, height));
        }
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

    public void harvest(Mine mine) {
        if (goldInHand == 0) {
            actualMine = mine;
            lastHarvest = System.currentTimeMillis();
            cantMoveImLoading = true;
        }
    }

    public int getGold() {
        int aux = goldInHand;
        goldInHand = 0;
        if (lastMine != null && lastMine.isAlive())
            addObjective(new Point2D.Double(lastMine.getCenterX(), lastMine.getCenterY()));
        return aux;
    }
}
