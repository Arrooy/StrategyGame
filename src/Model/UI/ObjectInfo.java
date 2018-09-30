package Model.UI;

import Utils.AssetManager;

import java.awt.*;
import java.text.DecimalFormat;

public class ObjectInfo {

    private int hp;
    private int maxHp;
    private int damage;
    private int armor;
    private double attSpeed;
    private double vel;

    private String img;
    private DecimalFormat nf = new DecimalFormat("0.00");

    public ObjectInfo(int hp, int maxHp, int damage, int armor, double attSpeed, double vel, String img) {
        this.hp = hp;
        this.damage = damage;
        this.armor = armor;
        this.attSpeed = attSpeed;
        this.vel = vel;
        this.img = img;
        this.maxHp = maxHp;
    }

    public Image getImg() {
        return AssetManager.getImage(img);
    }
    public String getImgName() {
        return img;
    }


    public Image getImg(int w,int h) {
        return AssetManager.getImage(img,w,h);
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

}
