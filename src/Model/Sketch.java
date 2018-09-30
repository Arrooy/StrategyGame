package Model;

import Model.Edificis.Base;
import Model.Edificis.Edifici;
import Model.UI.Minimap;
import Model.UI.Resources;
import Model.UI.SelectionVisualitzer;
import Model.Unitats.Entity;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.LinkedList;

import static Controlador.Controller.*;


public class Sketch implements Representable{
    private LinkedList<Entity> entities;

    private LinkedList<Edifici> buildings;



    public void initSketch(){

        SelectionVisualitzer.init();
        MouseSelector.init();
        WorldManager.init();
        Resources.init();
        Minimap.init();
        BuildManager.init();

        buildings = new LinkedList<>();
        entities = new LinkedList<>();

        //12 0.8
        for(int i = 0; i < 500; i++){
            entities.add(new Entity(Math.random() * gameWidth,Math.random() * gameHeight,12,0.9));
        }
    }

    @Override
    public void update() {
        SelectionVisualitzer.update();
        for(Entity c : entities) {
            c.update();
        }
        for(Edifici e : buildings){
            e.update();
        }

        WorldManager.update();
        BuildManager.update();
    }

    @Override
    public void render(Graphics2D g) {
        g.translate( - WorldManager.xPos(),- WorldManager.yPos());

        for(Entity c : entities) {
            c.render(g);
        }

        for(Edifici e : buildings){
            e.render(g);
        }

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


    public void keyPressed(int key){
        switch (key) {
            case ' ':
                if(MouseSelector.hasSelection()){
                    formacion(gameWidth/2 - 100, gameHeight / 2, gameWidth/2 + 100);
                }
                break;
            case 'q':
                if(Resources.canAfford(Base.price)){
                    Resources.add(-Base.price);
                    BuildManager.initBuild(new Base(mouseX,mouseY,20,20,1));
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
            if(!BuildManager.isBuilding()) MouseSelector.mousePressed();
        }else{
            if(BuildManager.isBuilding()){
                if(BuildManager.isReadyToBuild()){
                    buildings.add(BuildManager.finshBuild());
                    BuildManager.abortBuild();
                }
            }else {
                double xPos = WorldManager.xPos(), yPos = WorldManager.yPos();
                for (Selectable s : MouseSelector.selectedItems()) {
                    if (s instanceof Entity) {
                        Entity aux = (Entity) s;
                        double ox = Minimap.isMouseOver() ? Minimap.mapCoordToRealCoordH(mouseX, aux.getSizeX()) : mouseX + xPos;
                        double oy = Minimap.isMouseOver() ? Minimap.mapCoordToRealCoordV(mouseY, aux.getSizeY()) : mouseY + yPos;
                        aux.addObjective(new Point2D.Double(ox, oy));
                    }
                }
            }
        }
    }

    public void mouseReleased(int button){
        if(button == 1){
            if(BuildManager.isBuilding()){
                Resources.add(BuildManager.buildPrice());
                BuildManager.abortBuild();
            }else{
                MouseSelector.mouseReleased();
                if(MouseSelector.selectedItems().isEmpty()){
                    SelectionVisualitzer.hide();
                }else{
                    SelectionVisualitzer.show();
                }
            }
        }else{
            if(BuildManager.isBuilding()){

            }
        }
    }
}
