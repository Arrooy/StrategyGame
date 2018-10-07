package Model.Unitats;

import Model.DataContainers.ObjectInfo;
import Model.DataContainers.Trainable;
import Model.Edificis.Base;
import Model.Edificis.Building;
import Model.Edificis.Mine;
import Model.*;
import Model.UI.Mappable;
import Model.UI.Minimap;
import Model.UI.Resources;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static Model.Sketch.buildingsManager;


public abstract class Entity implements Representable, Selectable, Mappable, Managable, Trainable {

    private final int THRESHOLD_STOP_TIME_F = 10;
    private final double THRESHOLD_STOP_DIST_F = 0.5;

    private int THRESHOLD_STOP_TIME = 100;
    private double THRESHOLD_STOP_DIST = 0.35;

    private int numberOfColisions;
    private boolean inSlowZone;

    private long lastMillis;
    private int estat;

    private Point2D.Double actualObj;
    private final double  minArriveDistance = 100;
    private double maxSpeed,vx,vy,ax,ay,maxAccel;
    private int objectiveID,lastObjectiveID;
    //Protected vars
    protected double s = 11, x, y;
    protected boolean selected;
    protected Map<Integer,Point2D.Double> objList;
    private Double key = Sketch.getNewKey();

    boolean imFreeToMove = false;
    protected boolean cantMoveImLoading = false;

    public Entity(double x, double y, double  maxSpeed, double maxAccel) {
        this.x = x;
        this.y = y;
        objList = new ConcurrentHashMap<>();
        this.maxSpeed = maxSpeed;
        this.maxAccel = maxAccel;

        selected = false;
        estat = 0;
        numberOfColisions = 0;
        inSlowZone = false;
        objectiveID = 0;
        lastObjectiveID = 0;

        MouseSelector.add(this);
        Minimap.add(this);
    }

    public void addObjective(Point2D.Double o){
        objList.put(objectiveID++,o);
    }

    private void calculateMovement(){
        double dx = actualObj.getX() - x;
        double dy = actualObj.getY() - y;
        double mod = Math.sqrt(Math.pow(dx,2) + Math.pow(dy,2));

        double speed = maxSpeed;

        if(mod < minArriveDistance && mod != 0){
            inSlowZone = true;
            speed = mod * maxSpeed / minArriveDistance;
        }else{

            inSlowZone = false;
        }

        dx = dx * speed / mod;
        dy = dy * speed / mod;

        ax = dx - vx;
        ay = dy - vy;
        mod = Math.sqrt(Math.pow(ax,2) + Math.pow(ay,2));

        if(mod != 0) {
            ax = ax * (mod >= maxAccel ? maxAccel : mod) / mod;
            ay = ay * (mod >= maxAccel ? maxAccel : mod) / mod;
        }
    }

    private double dist(double x, double y,Point2D p){
        return Math.sqrt(Math.pow(x-p.getX(),2) + Math.pow(y-p.getY(),2));
    }

    protected double dist(double x, double y, double x1, double y1) {
        return Math.sqrt(Math.pow(x - x1, 2) + Math.pow(y - y1, 2));
    }

    public void baseUpdate() {
        switch (estat){
            case 0:
                if(!objList.isEmpty()){
                    actualObj = objList.get(lastObjectiveID);
                    THRESHOLD_STOP_DIST = THRESHOLD_STOP_DIST_F;
                    estat++;
                }
                break;

            case 1:
                if (!cantMoveImLoading) calculateMovement();
                checkColisionsWithBuildings();
                if(dist(x,y,actualObj) < THRESHOLD_STOP_DIST){
                    estat = 3;
                    lastMillis = System.currentTimeMillis();
                }
                break;
            case 2:
                ax = 0;
                ay = 0;
                vx = 0;
                vy = 0;
                estat = 0;
                break;
            case 3:
                if(System.currentTimeMillis() - lastMillis > THRESHOLD_STOP_TIME){
                    if(dist(x,y,actualObj) < THRESHOLD_STOP_DIST) {
                        actualObj = null;
                        objList.remove(lastObjectiveID++);
                        estat = 2;
                    }else{
                        estat = 1;
                    }
                }
                break;
        }

        x += vx;
        y += vy;

        vx += ax;
        vy += ay;

        ax = 0;
        ay = 0;
        update();
    }

    private void checkColisionsWithBuildings() {
        for (Building b : buildingsManager.getObjects()) {
            double cx = b.getCenterX();
            double cy = b.getCenterY();
            double size = b.getWidth() / 2 + s / 2;
            double dist = dist(cx, cy, x, y);

            if (dist <= size) {
                if (imFreeToMove) {
                    double dir = Math.atan2(cy - y, cx - x);

                    size = size + 1;

                    lastObjectiveID = 0;
                    objectiveID = 0;
                    objList.clear();

                    ax = 0;
                    ay = 0;
                    vx = 0;
                    vy = 0;

                    x = cx - Math.cos(dir) * (size);
                    y = cy - Math.sin(dir) * (size);


                    //TODO: ALGORITME PER SOBREPASAR EL EDIFICI
                    addObjective(new Point2D.Double(x, y));
                    actualObj = new Point2D.Double(x, y);

                    if (this instanceof Miner) {
                        if (b instanceof Mine) {
                            ((Miner) this).harvest(((Mine) b));
                        } else if (b instanceof Base) {
                            Resources.add(((Miner) this).getGold());
                        }
                    }

                    imFreeToMove = false;
                }
            } else {
                imFreeToMove = true;
            }
        }
    }

    private boolean checkColisions(int numCol) {
        boolean flag = true;

        for(Selectable s : MouseSelector.getAllItems()){
            if(s != this) {
                double cx = (s.getLX() + s.getRX() + WorldManager.xPos() * 2) / 2.0;
                double cy = (s.getUY() + s.getLY() + WorldManager.yPos() * 2) / 2.0;

                if (dist(cx, cy, new Point2D.Double(x, y)) <= this.s) {
                    double dir = Math.atan2(cy - y, cx - x);

                    this.ax = - Math.cos(dir) * maxAccel;
                    this.ay = - Math.sin(dir) * maxAccel;

                    // this.x +=  -Math.cos(dir) * this.s;
                    // this.y += -Math.sin(dir) * this.s;

                    flag = false;

                    if(inSlowZone) {
                        numberOfColisions++;
                        if (numberOfColisions % numCol == 0) {
                            THRESHOLD_STOP_DIST *= 2;
                        }
                    }
                }
            }
        }
        if(flag){
            numberOfColisions = 0;
        }
        return flag;
    }

    @Override
    public abstract void update();

    @Override
    public abstract void render(Graphics2D g);

    public double getSizeX() {
        return s;
    }

    public double getSizeY() {
        return s;
    }
    @Override
    public double getLX() {
        return x - s/2 - WorldManager.xPos();
    }

    @Override
    public double getRX() {
        return x + s / 2 - WorldManager.xPos();
    }

    @Override
    public double getUY() {
        return y - s/2 - WorldManager.yPos();
    }

    @Override
    public double getLY() {
        return y + s/2 - WorldManager.yPos();
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

    @Override
    public double getCenterX() {
        return x ;
    }

    @Override
    public double getCenterY() {
        return y ;
    }

    @Override
    public double getMapSizeX() {
        return 2;
    }

    @Override
    public double getMapSizeY() {
        return 2;
    }

    @Override
    public abstract Color getMapColor();

    public Double getKey() {
        return key;
    }

    public abstract long getTrainingTime();

    public void setLocation(double x, double y) {
        this.x = x;
        this.y = y;
    }
}
