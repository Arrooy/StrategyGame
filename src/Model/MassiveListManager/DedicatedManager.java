package Model.MassiveListManager;

import Model.UI.Mouse_Area_Selection.MouseSelector;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static Controlador.Controller.UPDATE_DELAY;

/**
 * Esta classe se ocupa de :
 * -> Añadir
 * -> Sacar
 * objetos de un mapa del tipo <T>.
 * Aparte de gestionar una entrada i salida segura i concurrente, llama en un
 * thread independiente la funcion baseUpdate(); del objeto
 * <p>
 * Se usa la interficie Managable para poder acordar la existencia del baseUpdate() en los
 * objetos del tipo <T>
 * <p>
 * Esta classe tiene mucha chicha. Basicamente gestiona listas enormes de
 * objetos y las actualitza en threads diferentes para augmentar el rendimiento general del juego.
 * Basicamente reparte la carga del thread Updater() generando threads dedicados para objetos del mismo tipo.
 * De esta forma, Updater() se concentra en actualitzar la UI.
 *
 * @param <T> Tipo que se quiere gestionar
 */

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
