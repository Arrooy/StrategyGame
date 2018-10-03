package Model.UI;

import Model.DataContainers.Action;
import Model.DataContainers.ObjectInfo;
import Model.MouseSelector;
import Model.Selectable;
import Utils.AssetManager;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static Controlador.Controller.*;

public class SelectionVisualitzer {

    private static boolean shown;
    private static double h,w,x;
    private static int desiredH;
    private static double time = 0;
    private static boolean ready;

    private static double maxX;

    public static void init(){
        desiredH = gameHeight / 5;
        x = gameWidth / 4;
        w = gameWidth / 3;
        ready = false;
        shown = false;
    }

    private static double BezierBlend(double t) {
        return (t * t) * (3.0f - 2.0f * t);
    }

    private static double ParametricBlend(double t)
    {
        double sqt = Math.pow(t,2);
        return sqt / (2.0f * (sqt - t) + 1.0f);
    }

    private static void renderSelectionFace(Graphics2D g, ObjectInfo oi) {

        g.setFont(new Font("Arial", Font.PLAIN, 7));
        FontMetrics fm = g.getFontMetrics();

        int is = desiredH - fm.getHeight() - 12;
        int yloc = (int) (gameHeight - h + is + 6);
        int xloc = (int) (x + 5);

        g.drawImage(oi.getImg(is + 5, is), xloc, yloc - 1 - is, null);

        g.setColor(Color.red);
        g.fillRect(xloc, yloc, is + 5, 9);

        g.setColor(Color.green);
        g.fillRect(xloc, yloc, (is + 5) * oi.getHp() / oi.getMaxHp(), 9);

        String hpText = oi.getHp() + " / " + oi.getMaxHp();
        g.setColor(Color.WHITE);
        g.drawString(hpText, (int) (x + 5 + (is + 5) / 2.0 - fm.stringWidth(hpText) / 2.0), yloc + fm.getAscent());
    }
    private static void renderSelectionInfo(Graphics2D g, ObjectInfo oi) {
        g.setFont(new Font("Arial",Font.PLAIN,14));
        FontMetrics fm = g.getFontMetrics();

        int imageSize = (int) ((h - 22.5) / 4.0);
        int a = (int) (x + desiredH),b = (int) (gameHeight - h + 5);
        int c = a + imageSize + 5,d = b + imageSize / 2+ fm.getAscent() / 2;

        g.setColor(Color.white);
        g.drawImage(AssetManager.getImage("info_damageM.png",imageSize,imageSize),a,b,null);
        g.drawString("" + oi.getDamage(),c,d);
        b += imageSize + 5;
        d += imageSize + 5;
        g.drawImage(AssetManager.getImage("info_attSpeed.png",imageSize,imageSize),a,b,null);
        g.drawString("" + oi.getAttSpeedf(),c,d);
        b += imageSize + 5;
        d += imageSize + 5;
        g.drawImage(AssetManager.getImage("info_speed.png",imageSize,imageSize),a,b,null);
        g.drawString("" + oi.getVelf(),c,d);
        b += imageSize + 5;
        d += imageSize + 5;
        g.drawImage(AssetManager.getImage("info_armor.png",imageSize,imageSize),a,b,null);
        g.drawString("" + oi.getArmor(),c,d);
        maxX = c + fm.stringWidth("00,00");
    }

    private static void renderSelectionHabilities(Graphics2D g, ObjectInfo oi) {
        double ax = maxX + 5;
        double ay = gameHeight - h + 5;
        int imageSize = (int) ((h - 22.5) / 2.75);

        g.setStroke(new BasicStroke(4));
        g.draw(new Line2D.Double(ax,ay,ax,ay + h - 10));
        ax += 7;
        g.draw(new Line2D.Double(ax,ay,ax,ay + h - 10));
        g.setStroke(new BasicStroke(1));
        ax += 5;

        if(oi.hasActions()) {
            for (Action action : oi.getActions()) {
                g.drawImage(action.getImg(imageSize, imageSize), (int) ax, (int) ay, null);
                action.show(ax,ay,imageSize,imageSize);
                ax += imageSize + 5;
                if (ax > x + w || ax + imageSize + 5 > x + w) {
                    ax = maxX + 5;
                    ay += imageSize + 5;
                }
            }
        }
    }


