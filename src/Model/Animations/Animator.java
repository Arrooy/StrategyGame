package Model.Animations;

import java.util.LinkedList;

public class Animator implements Runnable {

    private LinkedList<Character> animations;


    public Animator(LinkedList<Character> characters) {
        animations = characters;
        new Thread(this).start();
    }

    @Override
    public void run() {
        while (true) {
            for (Character c : animations) {
                if (System.currentTimeMillis() - c.getLastTime() > c.getPeriod()) {
                    c.nextFrame();
                    c.updateLastTime(System.currentTimeMillis());
                }
            }
        }
    }
}
