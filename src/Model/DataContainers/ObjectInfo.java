package Model.DataContainers;

import Model.Actions.Action;
import Utils.AssetManager;

import java.awt.*;
import java.text.DecimalFormat;


/**
 * Permite guardar la informacion de cada Selectable. De esta forma, al selecionar algo, SelectionVisualitzer puede
 * mostrar la informacion correcta del elemento selecionada buscando en este objeto.
 */

//TODO: REPENSAR ESTE CONCEPTO + BUSCARLE UN PACKAGE CORRECTO

public class ObjectInfo {

    private int hp;
    private int maxHp;
    private int damage;
    private int armor;
    private double attSpeed;
    private double vel;

    private String img;
    private DecimalFormat nf = new DecimalFormat("0.00");
    private Action[] actions;
    private int team;

    public ObjectInfo(int hp, int maxHp, int damage, int armor, double attSpeed, double vel, int team, String img) {
        this.hp = hp;
        this.team = team;
        this.damage = damage;
        this.armor = armor;
        this.attSpeed = attSpeed;
        this.vel = vel;
        this.img = img;
        this.maxHp = maxHp;
        actions = null;
    }

    public ObjectInfo(int hp, int maxHp, int damage, int armor, double attSpeed, double vel, int team, String img, Action[] action) {
        this(hp, maxHp, damage, armor, attSpeed, vel, team, img);
        actions = action;
    }

    public boolean hasActions(){
        return actions != null;
    }

    public Action [] getActions(){
        return actions;
    }

    public Image getImg() {
        return AssetManager.getImage(img);
    }

    public String getImgName() {
        return img;
    }


    //TODO: DOWNLOAD ORIGINAL SIZED IMAGES.
    public Image getImg(int w, int h) {
        Image aux = AssetManager.getImage(img);
        //System.out.println(w + " " + h +" " + img + " " + aux.getWidth(null) + "  " + aux.getHeight(null));
        if(aux.getWidth(null) == w && aux.getHeight(null) == h){
            //System.out.println("GIVIN ORIGINAL " + img);
            return aux;
        }else{
            return AssetManager.getImage(img,w,h);
        }
    }

    public int getHp() {
        return hp;
    }

    public int getDamage() {
        return damage;
    }

    public int getArmor() {
        return armor;
    }

    public double getAttSpeed() {
        return attSpeed;
    }

    public double getVel() {
        return vel;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public String getAttSpeedf() {
        return nf.format(attSpeed);
    }

    public String getVelf() {
        return nf.format(vel);
    }

    public void updateHp(int hp) {
        this.hp = hp;
    }

    public int getTeam() {
        return team;
    }
}
