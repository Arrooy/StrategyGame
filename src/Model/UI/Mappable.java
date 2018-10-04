package Model.UI;

import Model.Managable;

import java.awt.*;

public interface Mappable extends Managable {
    double getCenterX();
    double getCenterY();
    double getMapSizeX();
    double getMapSizeY();
    Color getMapColor();
}
