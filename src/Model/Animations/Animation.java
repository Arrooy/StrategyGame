package Model.Animations;

import Utils.DEBUG;

import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class Animation {
    private LinkedList<BufferedImage> frames;
    private long framePeriod;
    private long lastTime;
    private int actualFrame;
    private int frameSize;

    public Animation(LinkedList<BufferedImage> frames, long framePeriod) {
        this.frames = frames;
        this.framePeriod = framePeriod;
        this.frameSize = frames.size();
        actualFrame = 0;
        lastTime = 0;
    }

    public BufferedImage getFrame() {
        DEBUG.add("FRAME: ", (Integer) actualFrame);
        if (frames.isEmpty()) return null;
        return frames.get(actualFrame);
    }

    public void nextFrame() {
        actualFrame++;
        if (actualFrame >= frameSize) actualFrame = 0;
    }

    public long getLastTime() {
        return lastTime;
    }

    public long getPeriod() {
        return framePeriod;
    }

    public void updateLastTime(long currentTimeMillis) {
        lastTime = currentTimeMillis;
    }

}
