package Model;

import Model.Edificis.Edifici;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;

import static Controlador.Controller.mouseX;
import static Controlador.Controller.mouseY;

public class BuildManager {

    private final static Color okColor = new Color(50,200,100);
    private final static Color badColor = new Color(200,50,100);

    private static Edifici blueprint;
    private static boolean readyToBuild;
    private static Color c;

    public static void init(){
        readyToBuild = false;
        c = badColor;

    }

    public static void initBuild(Edifici edifici){
        blueprint = edifici;
    }

    public static Edifici finshBuild(){
        return blueprint;
    }

    public static void abortBuild(){
        blueprint = null;
    }

    private static boolean isClearLocation(){
        return true;
    }

    public static void update() {
        if(blueprint != null) {
            blueprint.move(mouseX, mouseY);
            readyToBuild = isClearLocation();
            c = readyToBuild ? okColor : badColor;
        }
    }

    public static void render(Graphics2D g) {
        if(blueprint != null) {
            g.setColor(c);
            g.fill(new Rectangle2D.Double(blueprint.getLX(), blueprint.getUY(), blueprint.getWidth(), blueprint.getHeigth()));
        }
    }

    public static boolean isReadyToBuild() {
        return readyToBuild;
    }

    public static boolean isBuilding(){
        return blueprint != null;
    }

    public static int buildPrice() {
        return blueprint.getPrice();
    }
}
