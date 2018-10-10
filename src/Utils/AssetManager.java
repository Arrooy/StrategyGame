package Utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static java.awt.Image.SCALE_DEFAULT;

/** Gestiona tots els assets del joc. Coordina la càrrega de contingut inicial i guarda les imatges orientades a la UI*/
public class AssetManager {

    private static CharSequence AcceptedExtensions[] = new CharSequence[]{
            ".jpg",
            ".png",
            ".gif"
    };

    /** Localitzacio de les imatges de la UI*/
    private final static String PATH_IMATGES = "./assets/Images";

    /** Conjunt d'imatges de la UI*/
    private static Map<String, BufferedImage> imatges;

    /** Carrega tot el contingut del joc*/
    public static void loadData() {
        loadImatges();
        Sounds.loadAllSounds();
    }

    /** Carrega les imatges de la UI*/
    private static void loadImatges(){
        //Es llegeix la carpeta indicada en PATH_IMATGES
        File carpetaAssetsCartes = new File(PATH_IMATGES);
        File[] listOfFiles = carpetaAssetsCartes.listFiles();

        imatges = new HashMap<>();

        //Si la carpeta existeix
        if (listOfFiles != null) {
            //Per cada foto de la UI
            for (File foto : listOfFiles) {
                BufferedImage img = null;

                if(!checkExtension(foto))
                    continue;
                try {
                    //Es llegeix la foto
                    img = ImageIO.read(foto);
                } catch (IOException e) {
                    e.printStackTrace();
                    //En el cas de trobar un error en la carrega, es para el sistema
                    System.out.println("Error llegint " + foto.getName());
                }
                if (img != null) {
                    //Si la lectura ha sigut correcte, es guarda la imatge
                    imatges.put(foto.getName(), img);

                    //S'indica en la loadingScreen l'estat de la carrega
                    //System.out.println("Loaded " + imatges.size() + " UI images.");
                }
            }
            System.out.println("Loaded " + imatges.size() + " UI images.");
        }
    }

    /**
     * Revisa que l'extensio de l'imatge sigui correcte.
     * @param foto imatge que es vol comprovar
     * @return false si no s'accepta l'imatge
     */
    private static boolean checkExtension(File foto) {
        for(CharSequence ext : AcceptedExtensions){
            if(foto.getName().contains(ext))return true;
        }
        return false;
    }

    /**
     * Retorna la font del Casino amb un tamany definit per size
     * @param size mida de la font
     * @return font personalitzada
     */
    public static Font getFont(String name,int size) {
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, new File("./assets/Fonts/" + name)).deriveFont((float)size);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font);
            return font;
        } catch (IOException|FontFormatException e) {
            //La font no sha trobat / no s'ha pogut carregar
            //Fem plan b
            return new Font("Comic Sans", Font.PLAIN, 20);
        }
    }

    /**
     * Retorna una imatge de la UI
     * @param nom nom de la imatge que es vol obtenir
     * @return imatge solicitada
     */

    public static Image getImage(String nom){
        return imatges.get(nom) != null ? imatges.get(nom) : imatges.get("default.png");
    }


    //TODO: BAD IDEA!!!!!!!! RESIZE IN A LOOP IS FUKING BAD
    /**
     * Retorna una imatge de la UI escalada
     * @param nom nom de la imatge que es vol obtenir
     * @param width amplada de la imatge
     * @param height altura de la imatge
     * @return imatge amb el tamany personalitzat
     */
    public static Image getImage(String nom, int width, int height) {
        return getImage(nom).getScaledInstance(width,height,SCALE_DEFAULT);
    }
}
