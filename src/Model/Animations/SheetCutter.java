package Model.Animations;

import Utils.AssetManager;

import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class SheetCutter {

    public static LinkedList<BufferedImage> obtainFrames(String sheetName, int x, int y, int w, int h, int num) {
        LinkedList<BufferedImage> res = new LinkedList<>();
        BufferedImage sheet = AssetManager.getImage(sheetName);
        System.out.println("Working with " + sheetName);
        for (int i = 0; i < num; i++) {
            System.out.println((x + i * w) + " " + y);
            try {

                res.add(sheet.getSubimage(x + i * w, y, w, h));
            } catch (Exception e) {
                System.out.println("HEY!");
                e.printStackTrace();
            }
        }
        return res;
    }
}
