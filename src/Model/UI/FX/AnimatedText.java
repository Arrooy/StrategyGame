package Model.UI.FX;

import Model.MassiveListManager.Managable;
import Model.Sketch;

import java.awt.*;
import java.awt.geom.AffineTransform;

import static Model.Sketch.fxTextManager;

public class AnimatedText implements Managable {

    private float x, y, y1, speed;
    private String content;
    private int alfa, size;
    private long spawnTime;
    private int angle;
    private Color color;
    private double key = Sketch.getNewKey();

    public AnimatedText(int x, int y, String content) {
        spawnTime = System.currentTimeMillis();
        this.content = content;
        this.x = x;
        this.y = y;
        y1 = 0;
        size = 6;
        alfa = 255;
        speed = (float) Math.random() * 3.0f + 0.2f;
        angle = (int) (Math.random() * 20 - 10);
        color = new Color((int) (Math.random() * 255.0), (int) (Math.random() * 255.0), (int) (Math.random() * 255.0));
        ;
    }


    public void render(Graphics2D g) {

        Font font = new Font("Arial", Font.PLAIN, size);
        FontMetrics fm = g.getFontMetrics();

        AffineTransform affineTransform = new AffineTransform();
        affineTransform.rotate(Math.toRadians(angle), 0, 0);

        Font rotatedFont = font.deriveFont(affineTransform);
        g.setFont(rotatedFont);
        g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), alfa < 0 ? 0 : alfa));
        g.drawString(content, x - fm.stringWidth(content) / 2.0f, y + y1);
    }

    @Override
    public void baseUpdate() {
        //Grow 100 ms & go up later
        if (System.currentTimeMillis() - spawnTime > 200) {
            y1 -= speed;
        } else {
            size += 2;
        }

        alfa -= 4;

        if (y1 >= 50 || alfa <= 0) {
            fxTextManager.remove(this);
        }
    }


    @Override
    public double getKey() {
        return key;
    }
}