    public static boolean mouseOnVisualitzer() {
        if(SelectionVisualitzer.isShown()){
            return mouseY > gameHeight - SelectionVisualitzer.getH() &&
                    mouseY < gameHeight &&
                    mouseX > SelectionVisualitzer.getX() &&
                    mouseX < SelectionVisualitzer.getX() + SelectionVisualitzer.getW();
        }else{
            return false;
        }
    }

    public static void mousePressed() {
        if(shown && ready && MouseSelector.hasSelection()) {
            if(MouseSelector.multipleSelection()){
                //Al apretar sobre un grup dins d'una multiple selection, es selecionan nomes les unitats apretades o
                //es mostres les habilitats d'aquesta unitat!
                /*for(Selectable s : MouseSelector.selectedItems()){

                }*/
            }else{
                Selectable s = MouseSelector.selectedItems().getFirst();
                ObjectInfo oi = s.getInfo();
                if(oi.hasActions()) {
                    for (Action action : oi.getActions()) {
                        if(action.isMouseOver()){
                            action.init();
                            break;
                        }
                    }
                }
            }
        }
    }

    private static void renderMultipleSelection(LinkedList<Selectable> selectedItems, Graphics2D g) {
        Map<String,Integer> multipleSelectionItems = new HashMap<>();

        for(Selectable s : selectedItems){
            String fotoName = s.getInfo().getImgName();
            Integer a = 1;

            if(multipleSelectionItems.containsKey(fotoName)) a += multipleSelectionItems.remove(fotoName);
            multipleSelectionItems.put(s.getInfo().getImgName(),a);
        }

        int ax = (int) x + 5;
        int ay = (int) (gameHeight - h + 5);
        int imageSize = 30;
        g.setColor(Color.white);
        g.setFont(new Font("Arial",Font.PLAIN,7));
        FontMetrics fm = g.getFontMetrics();

        for(String name : multipleSelectionItems.keySet()){
            g.drawImage(AssetManager.getImage(name,imageSize,imageSize),ax,ay,null);
            int val = multipleSelectionItems.get(name);
            g.drawString( val +  "", (float) (ax + imageSize / 2 - fm.stringWidth(val + "") / 2.0),ay + imageSize + fm.getAscent());
            ax += imageSize + 5;
            if(ax >= x + w|| ax + imageSize + 5 >= x + w){
                ay += imageSize + 5;
                ax = (int) x + 5;
            }
        }
    }

    public static void render(Graphics2D g){

        g.setColor(Color.black);
        //TODO: REVISAR AQUESTA BOGERIA DE WIDTH = GAMEWIDTH - X - W...
        g.fill(new Rectangle2D.Double(x,gameHeight - h,gameWidth - x - w,h));
        if(shown && ready && MouseSelector.hasSelection()) {

            if(MouseSelector.multipleSelection()){
                renderMultipleSelection(MouseSelector.selectedItems(),g);
            }else{
                Selectable s = MouseSelector.selectedItems().getFirst();
                ObjectInfo oi = s.getInfo();

                renderSelectionFace(g,oi);
                renderSelectionInfo(g,oi);
                renderSelectionHabilities(g,oi);
            }
        }
    }

    public static void update(){
        double animationSpeed = 0.03;
        if(shown) {
            if(time <= 1- animationSpeed) {
                ready = false;
                time += animationSpeed;
                h = desiredH * BezierBlend(time);
            }else if(time > 1 - animationSpeed ){
                ready = true;
            }
        }else {
            if(time >= animationSpeed) {
                time -= animationSpeed;
                h = desiredH * ParametricBlend(time);
            }
        }
    }

    public static void show() {
        shown = true;
    }
    public static void hide() {
        shown = false;
    }

    public static boolean isShown() {
        return shown;
    }

    public static double getH() {
        return h;
    }

    public static double getW() {
        return w;
    }

    public static double getX() {
        return x;
    }
}
