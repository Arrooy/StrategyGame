package Model.Animations;

import Model.CameraControl.WorldManager;

import java.awt.*;
import java.util.LinkedList;

public class Animator implements Runnable {

    private LinkedList<Animation> animations;

    public Animator(LinkedList<Animation> animation) {
        animations = animation;
        new Thread(this).start();
    }

    public void render(Graphics2D g) {
        for (Animation a : animations) {
            g.drawImage(a.getFrame(), 150 + WorldManager.xPos(), 150 + WorldManager.yPos(), null);
        }
    }

    @Override
    public void run() {
        while (true) {
            for (Animation a : animations) {
                if (System.currentTimeMillis() - a.getLastTime() > a.getPriod()) {
                    a.nextFrame();
                    a.updateLastTime(System.currentTimeMillis());
                }
            }
        }
    }
}
