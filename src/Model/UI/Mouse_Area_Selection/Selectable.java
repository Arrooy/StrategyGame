package Model.UI.Mouse_Area_Selection;

import Model.DataContainers.ObjectInfo;

public interface Selectable {
    double getLX();
    double getRX();
    double getUY();
    double getLY();

    boolean isSelected();
    void setSelected(boolean state);

    ObjectInfo getInfo();
}
