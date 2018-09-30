package Model;

import Model.UI.Minimap;
import Model.UI.Resources;
import Model.UI.SelectionVisualitzer;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.LinkedList;

import static Controlador.Controller.*;


public class Sketch implements Representable{
    private LinkedList<Car> cars;

    public void initSketch(){

        SelectionVisualitzer.init();
        MouseSelector.init();
        WorldManager.init();
        Resources.init();
        Minimap.init();


        cars = new LinkedList<>();

        //12 0.8
        for(int i = 0; i < 500; i++){
            cars.add(new Car(Math.random() * gameWidth,Math.random() * gameHeight,12,0.9));
        }
    }

    @Override
    public void update() {
        SelectionVisualitzer.update();
        for(Car c : cars) {
            c.update();
        }
        WorldManager.update();
    }

    @Override
    public void render(Graphics2D g) {
        g.translate( - WorldManager.xPos(),- WorldManager.yPos());

        for(Car c : cars) {
            c.render(g);
        }

        g.translate( + WorldManager.xPos(),+ WorldManager.yPos());

        MouseSelector.render(g);
        SelectionVisualitzer.render(g);
        Minimap.render(g);

        g.setColor(Color.red);
        g.drawRect(gameWidth / 2 - 100,gameHeight / 2 - 50,1,100);
        g.drawRect(gameWidth / 2 + 100,gameHeight / 2 - 50,1,100);
    }


    public void keyPressed(int key){
        switch (key) {
            case ' ':
                if(MouseSelector.hasSelection()){
                    formacion();
                }
                break;
           /* case 'w':
                c.THRESHOLD_STOP_DIST+=0.01;
                break;
            case 's':
                c.THRESHOLD_STOP_DIST-=0.01;
                break;
            case 'a':
                c.THRESHOLD_STOP_TIME -= 1;
                break;
            case 'd':
                c.THRESHOLD_STOP_TIME += 1;
                break;*/

            default:
        }
    }

    private void formacion() {

        double ix = gameWidth/2 - 100, iy = gameHeight / 2, fx = gameWidth/2 + 100;
        double ax = ix,ay = iy;
        double spacerX = 3,spacerY = 3;

        for(Selectable s : MouseSelector.selectedItems()) {
            if(s instanceof Car){
                Car car = (Car) s;
                car.addObjective(new Point2D.Double(ax + car.getSizeX() / 2,ay + car.getSizeY() / 2));
                ax += car.getSizeX() + spacerX;
                if(ax >= fx || ax + car.getSizeX() + spacerX >= fx){
                    ax = ix;
                    ay += car.getSizeX() + spacerY;
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
            MouseSelector.mousePressed();
        }else{
            double xPos = WorldManager.xPos(),yPos = WorldManager.yPos();
            for(Selectable s : MouseSelector.selectedItems()) {
                if(s instanceof Car){
                    Car aux = (Car) s;
                    double ox = Minimap.isMouseOver() ? Minimap.mapCoordToRealCoordH(mouseX,aux.getSizeX()) : mouseX + xPos;
                    double oy = Minimap.isMouseOver() ? Minimap.mapCoordToRealCoordV(mouseY,aux.getSizeY()) : mouseY + yPos;
                    aux.addObjective(new Point2D.Double(ox,oy));
                }
            }
        }
    }

    public void mouseReleased(int button){
        if(button == 1){
            MouseSelector.mouseReleased();
            if(MouseSelector.selectedItems().isEmpty()){
                SelectionVisualitzer.hide();
            }else{
                SelectionVisualitzer.show();
            }
        }else{

        }
    }
}
