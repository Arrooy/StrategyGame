package Model.UI.Mouse_Area_Selection;

import Model.DataContainers.ObjectInfo;


/**
 * Todoo elemento que quiera ser selecionado ha de implementar esta interficie.
 */
public interface Selectable {
    double getLX();

    double getRX();

    double getUY();

    double getLY();

    boolean isSelected();

    void setSelected(boolean state);

    ObjectInfo getInfo();
}
