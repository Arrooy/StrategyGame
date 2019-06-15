package Model.Unitats;

import Model.Actions.Action;
import Model.Actions.DestroyMeAction;
import Model.Animations.Character;
import Model.CShape;
import Model.CameraControl.WorldManager;
import Model.DataContainers.ObjectInfo;
import Model.UI.FX.AnimatedText;
import Utils.TeamColors;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import static Model.Sketch.*;


/**
 * Unidad basica de lucha cuerpo a cuerpo.
 * -> Tiene un algoritmo de busqueda de objetivos por proximidad.
 * -> Ataca a los objetivos fijados que esten a su rango acercandose i actualizando la ubicacion del objetivo
 * en real time.
 */

//TODO: LOS WARRIOR SE QUEDAN ATACANDO AL OBJECTIVO HASTA QUE LO MATAN.
//TODO: MIENTRAS TENGAN OBJECTIVO, IGNORAN LAS ORDENES DEL USUARIO Y NO CUNDE.

public class Warrior extends Entity {

    private static final int TRAINING_TIME = 500;
    private static final int VISION_RANGE = 200;
    private static final int ATTACK_RANGE = 50;
    public static final int price = 100;
    private final static int maxHp = 2;

    private double attSpeed = 5;
    private int def = 1;
    private int dmg = 1;
    private String img;
    private ObjectInfo objectInfo;

    private Color c;
    private Action[] actions;


    private Entity target;
    private long lastAttack;


    public Warrior(double x, double y, double maxSpeed, double maxAccel, int team) {
        super(x, y, maxSpeed, maxAccel, maxHp, team);

        img = "prev_warrior.png";

        c = TeamColors.getMyColor(team);

        character = new Character(500, 100, "miner_body.png");

        actions = new Action[1];
        actions[0] = new DestroyMeAction(this);

        objectInfo = new ObjectInfo(maxHp, maxHp, dmg, def, attSpeed, maxSpeed, team, img, actions);
        target = null;
        lastAttack = 0;
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
                double angle = Math.atan2(target.getCenterY() - getCenterY(), target.getCenterX() - getCenterX());
                double tx = target.getCenterX() - target.getSizeX() * Math.cos(angle);
                double ty = target.getCenterY() - target.getSizeY() * Math.sin(angle);

                if (!objList.isEmpty())
                    updateActualObjective(new Point2D.Double(tx, ty));
                else
                    addObjective(new Point2D.Double(tx, ty), false);

                if (distance <= ATTACK_RANGE / 2) {
                    if (System.currentTimeMillis() - lastAttack >= attSpeed) {
                        int realDamage = target.calculateRealDamage(dmg);
                        boolean isDead = target.getDamage(realDamage);
                        if (isDead) {
                            entityManager.remove(target);
                            minimapManager.remove(target);

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
        g.fill(CShape.warrior(x, y, s));
/*
        g.setColor(targetOnRange ? new Color(200,100,100,100) : new Color(100,200,100,100));
        g.fill(new Ellipse2D.Double(x - VISION_RANGE / 2,y - VISION_RANGE/2,VISION_RANGE,VISION_RANGE));

        g.setColor(new Color(200,100,255,200));
        g.fill(new Ellipse2D.Double(x - ATTACK_RANGE / 2,y - ATTACK_RANGE / 2, ATTACK_RANGE, ATTACK_RANGE));*/
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
