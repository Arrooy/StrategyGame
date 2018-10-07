package Model;

import Model.Unitats.Entity;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;

import static Controlador.Controller.mouseX;
import static Controlador.Controller.mouseY;

public class MouseSelector {

    private static double ix;
    private static double iy;
    private static boolean selecting;
    private static int quadrante;
    private static LinkedList<Selectable> selectables;
    private static LinkedList<Selectable> selected;

    public static void init() {
        selecting = false;
        quadrante = 0;
        selectables = new LinkedList<>();
        selected = new LinkedList<>();
    }

    public static void add(Selectable s){
        selectables.add(s);
    }

    public static void remove(Managable blueprint) {
        selectables.remove(blueprint);
    }

    public static void addToSelection(Selectable s){
        selected.add(s);
    }

    public static void mousePressed(){

        ix = mouseX;
        iy = mouseY;
        selecting = true;
        if (!selected.isEmpty()) selected.clear();
    }

    public static void mouseReleased() {
        selecting = false;
        for (Selectable s : selectables) {
            s.setSelected(almostInsideTheSelectionArea(s)|| insideTheSelectionClick(s));
            if (s.isSelected()) selected.add(s);
        }
    }

    //VERIFICA LES 4 CANTONADES DEL SELECTABLE SON AL INTERIOR DE LA SELECCIO
    private static boolean insideTheSelectionArea(Selectable s){
        return s.getLX() >= ix && s.getRX() <= mouseX &&
                s.getUY() >= iy && s.getLY() <= mouseY;
    }

    //VERIFICA EL MOUSE AL INTERIOR DEL SELECTABLE
    private static boolean insideTheSelectionClick(Selectable s){
        return mouseX >= s.getLX() && mouseX <= s.getRX() &&
                mouseY >= s.getUY()&& mouseY <= s.getLY();
    }

    //VERIFICA TOCAR UN SELECTABLE AMB ALGUNA CANTONADA
    private static boolean almostInsideTheSelectionArea(Selectable s){
        return  selectAPoint(ix,iy,mouseX,mouseY,s.getLX(),s.getUY(),quadrante) ||
                selectAPoint(ix,iy,mouseX,mouseY,s.getLX(),s.getLY(),quadrante) ||
                selectAPoint(ix,iy,mouseX,mouseY,s.getRX(),s.getUY(),quadrante) ||
                selectAPoint(ix,iy,mouseX,mouseY,s.getRX(),s.getLY(),quadrante);
    }


    public static boolean selectAPoint(double EX,double EY,double DX,double DY ,double x,double y,int quadrante){
        if(quadrante == 1){
            return x >= EX && x <= DX &&
                    y <= EY && y >= DY;
        }else if(quadrante == 2){
            return x <= EX && x >= DX &&
                    y <= EY && y >= DY;
        }else if (quadrante == 3){
            return x <= EX && x >= DX &&
                    y >= EY && y <= DY;
        }else{
            return x >= EX && x <= DX &&
                    y >= EY && y <= DY;
        }
    }

    public static void render(Graphics2D g){
        if(selecting && (mouseX != ix || mouseY != iy)){

            Color c = new Color(100,100,100,50);
            g.setColor(c);
            Rectangle2D rect;

            if((mouseX - ix) < 0 && !(mouseY - iy < 0)) {
                quadrante = 3;
                rect = new Rectangle2D.Double(ix - Math.abs(mouseX - ix), iy, Math.abs(mouseX - ix), Math.abs(mouseY - iy));
            }else if(mouseY - iy < 0 && !((mouseX - ix) < 0)) {
                quadrante = 1;
                rect = new Rectangle2D.Double(ix, iy - Math.abs(mouseY - iy), Math.abs(mouseX - ix), Math.abs(mouseY - iy));
            }else if((mouseX - ix) < 0 && (mouseY - iy < 0)) {
                quadrante = 2;
                rect = new Rectangle2D.Double(ix - Math.abs(mouseX - ix), iy - Math.abs(mouseY - iy), Math.abs(mouseX - ix), Math.abs(mouseY - iy));
            }else{
                quadrante = 4;
                rect = new Rectangle2D.Double(ix, iy,Math.abs(mouseX - ix),Math.abs(mouseY - iy));
            }

            g.fill(rect);
            g.setColor(new Color(c.getRed(),c.getGreen(),c.getBlue(),255));
            g.draw(rect);
        }
    }

    public static LinkedList<Selectable> selectedItems() {
        return selected;
    }

    public static boolean multipleSelection(){
        return selected.size() > 1;
    }

    public static boolean hasSelection() {
        return selected != null && !selected.isEmpty();
    }

    public static LinkedList<Selectable> getAllItems() {
        return selectables;
    }


    public static Entity getFirstEntity() {
        for (Selectable s : selectedItems()) {
            if (s instanceof Entity) return (Entity) s;
        }
        return null;
    }
}
