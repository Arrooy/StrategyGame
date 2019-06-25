package Model.Animations;

import Utils.AssetManager;

import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class SheetCutter {

    public static LinkedList<BufferedImage> obtainFrames(String sheetName, int x, int y, int w, int h, int num) {
        LinkedList<BufferedImage> res = new LinkedList<>();
        BufferedImage sheet = AssetManager.getImage(sheetName);

        //System.out.println(sheet.getWidth() + " imgH " + sheet.getHeight());


        for (int i = 0; i < num; i++) {

            try {
                res.add(sheet.getSubimage(x + i * w, y, w, h));
            } catch (Exception e) {
                System.out.println("Error in sheetName: " + sheetName + " x " + (x + i * w) + " " + " y " + y + " w " + w + " h " + h);
            }
        }
        return res;
    }
}