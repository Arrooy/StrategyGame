package Model.MassiveListManager;

import Model.UI.Mouse_Area_Selection.MouseSelector;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static Controlador.Controller.UPDATE_DELAY;

public class DedicatedManager <T extends Managable> extends Thread {

    private Map<Double,T> objectsToManage;

    public DedicatedManager() {
        objectsToManage = new ConcurrentHashMap<>();
    }

    public void init() {
        this.start();
    }

    public synchronized void add(T e) {
        objectsToManage.put(e.getKey(),e);
    }

    public synchronized void remove(T e) {
        objectsToManage.remove(e.getKey());
        MouseSelector.remove(e);
    }

    @Override
    public void run() {
        while(true){
            objectsToManage.forEach((id, e) -> e.baseUpdate());
            try {
                sleep(UPDATE_DELAY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public Collection<T> getObjects() {
        return objectsToManage.values();
    }
}
