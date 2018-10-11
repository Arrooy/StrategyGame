package Model.Actions;

import Model.Edificis.Building;
import Model.UI.Mouse_Area_Selection.Selectable;
import Model.Unitats.Entity;

import static Model.Sketch.*;

public class DestroyMeAction extends Action {

    public DestroyMeAction(Selectable a) {
        super("action_destroy", a);
    }

    @Override
    public void init() {

        if (actuator instanceof Building) {
            Building aux = ((Building) actuator);
            buildingsManager.remove(aux);
            minimapManager.remove(aux);

        } else if (actuator instanceof Entity) {
            Entity aux = ((Entity) actuator);
            entityManager.remove(aux);
            minimapManager.remove(aux);
        }
    }
}
