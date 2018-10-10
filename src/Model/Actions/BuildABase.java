package Model.Actions;

import Model.CameraControl.WorldManager;
import Model.Edificis.Base;
import Model.UI.Mouse_Area_Selection.Selectable;
import Model.UI.Resources;
import Model.Unitats.Entity;
import Utils.BuildManager;

import static Controlador.Controller.mouseX;
import static Controlador.Controller.mouseY;

public class BuildABase extends Action {

    public BuildABase(Selectable actuator) {
        super("prev_castle.png",actuator);
    }

    @Override
    public void init() {
        if (!BuildManager.isBuilding() && Resources.canAfford(Base.PRICE)) {
            BuildManager.initBuild(new Base(mouseX + WorldManager.xPos(), mouseY + WorldManager.yPos(), ((Entity) actuator).getTeam()));
        } else if (!Resources.canAfford(Base.PRICE)) {
            //ERROR NO MONEY!
        }
    }
}
