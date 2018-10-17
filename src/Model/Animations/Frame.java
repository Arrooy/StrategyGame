package Model.Animations;

import java.awt.image.BufferedImage;

public class Frame {

    private BufferedImage image;
    private double lx, uy;

    public Frame(BufferedImage image, double lx, double uy) {
        this.image = image;
        this.lx = lx;
        this.uy = uy;
    }

    public BufferedImage getImage() {
        return image;
    }
}
