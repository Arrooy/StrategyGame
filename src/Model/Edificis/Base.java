package Model.Edificis;

import Model.DataContainers.Action;
import Model.DataContainers.ObjectInfo;
import Model.DataContainers.RecuitAMiner;
import Model.Unitats.Entity;
import Model.Unitats.Miner;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.concurrent.ConcurrentLinkedQueue;

import static Model.Sketch.entityManager;

public class Base extends Building {

    public static final int price = 200;
    private ObjectInfo objectInfo;

    private ConcurrentLinkedQueue<TrainTask> trainQueue;

    public Base(double x, double y, double sx, double sy, int hp) {
        super(x, y, sx, sy, hp, price);

        trainQueue = new ConcurrentLinkedQueue<>();

        Action actions [] = new Action[1];
        actions[0] = new RecuitAMiner(this);

        objectInfo = new ObjectInfo(hp,10,10,10,10,10,"prev_castle.png",actions);
    }

    private double dist(double x, double y, Point2D p) {
        return Math.sqrt(Math.pow(x - p.getX(), 2) + Math.pow(y - p.getY(), 2));
    }
    @Override
    public synchronized void update() {
        if(!trainQueue.isEmpty()){
            TrainTask tt = trainQueue.peek();
            if (System.currentTimeMillis() - tt.getLastTrain() >= tt.getTrainingTime()) {
                Entity aux = tt.getNextEntityToTrain();

                double cx = getSpawnPoint().getX(), cy = getSpawnPoint().getY();
                double dir = Math.atan2(cy - y, cx - x);
                aux.setLocation(x + Math.cos(dir) * (sx + aux.getSizeX() + 2) / 2, y + Math.sin(dir) * (sy + aux.getSizeY() + 2) / 2);
                entityManager.add(aux);
                aux.addObjective(this.getSpawnPoint());
                trainQueue.remove();
                if (!trainQueue.isEmpty()) trainQueue.peek().initTrain();
            }
        }
    }

    @Override
    public void render(Graphics2D g) {
        g.setColor(selected ? c.darker() : c);
        g.fill(new Rectangle2D.Double(x - sx / 2.0,y - sy / 2.0,sx,sy));
        // g.fill(new Ellipse2D.Double(x - sx / 2.0,y - sy / 2.0,sx,sy));
        if(selected){
            g.setColor(Color.blue);
            g.draw(new Rectangle2D.Double(spawnPoint.getX() - 2.5,spawnPoint.getY() - 2.5,5,5));
        }
        if(!trainQueue.isEmpty()){
            double maxTrainSize = sx + 30;
            int height = 5;
            double gap = sy / 2.0 + 5;

            TrainTask aux = trainQueue.peek();
            long trainingTime = aux.getTrainingTime();
            long lastTrain = aux.getLastTrain();

            g.setColor(Color.gray);
            g.fill(new Rectangle2D.Double(x - maxTrainSize / 2,y - gap - height,maxTrainSize,height));

            g.setColor(Color.blue);
            g.fill(new Rectangle2D.Double(x - maxTrainSize / 2, y - gap - height, (System.currentTimeMillis() - lastTrain) * maxTrainSize / trainingTime, height));

            g.setFont(new Font("Arial",Font.PLAIN,10));
            FontMetrics fm = g.getFontMetrics();
            String a = "" + trainQueue.size();
            g.setColor(Color.black);
            g.drawString(a,(float)(x - fm.stringWidth(a)), (float) (y - gap - 10));
        }
    }

    @Override
    public ObjectInfo getInfo() {
        return objectInfo;
    }

    public void train(Miner miner) {
        trainQueue.add(new TrainTask(miner));
    }
}
