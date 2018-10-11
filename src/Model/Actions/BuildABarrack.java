package Model.Actions;

import Model.CameraControl.WorldManager;
import Model.Edificis.Barrack;
import Model.UI.Mouse_Area_Selection.Selectable;
import Model.UI.Resources;
import Model.Unitats.Entity;
import Utils.BuildManager;

import static Controlador.Controller.mouseX;
import static Controlador.Controller.mouseY;


/**
 * Permite inicar la construccion de una barraca en el BuildManager
 */
public class BuildABarrack extends Action {
    public BuildABarrack(Selectable actuator) {
        super("prev_barrack.png", actuator);
    }

    @Override
    public void init() {
        if (!BuildManager.isBuilding() && Resources.canAfford(Barrack.PRICE)) {
            BuildManager.initBuild(new Barrack(mouseX + WorldManager.xPos(), mouseY + WorldManager.yPos(), ((Entity) actuator).getTeam()));
        } else if (!Resources.canAfford(Barrack.PRICE)) {
            //ERROR NO MONEY!
        }
    }
}
