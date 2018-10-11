package Model.Unitats;

import Model.Actions.Action;
import Model.Actions.BuildABarrack;
import Model.Actions.BuildABase;
import Model.Actions.DestroyMeAction;
import Model.CShape;
import Model.DataContainers.ObjectInfo;
import Model.Edificis.Base;
import Model.Edificis.Building;
import Model.Edificis.Mine;
import Utils.TeamColors;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import static Controlador.Controller.gameWidth;
import static Model.Sketch.buildingsManager;
import static Model.Sketch.minimapManager;

/**
 * Unidad basica del juego.
 * Minero puede construir edificios y recojer recursos.
 * Al tocar una base del mismo equipo, se depositan todos los recursos recolectados.
 * Permite una recoleccion automatica de minerales.
 */


//TODO: Mejorar la recoleccion automatica, actualmente aparecen muchos errores de user experience al borrar la lista
//Todo: de objetivos para ir a la mina. Teoricamente he apañado(adria) la gestion de la mina hacia la base, pero falta
//Todo: mejorarlo i añadir la direccion contraria.

public class Miner extends Entity {

    public static final int price = 100;
    private static final int TRAINING_TIME = 500;
    private static final long HARVEST_TIME = 2000;
    private static final int HARVEST_AMOUNT = 10;

    private double attSpeed = 1;
    private int def = 0;
    private static final int maxHp = 1000;
    private int dmg = 1;
    private String img;
    private ObjectInfo objectInfo;

    private Color c;

    private Action[] actions;

    private long lastHarvest;
    private Mine actualMine;
    private Mine lastMine;

    private int goldInHand;

    public Miner(double x, double y, double maxSpeed, double maxAccel, int team) {
        super(x, y, maxSpeed, maxAccel, maxHp, team);

        actions = new Action[3];
        actions[0] = new BuildABase(this);
        actions[1] = new BuildABarrack(this);
        actions[2] = new DestroyMeAction(this);
        img = "prev_miner.png";

        goldInHand = 0;
        c = TeamColors.getMyColor(team);

        objectInfo = new ObjectInfo(maxHp, maxHp, dmg, def, attSpeed, maxSpeed, team, img, actions);
        lastHarvest = 0;
    }

    @Override
    public void update() {
        if (actualMine != null && System.currentTimeMillis() - lastHarvest >= HARVEST_TIME) {
            if (actualMine.isAlive()) {
                int hp = actualMine.getInfo().getHp();

                if (actualMine.getDamage(HARVEST_AMOUNT)) {
                    goldInHand = hp;
                    buildingsManager.remove(actualMine);
                    minimapManager.remove(actualMine);
                    lastMine = null;
                } else {
                    if (objList.isEmpty()) {
                        double minDist = gameWidth * 2;
                        Base minDistBase = null;
                        for (Building b : buildingsManager.getObjects()) {
                            if (b instanceof Base && b.getTeam() == team) {
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
                    goldInHand = HARVEST_AMOUNT;
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
        g.fill(CShape.miner(x, y, s));

        if (goldInHand != 0) {
            g.setColor(new Color(200, 171, 0));
            g.fill(new Rectangle2D.Double(x, y, s / 2, s / 2));
        }

        if (actualMine != null) {
            double maxTrainSize = s + 30;
            int height = 5;
            double gap = s / 2.0 + 5;
            long trainingTime = HARVEST_TIME;
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
        return TRAINING_TIME;
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

        if (lastMine != null && lastMine.isAlive()) {
            addObjective(new Point2D.Double(lastMine.getCenterX(), lastMine.getCenterY()));
        }
        return aux;
    }
}
