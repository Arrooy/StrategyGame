package Model.DataContainers;

import Model.BuildManager;
import Model.Edificis.Base;
import Model.Selectable;
import Model.UI.Resources;
import Model.WorldManager;

import static Controlador.Controller.mouseX;
import static Controlador.Controller.mouseY;

public class BuildABaseAction extends Action {

    public BuildABaseAction(Selectable actuator) {
        super("prev_castle.png",actuator);
    }

    @Override
    public void init() {
        if(!BuildManager.isBuilding() && Resources.canAfford(Base.price)){
            BuildManager.initBuild(new Base(mouseX + WorldManager.xPos(),mouseY + WorldManager.yPos(),20,20,1));
        }else if(!Resources.canAfford(Base.price)){
            //ERROR NO MONEY!
        }
    }
}
