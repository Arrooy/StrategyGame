package Model;

import Model.Edificis.Building;
import Model.Edificis.Mine;
import Model.UI.Minimap;
import Model.UI.Resources;
import Model.UI.SelectionVisualitzer;
import Model.Unitats.Entity;
import Model.Unitats.Miner;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.LinkedList;

import static Controlador.Controller.*;


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

        entityManager = new DedicatedManager();
        buildingsManager = new DedicatedManager<>();

        entityManager.init();
        buildingsManager.init();
        for (int i = 0; i < 50; i++)
            buildingsManager.add(new Mine(Math.random() * gameWidth, Math.random() * gameHeight, 30, 30, 1000, 0));
        entityManager.add(new Miner(Math.random() * gameWidth,Math.random() * gameHeight,12,0.9));
    }

    @Override
    public void update() {
        SelectionVisualitzer.update();
        WorldManager.update();
        BuildManager.update();
    }

    @Override
    public void render(Graphics2D g) {
        g.translate( - WorldManager.xPos(),- WorldManager.yPos());

        entityManager.getObjects().forEach((a)->a.render(g));
        buildingsManager.getObjects().forEach((a)->a.render(g));

        g.translate( + WorldManager.xPos(),+ WorldManager.yPos());

        MouseSelector.render(g);
        SelectionVisualitzer.render(g);
        Minimap.render(g);
        Resources.render(g);
        BuildManager.render(g);

        g.setColor(Color.red);
        g.drawRect(gameWidth / 2 - 100,gameHeight / 2 - 50,1,100);
        g.drawRect(gameWidth / 2 + 100,gameHeight / 2 - 50,1,100);
    }

    public void mouseMoved(){

    }

    public void keyPressed(int key){
        switch (key) {
            case ' ':
                if(MouseSelector.hasSelection()){
                    formacion(gameWidth/2 - 100, gameHeight / 2, gameWidth/2 + 100);
                }
                break;
            default:
        }
    }

    private void formacion(double ix,double iy,double fx) {

        double ax = ix,ay = iy;
        boolean advanced = true;

        Entity entity = (Entity) MouseSelector.selectedItems().getFirst();
        int numberOfCars = (int) Math.floor((fx - ix) / (entity.getSizeX() + 1));
        double espaiSobrant = (fx - ix) - entity.getSizeX() * numberOfCars;
        double spacerX = espaiSobrant / numberOfCars,spacerY = entity.getSizeY()/2;

        int aux = 0;
        for(Selectable s : MouseSelector.selectedItems()) {
            if(s instanceof Entity){
                entity = (Entity) s;
                if(advanced && numberOfCars > MouseSelector.selectedItems().size() - numberOfCars * aux) {
                    ax += entity.getSizeX()/2 * (numberOfCars - MouseSelector.selectedItems().size() + numberOfCars * aux) + entity.getSizeX();
                    advanced = false;
                }


                entity.addObjective(new Point2D.Double(ax + entity.getSizeX() / 2,ay + entity.getSizeY() / 2));

                ax += entity.getSizeX() + spacerX;
                if(ax >= fx || ax + entity.getSizeX() + spacerX >= fx){
                    ax = ix;
                    aux++;
                    ay += entity.getSizeX() + spacerY;
                    advanced = true;
                }
            }
        }
    }

    public void keyReleased(int key){
        switch (key) {
            default:
        }
    }

    public void mousePressed(int button){
        //Button 1 esquerra 3 dreta
        if(button == 1){
            if(!BuildManager.isBuilding()) {
                if(SelectionVisualitzer.mouseOnVisualitzer()){
                    SelectionVisualitzer.mousePressed();
                }else{
                    MouseSelector.mousePressed();
                }
            }else{
                BuildManager.abortBuild();
            }

        }else{
            if(!SelectionVisualitzer.mouseOnVisualitzer()) {
                if (BuildManager.isBuilding()) {
                    //If its a good location, build
                    if (BuildManager.isReadyToBuild()) {
                        buildingsManager.add(BuildManager.finshBuild());
                    }
                } else {

                    double xPos = WorldManager.xPos(), yPos = WorldManager.yPos();
                    for (Selectable s : MouseSelector.selectedItems()) {

                        if (s instanceof Entity) {
                            Entity aux = (Entity) s;
                            double ox = Minimap.isMouseOver() ? Minimap.mapCoordToRealCoordH(mouseX, aux.getSizeX()) : mouseX + xPos;
                            double oy = Minimap.isMouseOver() ? Minimap.mapCoordToRealCoordV(mouseY, aux.getSizeY()) : mouseY + yPos;
                            aux.addObjective(new Point2D.Double(ox, oy));
                        }

                        if (s instanceof Building) {
                            Building aux = (Building) s;
                            double ox = Minimap.isMouseOver() ? Minimap.mapCoordToRealCoordH(mouseX, 5) : mouseX + xPos;
                            double oy = Minimap.isMouseOver() ? Minimap.mapCoordToRealCoordV(mouseY, 5) : mouseY + yPos;
                            aux.setSpawnPoint(ox, oy);
                        }
                    }
                }
            }
        }
    }

    public void mouseReleased(int button){
        if(button == 1){
            if(!BuildManager.isBuilding()){
                if(!SelectionVisualitzer.mouseOnVisualitzer()) {
                    MouseSelector.mouseReleased();
                    if (MouseSelector.selectedItems().isEmpty()) {
                        SelectionVisualitzer.hide();
                    } else {
                        SelectionVisualitzer.show();
                    }
                }
            }
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
