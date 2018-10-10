package Utils;

import Model.Edificis.Building;
import Model.UI.Map.Minimap;
import Model.UI.Mouse_Area_Selection.MouseSelector;
import Model.UI.Mouse_Area_Selection.Selectable;
import Model.UI.Resources;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static Controlador.Controller.mouseX;
import static Controlador.Controller.mouseY;

public class BuildManager {

    private final static Color okColor = new Color(50,200,100);
    private final static Color badColor = new Color(200,50,100);

    private static Building blueprint;
    private static boolean readyToBuild;
    private static Color c;

    public static void init(){
        readyToBuild = false;
        c = badColor;
    }

    public static void initBuild(Building building){
        Resources.add(- building.getPrice());
        blueprint = building;
    }

    public static Building finshBuild(){
        blueprint.setSpawnPoint(blueprint.getCenterX() + blueprint.getWidth() * 1.5,blueprint.getCenterY());
        Minimap.add(blueprint);
        MouseSelector.add(blueprint);
        Building aux = blueprint;
        blueprint = null;
        return aux;
    }

    public static void abortBuild(){
        Resources.add(blueprint.getPrice());
        blueprint = null;
    }

    //Burrada de condicions.
    private static boolean almostInsideTheSelectionArea(Selectable s, int quadrante) {
        return  MouseSelector.selectAPoint(blueprint.getLX(),blueprint.getUY(),blueprint.getRX(),blueprint.getLY(),s.getLX(),s.getUY(),quadrante) ||
                MouseSelector.selectAPoint(blueprint.getLX(),blueprint.getUY(),blueprint.getRX(),blueprint.getLY(),s.getLX(),s.getLY(),quadrante) ||
                MouseSelector.selectAPoint(blueprint.getLX(),blueprint.getUY(),blueprint.getRX(),blueprint.getLY(),s.getRX(),s.getUY(),quadrante) ||
                MouseSelector.selectAPoint(blueprint.getLX(),blueprint.getUY(),blueprint.getRX(),blueprint.getLY(),s.getRX(),s.getLY(),quadrante);
    }

    private static boolean isClearLocation(){

        for(Selectable s : MouseSelector.getAllItems()){
            //Mirem els 4 quadrants
            for(int q = 1; q <= 4; q++){
                if(almostInsideTheSelectionArea(s,q)) return false;
            }
        }
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

}
