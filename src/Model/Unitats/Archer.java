package Model.Unitats;

import Model.Actions.Action;
import Model.Actions.DestroyMeAction;
import Model.CShape;
import Model.CameraControl.WorldManager;
import Model.DataContainers.ObjectInfo;
import Model.UI.FX.AnimatedText;
import Utils.TeamColors;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static Model.Sketch.*;


//ALERT: NO ESTA ACABADA!!! NO LA USIS!

//TODO: He notado (adria) que Archer i Warrior comparten un comportamiento bastante parecido de
//TODO: localizacion i gestion de target. Seguramente es buena idea generalizar
public class Archer extends Entity {

    public static final int price = 150;

    private static final int TRAINING_TIME = 500;
    private static final int VISION_RANGE = 200;
    private static final int ATTACK_RANGE = 200;
    private final static int maxHp = 1;

    private double attSpeed = 5;
    private int def = 1;
    private int dmg = 1;
    private String img;
    private ObjectInfo objectInfo;

    private Color c;
    private Action[] actions;

    private Entity target;
    private long lastAttack;
    private double heading;


    public Archer(double x, double y, double maxSpeed, double maxAccel, int team) {
        super(x, y, maxSpeed, maxAccel, maxHp, team);

        img = "prev_archer.png";

        c = TeamColors.getMyColor(team);
        actions = new Action[1];
        actions[0] = new DestroyMeAction(this);

        objectInfo = new ObjectInfo(maxHp, maxHp, dmg, def, attSpeed, maxSpeed, team, img, actions);
        target = null;
        lastAttack = 0;
        heading = 0;
    }

    @Override
    public void update() {
        if (target == null) {
            //No hi ha target, busquem un
            for (Entity e : entityManager.getObjects()) {
                if (e.getTeam() != team && dist(this, e) <= VISION_RANGE / 2) {
                    //Es un enemic i esta en rango.
                    target = e;
                    break;
                }
            }
        } else {
            //Target localitzat, ens dirigim a atacarlo
            double distance = dist(this, target);
            if (distance <= VISION_RANGE / 2) {
                heading = Math.atan2(target.getCenterY() - getCenterY(), target.getCenterX() - getCenterX());
                double tx = target.getCenterX() - target.getSizeX() * Math.cos(heading);
                double ty = target.getCenterY() - target.getSizeY() * Math.sin(heading);
/*
                if (!objList.isEmpty())
                    updateActualObjective(new Point2D.Double(tx, ty));
                else
                    addObjective(new Point2D.Double(tx, ty));
                */
                if (distance <= ATTACK_RANGE / 2) {
                    if (System.currentTimeMillis() - lastAttack >= attSpeed) {
                        int realDamage = target.calculateRealDamage(dmg);
                        boolean isDead = target.getDamage(realDamage);
                        if (isDead) {
                            entityManager.remove(target);
                            minimapManager.remove(target);
                            heading = 0;
                            target = null;
                        }
                        fxTextManager.add(new AnimatedText((int) x - WorldManager.xPos(), (int) y - WorldManager.yPos(), "-" + realDamage));

                        lastAttack = System.currentTimeMillis();
                    }
                }
            } else {
                target = null;
            }
        }
    }

    @Override
    public void render(Graphics2D g) {
        objList.forEach((n, p) -> {
            g.setColor(Color.red);
            g.draw(new Rectangle2D.Double(p.getX() - s / 2, p.getY() - s / 2, s, s));
        });

        g.setColor(selected ? c.darker() : c);
        g.rotate(heading);
        g.fill(CShape.archer(x, y, s));
        g.rotate(-heading);
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
}
