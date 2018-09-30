package Model;

import Controlador.Controller;

public class Updater extends Thread {

    Representable r;

    public Updater(Representable rs){
        r = rs;
    }

    @Override
    public void run() {
        while (true){
            r.update();
            try {
                sleep(Controller.UPDATE_DELAY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
