package Model;

import Controlador.Controller;
import Utils.AssetManager;
import View.Vista;

import javax.swing.*;

/**
 * Las dos variables estaticas permiten decidir un tamaño al juego, dada la posibilidad que
 * el SO no permita FULLSCREEN
 * AVISO: SE HAN VISTO CODIGOS MAS LIMPIOS...eslokayxd
 */

//TODO: Mirar como repercute el uso del sistema Sketch,Controller,Updater y sus threads conjuntamente con swing
//TODO: y su invoke later...

public class Main {

    static int minWidth = 500, minHeight = 500;

    public static void main(String[] args) {


        AssetManager.loadData();

        Vista vista = new Vista(minWidth,minHeight,"Particles");
        Sketch e = new Sketch();
        Controller controller = new Controller(vista,e);

        Updater up = new Updater(e);

        vista.addMouseListener(controller);
        vista.addMouseMotionListener(controller);
        vista.addKeyListener(controller);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                vista.setVisible(true);
            }
        });

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                controller.frameSizeChanged(vista.getWidth(),vista.getHeight());
                e.initSketch();
                controller.start();
                up.start();
            }
        });
    }
}