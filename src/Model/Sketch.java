package Model;

import Model.Edificis.Building;
import Model.Edificis.Mine;
import Model.UI.*;
import Model.Unitats.Entity;
import Model.Unitats.Miner;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

import static Controlador.Controller.gameHeight;
import static Controlador.Controller.gameWidth;


public class Sketch implements Representable{

    private static DedicatedManager<Entity> entityManager;
    private static DedicatedManager<Building> buildingsManager;

    private static LinkedList<Double> KEYS_USED;

    public void initSketch(){

        SelectionVisualitzer.init();
        MouseSelector.init();
        WorldManager.init();
        Resources.init();
        Minimap.init();
        BuildManager.init();
        Organizer.init();
        FormationVisualitzer.init();

        entityManager = new DedicatedManager();
        buildingsManager = new DedicatedManager<>();

        entityManager.init();
        buildingsManager.init();

        buildingsManager.add(new Mine(Math.random() * gameWidth, Math.random() * gameHeight, 30, 30, 1000, 0));
        for (int i = 0; i < 500; i++)
            entityManager.add(new Miner(Math.random() * gameWidth, Math.random() * gameHeight, 12, 0.9));
    }
    @Override
    public void update() {
        SelectionVisualitzer.update();
        WorldManager.update();
        Organizer.update();
        BuildManager.update();
    }

    @Override
    public void render(Graphics2D g) {
        g.translate( - WorldManager.xPos(),- WorldManager.yPos());

        Organizer.render(g);
        entityManager.getObjects().forEach((a)->a.render(g));
        buildingsManager.getObjects().forEach((a)->a.render(g));

        g.translate( + WorldManager.xPos(),+ WorldManager.yPos());

        FormationVisualitzer.render(g);
        MouseSelector.render(g);
        SelectionVisualitzer.render(g);
        Minimap.render(g);
        Resources.render(g);
        BuildManager.render(g);
    }

    public void mouseMoved(){

    }

    public void keyPressed(int key){
        switch (key) {

            default:
        }
    }


    public void keyReleased(int key) {
        switch (key) {

            default:
        }
    }

    public void mousePressed(MouseEvent e) {

        int button = e.getButton();
        //Boto esquerra
        if(button == 1){
            //Estem mode construccio?
            if(!BuildManager.isBuilding()) {
                //Estem sobre la seccio d'informacio?
                if(SelectionVisualitzer.mouseOnVisualitzer()){
                    SelectionVisualitzer.mousePressed();
                }else{
                    //Estem sobre la seccio de formacions?
                    if (FormationVisualitzer.mouseOnVisualitzer()) {
                        FormationVisualitzer.mousePressed();
                    } else {
                        //Estem sobre el terreny de joc
                        MouseSelector.mousePressed();
                    }
                }
            }else{
                BuildManager.abortBuild();
            }

        }else{
            //Boto dret

            //Si estem sobre el terreny de joc
            if (!SelectionVisualitzer.mouseOnVisualitzer() && !FormationVisualitzer.mouseOnVisualitzer()) {
                if (BuildManager.isBuilding()) {
                    //Es una bona zona per establir l'edifici?
                    if (BuildManager.isReadyToBuild()) {
                        buildingsManager.add(BuildManager.finshBuild());
                    }
                } else {
                    Organizer.mousePressed();
                }
            }
        }
    }

    public void mouseReleased(MouseEvent e) {
        int button = e.getButton();
        //Boto esquerra
        if(button == 1){
            //Si no estem mode construccio
            if (!BuildManager.isBuilding()) {
                //Si no estem sobre la seccio d'informacio
                if (!SelectionVisualitzer.mouseOnVisualitzer() && !FormationVisualitzer.mouseOnVisualitzer()) {
                    MouseSelector.mouseReleased();

                    if (MouseSelector.selectedItems().isEmpty()) {
                        SelectionVisualitzer.hide();
                    } else {
                        SelectionVisualitzer.show();
                    }
                }
            }
        }else{
            //Boto dret
            Organizer.mouseReleased();

        }
    }

    public static Double getNewKey() {
        if(KEYS_USED == null)
            KEYS_USED = new LinkedList<>();

        double nextRandom = Math.random();

        while(KEYS_USED.contains(nextRandom)){
            System.out.println("RANDOM REPETIDO !!!!");
            nextRandom = Math.random();
        }

        KEYS_USED.add(nextRandom);
        return nextRandom;
    }

    //TODO: millorar aquesta PIPE!
    public static void addEntity(Entity e) {
        entityManager.add(e);
    }
}