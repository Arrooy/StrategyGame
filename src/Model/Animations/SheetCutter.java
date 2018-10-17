package Model.Animations;

import Utils.AssetManager;

import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class SheetCutter {

    public static LinkedList<BufferedImage> obtainFrames(String sheetName) {
        LinkedList<BufferedImage> res = new LinkedList<>();
        BufferedImage sheet = AssetManager.getImage(sheetName);


        for (int i = 1; i < 8; i++) {
            res.add(sheet.getSubimage(37 * i, 50 * 6, 38, 50));
        }
        return res;
    }
}
