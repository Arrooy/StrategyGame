package Model.DataContainers;

import Utils.AssetManager;

import java.awt.*;

import static Controlador.Controller.mouseX;
import static Controlador.Controller.mouseY;

public abstract class Action {

    private String img;
    private double x,y,w,h;
    private boolean mouseOver;
    private boolean shown;

    public Action(String img){
        this.img = img;
        mouseOver = false;
        shown = false;
    }

    //Desenvolupament de l'accio
    public abstract void init();

    public void show(double x,double y,double w,double h){
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        shown = true;
    }

    public boolean isMouseOver(){
        return mouseX >= x && mouseX <= x + w && mouseY >= y && mouseY <= y + h;
    }

    public Image getImg() {
        return AssetManager.getImage(img);
    }

    public Image getImg(int w,int h) {
        return AssetManager.getImage(img,w,h);
    }
}
