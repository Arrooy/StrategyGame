package Model.UI.Map;

import Model.MassiveListManager.Managable;

import java.awt.*;

public interface Mappable extends Managable {
    double getCenterX();
    double getCenterY();
    double getMapSizeX();
    double getMapSizeY();
    Color getMapColor();
}
