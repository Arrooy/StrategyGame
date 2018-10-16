package Model.UI.Entity_Formations;

import Model.CameraControl.WorldManager;
import Model.Edificis.Building;
import Model.UI.Map.Minimap;
import Model.UI.Mouse_Area_Selection.MouseSelector;
import Model.UI.Mouse_Area_Selection.Selectable;
import Model.Unitats.Entity;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.LinkedList;

import static Controlador.Controller.mouseX;
import static Controlador.Controller.mouseY;

/***
 * Gestiona las diferentes formaciones del juego indicando a cada elemento selecionado qual es su proximo objetivo.
 * Para elegir como formar unidades se usa el menu generado por FormationVisualitzer.
 *
 * Estas formaciones estan basadas en los elementos selecionados por MouseSelector.
 *
 */

public class Organizer {

    private static LinkedList<Point2D.Double> points;
    private static long lastTimePointRegistered, registerPeriod;

    private static boolean mpMode1;
    private static boolean mpMode2;

    private static int mode;
    private static boolean isFirstClick;

    private static Point2D firstClick = new Point2D.Double();
    private static Point2D secondClick = new Point2D.Double();

    private static boolean ctrlPressed;

    public static void init() {
        points = new LinkedList<>();
        lastTimePointRegistered = 0;
        mode = 0;
        isFirstClick = false;
        mpMode1 = false;
        mpMode2 = false;

        ctrlPressed = false;

        //Periode de mostreig ponderat en ms
        registerPeriod = 10;
    }

    public static void mousePressed() {
        switch (mode) {
            case 0:
                mpMode2 = false;
                break;
            case 1:
                mpMode1 = true;

                mpMode2 = false;
                break;
            case 2:
                mpMode2 = true;
                break;
        }
    }

    public static void mouseReleased() {
        switch (mode) {
            case 0:
                singlePointFormation();

                break;
            case 1:
                if (MouseSelector.hasSelection() && mpMode1) {
                    mpMode1 = false;
                    freeMovementFormation();
                }
                resetOrganization();
                break;
            case 2:
                if (!isFirstClick) {
                    firstClick.setLocation(mouseX + WorldManager.xPos(), mouseY + WorldManager.yPos());
                    isFirstClick = true;
                } else {
                    secondClick.setLocation(mouseX + WorldManager.xPos(), mouseY + WorldManager.yPos());
                    if (MouseSelector.hasSelection()) {
                        doublePointFormation(firstClick.getX(), firstClick.getY(), secondClick.getX(), secondClick.getY());
                    }
                    isFirstClick = false;
                }
                break;
        }
    }

    public static void render(Graphics2D g) {

        if (mpMode2) {
            g.setColor(Color.red);
            g.fill(new Ellipse2D.Double(firstClick.getX(), firstClick.getY(), 2, 2));
            g.fill(new Ellipse2D.Double(secondClick.getX(), secondClick.getY(), 2, 2));
        }
        if (mpMode1) {
            g.setColor(new Color(86, 112, 123));
            g.setStroke(new BasicStroke(7));

            int size = points.size();
            for (int i = 1; i < size; i++) {
                Point2D p = points.get(i);
                Point2D p2 = points.get(i - 1);
                g.draw(new Line2D.Double(p.getX(), p.getY(), p2.getX(), p2.getY()));
            }

            g.setStroke(new BasicStroke(1));
        }
    }

    public static void update() {
        if (mpMode1 && System.currentTimeMillis() - lastTimePointRegistered > registerPeriod) {
            Organizer.addPoint(mouseX + WorldManager.xPos(), mouseY + WorldManager.yPos());
            lastTimePointRegistered = System.currentTimeMillis();
        }
    }

    public static void setMode(int i) {
        mode = i;
    }


    public static void ctrlPressed(boolean state) {
        ctrlPressed = state;
    }

    //MODE 0
    private static void singlePointFormation() {
        double xPos = WorldManager.xPos(), yPos = WorldManager.yPos();
        for (Selectable s : MouseSelector.selectedItems()) {

            if (s instanceof Entity) {
                Entity aux = (Entity) s;
                double ox = Minimap.isMouseOver() ? Minimap.mapCoordToRealCoordH(mouseX, aux.getSizeX()) : mouseX + xPos;
                double oy = Minimap.isMouseOver() ? Minimap.mapCoordToRealCoordV(mouseY, aux.getSizeY()) : mouseY + yPos;
                aux.addObjective(new Point2D.Double(ox, oy), !ctrlPressed);
            }

            if (s instanceof Building) {
                Building aux = (Building) s;
                double ox = Minimap.isMouseOver() ? Minimap.mapCoordToRealCoordH(mouseX, 5) : mouseX + xPos;
                double oy = Minimap.isMouseOver() ? Minimap.mapCoordToRealCoordV(mouseY, 5) : mouseY + yPos;
                aux.setSpawnPoint(ox, oy);
            }
        }
    }

