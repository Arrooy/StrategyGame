package Utils;

import Model.Edificis.Building;
import Model.Edificis.SimpleWall;
import Model.UI.Map.Minimap;
import Model.UI.Mouse_Area_Selection.MouseSelector;
import Model.UI.Mouse_Area_Selection.Selectable;
import Model.UI.Resources;

import java.awt.*;

import static Controlador.Controller.mouseX;
import static Controlador.Controller.mouseY;

/**
 * Gestor de construccion del juego.
 * -> Permite construir un edificio
 * -> Detecta en realTime si se puede construir el edificio (Localizacion del edificio no obstruida)
 * -> Permite ver una preview del edificio a construir
 * -> Si se confirma la construccion, se resta el importe a los recursos
 */

//TODO: Restar el importe de la construcccion a los recursos del equipo que ha construido algo!

public class BuildManager {

    private final static Color okColor = new Color(50,200,100);
    private final static Color badColor = new Color(200,50,100);

    private static Building blueprint;
    private static boolean readyToBuild;
    private static Color c;

    private static double wallRad;
    private static boolean needsAnUpdate;

    public static void init(){
        readyToBuild = false;
        c = badColor;
        wallRad = 0;
        needsAnUpdate = false;
    }

    public static void initBuild(Building building){
        Resources.add(- building.getPrice());
        blueprint = building;
        wallRad = 30;
        needsAnUpdate = false;
    }

    public static Building finshBuild(){
        blueprint.setSpawnPoint(blueprint.getCenterX() + blueprint.getWidth() * 1.5,blueprint.getCenterY());
        blueprint.restoreOriginalColor();
        Minimap.add(blueprint);
        MouseSelector.add(blueprint);
        Building aux = blueprint;
        blueprint = null;
        return aux;
    }

    public static void abortBuild(){
        if (blueprint == null) return;
        Resources.add(blueprint.getPrice());
        blueprint = null;
    }

    //TODO: MODIFICAR LES CONFICIONS PER AL CAS DE UNA SIMPLE WALL!
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
            if (needsAnUpdate && blueprint instanceof SimpleWall) {
                ((SimpleWall) blueprint).updateOuterRad(wallRad);
                needsAnUpdate = false;
            }
            blueprint.move(mouseX, mouseY);
            readyToBuild = isClearLocation();
            c = readyToBuild ? okColor : badColor;
        }
    }

    public static void render(Graphics2D g) {
        if(blueprint != null) {

            DEBUG.add("blueprint x is ", blueprint.getCenterX());
            DEBUG.add("blueprint y is ", blueprint.getCenterY());

            //g.fill(new Rectangle2D.Double(blueprint.getLX(), blueprint.getUY(), blueprint.getWidth(), blueprint.getHeigth()));
            blueprint.setColor(c);
            blueprint.render(g);
        }
    }

    public static boolean isReadyToBuild() {
        return readyToBuild;
    }

    public static boolean isBuilding(){
        return blueprint != null;
    }

    public static int modifyWallSize(int addition) {
        wallRad += addition;
        needsAnUpdate = true;
        return (int) wallRad;
    }
}
