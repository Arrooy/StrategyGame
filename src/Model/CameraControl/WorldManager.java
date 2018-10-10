package Model.CameraControl;

import static Controlador.Controller.*;

//Gestiona moviment de la camara, el tamany del mapa
//TODO: Afegir zoom.

public class WorldManager {

    private static int maxScrollHorizontal, maxScrollVertical;

    private final static int margin = 10;
    private final static int cameraSpeed = 10;
    private static int xPos,yPos;

    public static void init() {
        maxScrollHorizontal = (int) (gameWidth / 1.5);
        maxScrollVertical = gameHeight * 3;
        xPos = maxScrollHorizontal / 2;
        yPos = maxScrollVertical;
    }


    public static void update(){

        if (xPos > cameraSpeed && mouseX < margin && mouseY > margin && mouseY < gameHeight - margin) {
            //Lateral esquerra
            xPos -= cameraSpeed;
        } else if (xPos < maxScrollHorizontal - cameraSpeed && mouseX > gameWidth - margin && mouseY > margin && mouseY < gameHeight - margin) {
            //Lateral dret
            xPos += cameraSpeed;
        } else if (mouseX > gameWidth - margin && mouseY <= margin) {
            //Esquina superior dreta
            if(xPos < maxScrollHorizontal - cameraSpeed) xPos += cameraSpeed;
            if(yPos > cameraSpeed) yPos -= cameraSpeed;
        } else if (mouseX > gameWidth - margin && mouseY >= gameHeight - margin) {
            //Esquina inferior dreta
            if(xPos < maxScrollHorizontal - cameraSpeed) xPos += cameraSpeed;
            if(yPos < maxScrollVertical - cameraSpeed) yPos += cameraSpeed;
        } else if (mouseX < margin && mouseY >= gameHeight - margin) {
            //Esquina inferior esquerra
            if(xPos > cameraSpeed) xPos -= cameraSpeed;
            if(yPos < maxScrollVertical - cameraSpeed) yPos += cameraSpeed;
        } else if (mouseX < margin && mouseY <= margin) {
            //Esquina superior esquerra
            if(xPos > cameraSpeed) xPos -= cameraSpeed;
            if(yPos > cameraSpeed) yPos -= cameraSpeed;
        } else if (yPos > cameraSpeed && mouseX < gameWidth - margin && mouseY < margin && mouseX > margin) {
            //Lateral superior
            yPos -= cameraSpeed;
        } else if (yPos < maxScrollVertical - cameraSpeed && mouseX < gameWidth - margin && mouseY > gameHeight - margin && mouseX > margin) {
            //Lateral inferior
            yPos += cameraSpeed;
        }
    }

    public static double getMaxScrollHorizontal() {
        return maxScrollHorizontal;
    }

    public static double getMaxScrollVertical() {
        return maxScrollVertical;
    }

    public static int xPos() {
        return xPos;
    }

    public static int yPos() {
        return yPos;
    }
}
