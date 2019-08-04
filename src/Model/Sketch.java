package Model;

import Model.Animations.Animator;
import Model.Animations.Character;
import Model.CameraControl.WorldManager;
import Model.Edificis.Base;
import Model.Edificis.Building;
import Model.Edificis.Mine;
import Model.MassiveListManager.DedicatedManager;
import Model.UI.Entity_Formations.FormationVisualitzer;
import Model.UI.Entity_Formations.Organizer;
import Model.UI.FX.AnimatedText;
import Model.UI.Map.Mappable;
import Model.UI.Map.Minimap;
import Model.UI.Mouse_Area_Selection.MouseSelector;
import Model.UI.Mouse_Area_Selection.SelectionVisualitzer;
import Model.UI.Resources;
import Model.Unitats.Entity;
import Model.Unitats.Miner;
import Utils.BuildManager;
import Utils.DEBUG;
import Utils.TeamColors;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

import static Controlador.Controller.*;


//TODO: ADD DOUBLE CLICK SELECTION
//TODO: ADD MULTIPLE SELECTION DESCOMPOSE
//TODO: ADD MELE ATTACK UNITS
//TODO: ADD RANGE ATTACK UNITS
//TODO: RELOCATE THE SELECTION VISUALIZER(MOLESTA LA VISIO DEL USER)
//TODO: MOVIMENT DE LA CAMARA AMB MOUSE ESQUERRA


public class Sketch implements Representable{


    public static final DedicatedManager<Entity> entityManager = new DedicatedManager<>("Entity");
    public static final DedicatedManager<Building> buildingsManager = new DedicatedManager<>("Buildings");
    public static final DedicatedManager<Mappable> minimapManager = new DedicatedManager<>("Mappable");
    public static final DedicatedManager<AnimatedText> fxTextManager = new DedicatedManager<>("Animated Text");
    private long RenderTime = 0;
    private long MaxRenderTime = 0;
    private long cleanTime = 0;
    private long updateTime = 0;

    private static LinkedList<Double> KEYS_USED;

    public static double getNewKey() {
        if (KEYS_USED == null)
            KEYS_USED = new LinkedList<>();

        double nextRandom = Math.random();

        while (KEYS_USED.contains(nextRandom)) {
            System.out.println("RANDOM REPETIDO !!!!");
            nextRandom = Math.random();
        }

        KEYS_USED.add(nextRandom);
        return nextRandom;
    }

    public void initSketch(){

        SelectionVisualitzer.init();
        MouseSelector.init();
        WorldManager.init();
        Resources.init();
        Minimap.init();
        BuildManager.init();
        Organizer.init();
        FormationVisualitzer.init();
        TeamColors.init(5);

        entityManager.init();
        buildingsManager.init();
        fxTextManager.init();


        for (int i = 0; i < 100; i++)
            buildingsManager.add(new Mine(Math.random() * (gameWidth + WorldManager.getMaxScrollHorizontal()), Math.random() * 100 + WorldManager.yPos() + gameHeight - 100));

        buildingsManager.add(new Base(WorldManager.xPos() + gameWidth / 2, WorldManager.yPos() + gameHeight / 2, 1, true));

        //buildingsManager.add(new SimpleWall(WorldManager.xPos() + gameWidth / 2 - 100, WorldManager.yPos() + gameHeight / 2, 200, 1));

        int initNWorkers = 10;
        int unitSize = 11;
        for (int i = 0; i < initNWorkers; i++)
            entityManager.add(new Miner(WorldManager.xPos() + gameWidth / 2 - unitSize / 2 * initNWorkers + unitSize * i, WorldManager.yPos() + gameHeight / 2 + 50, 12, 0.9, 1));

        //entityManager.add(new Warrior(WorldManager.xPos() + gameWidth / 2, WorldManager.yPos() + gameHeight / 2 + 75, 12, 0.9, 1));
        //entityManager.add(new Miner(WorldManager.xPos() + 100, WorldManager.yPos() + gameHeight / 2 + 50, 12, 0.9, 2));

        //entityManager.add(new Archer(WorldManager.xPos() + gameWidth / 2, WorldManager.yPos() + gameHeight / 2 + 75, 12, 0.9, 1));

        LinkedList<Character> characters = new LinkedList<>();
        for (Entity e : entityManager.getObjects()) characters.add(e.getCharacter());

        Animator animator = new Animator(characters);
    }

    @Override
    public void update() {
        SelectionVisualitzer.update();

        Organizer.update();
        BuildManager.update();
    }

    @Override
    public void render(Graphics2D g) {
        RenderTime = System.currentTimeMillis();

        DEBUG.render(g);

        WorldManager.update();

        g.translate(-WorldManager.xPos(), -WorldManager.yPos());

        Organizer.render(g);
        buildingsManager.getObjects().forEach((a) -> a.baseRender(g));
        entityManager.getObjects().forEach((a) -> a.render(g));
        BuildManager.render(g);

        g.translate(+WorldManager.xPos(), +WorldManager.yPos());

        fxTextManager.getObjects().forEach((a) -> a.render(g));
        Minimap.render(g);
        FormationVisualitzer.render(g);
        MouseSelector.render(g);
        Resources.render(g);
        SelectionVisualitzer.render(g);

        RenderTime = System.currentTimeMillis() - RenderTime;

        if (System.currentTimeMillis() - cleanTime > 10000) {
            MaxRenderTime = 0;
            cleanTime = System.currentTimeMillis();
        }

        if (RenderTime > MaxRenderTime)
            MaxRenderTime = RenderTime;


        DEBUG.add("Render Time [ms] : ", RenderTime);
        DEBUG.add("Max Render Time [ms] : ", MaxRenderTime);
        DEBUG.add("FPS : ", 1000 / RenderTime);
    }


    public void mouseMoved(){
        DEBUG.add("Mouse screen coord x is ", mouseX);
        DEBUG.add("Mouse screen coord y is ", mouseY);
        DEBUG.add("Mouse World coord x is ", mouseX + WorldManager.xPos());
        DEBUG.add("Mouse World coord y is ", mouseY + WorldManager.yPos());
    }

    public void keyPressed(int key){
        switch (key) {
            case KeyEvent.VK_CONTROL:
                Organizer.ctrlPressed(true);
                break;
            default:
        }
    }


    public void keyReleased(int key) {
        switch (key) {

            case KeyEvent.VK_ADD:

                DEBUG.add("Rad: ", (Integer) BuildManager.modifyWallSize(10));
                break;

            case KeyEvent.VK_SUBTRACT:
                BuildManager.modifyWallSize(-10);
                DEBUG.add("Rad: ", (Integer) BuildManager.modifyWallSize(-10));
                break;
            case KeyEvent.VK_ESCAPE:
                BuildManager.abortBuild();
                break;
            case KeyEvent.VK_CONTROL:
                Organizer.ctrlPressed(false);
                break;
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
                        MouseSelector.mousePressed(e.getClickCount());
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
                        Organizer.setMode(0);
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

}