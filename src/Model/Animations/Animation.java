package Model.Animations;

import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class Animation {
    private LinkedList<BufferedImage> frames;
    private long framePeriod;
    private long lastTime;
    private int actualFrame;
    private int frameSize;

    public Animation(String sheetName, long framePeriod) {
        this.frames = SheetCutter.obtainFrames(sheetName);
        this.framePeriod = framePeriod;
        this.frameSize = frames.size();
        actualFrame = 0;
        lastTime = 0;
    }

    public BufferedImage getFrame() {
        System.out.println("GETTING " + actualFrame);
        return frames.get(actualFrame);
    }

    public void nextFrame() {
        actualFrame++;
        if (actualFrame >= frameSize) actualFrame = 0;
    }

    public long getLastTime() {
        return lastTime;
    }

    public long getPriod() {
        return framePeriod;
    }

    public void updateLastTime(long currentTimeMillis) {
        lastTime = currentTimeMillis;
    }
}
