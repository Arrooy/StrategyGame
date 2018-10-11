package Model.Actions.Entities;

import Model.Actions.Action;
import Model.CameraControl.WorldManager;
import Model.Edificis.SimpleWall;
import Model.UI.Mouse_Area_Selection.Selectable;
import Model.UI.Resources;
import Model.Unitats.Entity;
import Utils.BuildManager;

import static Controlador.Controller.mouseX;
import static Controlador.Controller.mouseY;

public class BuildASimpleWall extends Action {

    public BuildASimpleWall(Selectable actuator) {
        super("prev_wall.png", actuator);
    }

    @Override
    public void init() {
        if (!BuildManager.isBuilding() && Resources.canAfford(SimpleWall.PRICE)) {
            BuildManager.initBuild(new SimpleWall(mouseX + WorldManager.xPos(), mouseY + WorldManager.yPos(), 30, ((Entity) actuator).getTeam()));
        } else if (!Resources.canAfford(SimpleWall.PRICE)) {
            //ERROR NO MONEY!
        }
    }
}