    //MODE 1
    //TODO - BUG: SI NO HI HA SELECCIO TÉ UN COMPORTAMENT EXTRANY
    private static void freeMovementFormation() {
        int size = MouseSelector.selectedItems().size();
        double[][] points = Organizer.getPositions(size);

        //TODO: SI TAMBE SELECCIONEM COSES Q NO SIGUIN ENTITTY , NO VA BE SEGUR. NOT TESTED
        for (int i = 0; i < size; i++) {
            Selectable s = MouseSelector.selectedItems().get(i);
            if (s instanceof Entity) {
                Entity aux = (Entity) s;
                aux.addObjective(new Point2D.Double(points[i][0], points[i][1]), !ctrlPressed);
            }
        }
    }

    public static void resetOrganization() {
        points.clear();
    }

    private static void addPoint(double x, double y) {
        points.add(new Point2D.Double(x, y));
    }

    private static double getSelectionLenght() {
        double lenght = 0;
        int size = points.size();

        for (int i = 1; i < size; i++) {
            Point2D p = points.get(i);
            Point2D p1 = points.get(i - 1);
            lenght += (Math.sqrt(Math.pow(p.getX() - p1.getX(), 2) +
                    Math.pow(p.getY() - p1.getY(), 2)));
        }
        return lenght;
    }

    private static double[][] getPositions(int n) {
        double acum = 0, total = getSelectionLenght();
        double gap = total / (n - 1);
        double[][] pos = new double[n][2];

        pos[0][0] = points.getFirst().getX();
        pos[0][1] = points.getFirst().getY();

        for (int i = 1; i < n; i++) {
            acum += gap;

            double diff = acum, aux = 0;
            int j = 1;
            int size = points.size() - 1;

            //Es declaren com null per a enganyar al compilador.
            Point2D p = null;
            Point2D p1 = null;

            while (diff > 0 && j < size) {

                p = points.get(j);
                p1 = points.get(j - 1);

                aux = (Math.sqrt(Math.pow(p.getX() - p1.getX(), 2) + Math.pow(p.getY() - p1.getY(), 2)));
                diff -= aux;
                j++;
            }


            diff += aux;

            if (p == null || p1 == null) {
                p = points.get(j);
                p1 = points.get(j - 1);
            }

            //REUSE p AND p1
            double alpha = Math.atan2(p.getY() - p1.getY(), p.getX() - p1.getX());
            pos[i][0] = p1.getX() + diff * Math.cos(alpha);
            pos[i][1] = p1.getY() + diff * Math.sin(alpha);
        }

        return pos;
    }

    //TODO: OPTIMITZAR AQUESTA LOCURA
    //MODE: 2
    private static void doublePointFormation(double ix, double iy, double fx, double fy) {
        double ax = ix, ay = iy;
        boolean advanced = true;

        double distance = dist(ix, iy, fx, fy);

        double angle = Math.atan2(fy - iy, fx - ix);

        Entity entity = MouseSelector.getFirstEntity();
        int less = 6;
        int numberOfCars = (int) Math.floor(distance / (entity.getSizeX())) - less;
        double espaiSobrant = entity.getSizeX() * less;
        double spacerX = espaiSobrant / numberOfCars, spacerY = entity.getSizeY() / 2;

        int aux = 0;
        int carCount = 1;
        int size = MouseSelector.selectedItems().size();
        for (Selectable s : MouseSelector.selectedItems()) {
            if (s instanceof Entity) {
                entity = (Entity) s;

                if (advanced && numberOfCars > size - numberOfCars * aux) {
                    ax += entity.getSizeX() / 2 * (numberOfCars - size + numberOfCars * aux) + entity.getSizeX();
                    advanced = false;
                }

                Point2D a = new Point2D.Double(ax + entity.getSizeX() / 2, ay + entity.getSizeY() / 2);

                double x = a.getX() - ix;
                double y = a.getY() - iy;
                double nx = x * Math.cos(angle) - y * Math.sin(angle);
                double ny = x * Math.sin(angle) + y * Math.cos(angle);
                entity.addObjective(new Point2D.Double(nx + ix, ny + iy), !ctrlPressed);

                ax += entity.getSizeX() + spacerX;
                if (carCount++ % numberOfCars == 0) {
                    ax = ix;
                    aux++;
                    ay += entity.getSizeX() + spacerY;
                    advanced = true;
                }
            }
        }
    }

    private static double dist(double x, double y, double x1, double y1) {
        return Math.sqrt(Math.pow(x1 - x, 2) + Math.pow(y1 - y, 2));
    }

    public static String isModeOn(int i) {
        return mode == i ? "1" : "0";
    }
}
