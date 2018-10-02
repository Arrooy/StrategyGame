package Model.DataContainers;

import Model.Edificis.Base;
import Model.Edificis.Building;
import Model.Selectable;
import Model.Sketch;
import Model.Unitats.Miner;

import static Controlador.Controller.gameHeight;
import static Controlador.Controller.gameWidth;

public class RecuitAMiner extends Action {

    public RecuitAMiner(Selectable s) {
        super("prev_miner.png",s);
    }

    @Override
    public void init() {
        Base b = (Base) actuator;
        b.train(new Miner(b.getCenterX(),b.getCenterY(),12,0.9));
    }
}
