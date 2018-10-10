package Model.Actions;

import Model.Edificis.Base;
import Model.UI.Mouse_Area_Selection.Selectable;
import Model.UI.Resources;
import Model.Unitats.Miner;

public class RecuitAMiner extends Action {

    public RecuitAMiner(Selectable s) {
        super("prev_miner.png",s);
    }

    @Override
    public void init() {
        if (Resources.canAfford(Miner.price)) {
            Resources.add(-Miner.price);
            Base b = (Base) actuator;
            b.train(new Miner(b.getCenterX(), b.getCenterY(), 12, 0.9, b.getTeam()));
        }
    }
}
