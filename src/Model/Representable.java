package Model;

import java.awt.*;

/**
 * BASE DEL SISTEMA SKETCH,UPDATER,Controller.
 * Define que los elementos han de poderse actualizar (UPDATER)
 * y han de poderse renderizar() -> aparecer en la imagen generada por Controller
 */

public interface Representable {
    void update();

    void render(Graphics2D g);
}
