package Model.Unitats;

import Model.MouseSelector;
import Model.Representable;
import Model.Selectable;
import Model.UI.Mappable;
import Model.UI.Minimap;
import Model.UI.ObjectInfo;
import Model.WorldManager;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;


public class Entity implements Representable, Selectable, Mappable {

    public final int THRESHOLD_STOP_TIME_F = 10;
    public final double THRESHOLD_STOP_DIST_F = 0.5;

    public int THRESHOLD_STOP_TIME = 100;
    public double THRESHOLD_STOP_DIST = 0.35;

    private boolean selected;
    private int numberOfColisions;
    private long lastColisionUpdate = 0;
    private boolean inSlowZone;

    private LinkedList<Point2D.Double> objList;
    private Point2D.Double actualObj;
    private double maxSpeed,x,y,vx,vy,ax,ay,s,maxAccel,minArriveDistance;
    private Color c;

    private long lastMillis;
    private int estat;

    //NEEDS TO BE A CLASS!
    private double attSpeed = 1;
    private int def = 0;
    private int hp = 400;
    private int maxHp = 5000;
    private int dmg = 1;
    private String img;
    private ObjectInfo objectInfo;

    public Entity(double x, double y, double  maxSpeed, double maxAccel) {
        this.x = x;
        this.y = y;
        estat = 0;
        objList = new LinkedList<>();
        c = new Color(50,180,80);
        this.maxSpeed = maxSpeed;
        this.maxAccel = maxAccel;
        minArriveDistance = 100;
        s = 5;
        selected = false;
        numberOfColisions = 0;
        inSlowZone = false;

        img = "car.png";

        objectInfo = new ObjectInfo(hp,maxHp,dmg,def,attSpeed,maxSpeed, img);
        MouseSelector.add(this);
        Minimap.add(this);
    }

    public void addObjective(Point2D.Double o){
        objList.add(o);
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

    int aux = 0;
    @Override
    public void update() {
        switch (estat){
            case 0:
                if(!objList.isEmpty()){
                    actualObj = objList.getFirst();
                    THRESHOLD_STOP_DIST = THRESHOLD_STOP_DIST_F;
                    estat++;
                }
                break;

            case 1:
                calculateMovement();
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
                        objList.removeFirst();
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
    public void render(Graphics2D g) {

        for(Point2D p : objList){
            g.setColor(Color.red);
            g.draw(new Rectangle2D.Double(p.getX() - s / 2,p.getY() - s / 2,s,s));
        }

        g.setColor(selected ? Color.black : c);
        g.fill(new Rectangle2D.Double(x - s / 2,y - s/2,s,s));
    }

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
    public ObjectInfo getInfo() {
        return objectInfo;
    }

    @Override
    public double getCenterX() {
        return x ;//- WorldManager.xPos();
    }

    @Override
    public double getCenterY() {
        return y ;//- WorldManager.yPos();
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
    public Color getMapColor() {
        return c;
    }

}
