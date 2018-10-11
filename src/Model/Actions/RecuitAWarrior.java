package Model.Actions;

import Model.Edificis.Barrack;
import Model.UI.Mouse_Area_Selection.Selectable;
import Model.UI.Resources;
import Model.Unitats.Warrior;


/**
 * Permite iniciar el reclutamiento de un warrior
 */


public class RecuitAWarrior extends Action {

    public RecuitAWarrior(Selectable s) {
        super("prev_warrior.png", s);
    }

    @Override
    public void init() {
        if (Resources.canAfford(Warrior.price)) {
            Resources.add(-Warrior.price);
            Barrack b = (Barrack) actuator;
            b.train(new Warrior(b.getCenterX(), b.getCenterY(), 12, 0.9, b.getTeam()));
        }
    }
}
