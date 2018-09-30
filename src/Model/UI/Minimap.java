package Model.UI;

import Model.WorldManager;

import javax.swing.border.StrokeBorder;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;

import static Controlador.Controller.*;

public class Minimap {

    private static final Color hoverColor = new Color(200,200,200,75);
    private static final Color normalColor = new Color(200,200,200,255);
    private static final Color hoverColorB = new Color(100,100,100,75);
    private static final Color normalColorB = new Color(100,100,100,255);

    private static double sx = 0,sy = 0;

    private static double widthToMap = 0,heightToMap = 0;
    private static double sizeProportion = 5.0;
    private static int strokeSize = 2;

    private static LinkedList<Mappable> objInMap;

    public static void init() {
        objInMap = new LinkedList<>();
        updateSize();
    }

    public static void add(Mappable m){
        objInMap.add(m);
    }

    public static void updateSize(){
        sx = gameHeight / sizeProportion;
        sy = sx;
        widthToMap = gameWidth + WorldManager.getMaxScrollHorizontal();
        heightToMap = gameHeight + WorldManager.getMaxScrollHorizontal();
    }

    public static boolean isMouseOver() {
        return mouseX >= 0 && mouseX <= sx && mouseY >= gameHeight - sy && mouseY <= gameHeight;
    }

    public static void render(Graphics2D g){

        g.setColor(normalColor);
        Rectangle2D rect = new Rectangle2D.Double(0,gameHeight - sy,sx,sy);
        g.fill(rect);
        g.setColor(normalColorB);
        g.setStroke(new BasicStroke(strokeSize));
        g.draw(rect);
        g.setStroke(new BasicStroke(1));

        g.setColor(Color.blue);
        double xx = WorldManager.xPos() * sx / widthToMap;
        double yy = gameHeight - sy + WorldManager.yPos() * sy / heightToMap;
        g.draw(new Rectangle2D.Double(xx,yy,gameWidth * sx / widthToMap,gameHeight * sx / heightToMap));

        for(Mappable m : objInMap){
            double ax = m.getCenterX() * (sx - m.getMapSize()) / widthToMap;
            double ay = gameHeight - sy - 1 + m.getCenterY() * sy / heightToMap;

            g.setColor(m.getMapColor());
            g.fill(new Rectangle2D.Double(ax - m.getMapSize() / 2,ay, m.getMapSize(), m.getMapSize()));
        }
    }



    private static double map(double value, double istart, double istop, double ostart, double ostop) {
        return ostart + (ostop - ostart) * ((value - istart) / (istop - istart));
    }

    public static double mapCoordToRealCoordH(double mouseX,double size) {
        return map(mouseX,0,sx,size/2,widthToMap - size/2);
    }

    public static double mapCoordToRealCoordV(double mouseY,double size) {
        return map(mouseY,gameHeight - sy,gameHeight,size/2,heightToMap - size/2);
    }
}
