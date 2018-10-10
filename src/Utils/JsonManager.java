package Utils;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * JsonManager gestiona tota interaccio amb arxius json.
 * Pot escriure i llegir fitxers json.
 * Tambe permet escriure, borrar i llegir camps especifics d'un json determinat.
 * Al utilitzar les funcions escriure i llegir, si al nom del camp que volem llegir li afegim al inici un #,
 * el escriptor/lector ho interpretara com que en aquell camp hi ha o ha d'haver-hi text encriptat.
 */

public class JsonManager {


    private static final String PATH = "./maps/";

    /**
     * LlegirJson rep un conjunt d'identificadors i
     * retorna la informació que contenen aquests identificadors del json objectiu.
     * En el cas de trobar un camp que contingui un #, significa que aquest camp ha de desencriptar-se.
     *
     * @param campsJson Camps que es volen llegir d'un json
     * @return Informació dels camps del json sol·licitats
     */

    public static Object[] llegirJson(String nomFitxer, String... campsJson) {

        Object[] informacioExtreta = new Object[campsJson.length];

        //Es llegeix l'arxiu json
        JSONObject jsonObject;

        //S'obte el json del fitxer CONFIG_FILENAME
        jsonObject = JsonManager.getJSONObject(nomFitxer);

        int index = 0;

        //Per a tots els camps especificats s'extreu la informacio del json
        for (String camp : campsJson) {
            //Si la informacio no esta encriptada, es guarda de manera standard
            informacioExtreta[index++] = jsonObject.has(camp) ? jsonObject.get(camp) : null;
        }

        return informacioExtreta;
    }

    /**
     * Afegeix un nou camp al JSON "CONFIG_FILENAME".
     *
     * @param nomCamp   camp per afegir al json. Si el camp conte un #, aquest s'afegira encriptadament.
     * @param contingut contingut d'aquest camp. El tipus d'aquest esta restringit per les possibilitats
     *                  de la llibreria json.
     * @throws FileNotFoundException En cas de no trobar-se l'arxiu CONFIG_FILENAME es llença aquesta excepcio
     */

    public static void afegeixCamp(String nomFitxer, String nomCamp, Object contingut) {

        //Es guarda una copia actual de JSON i se li afegeix el nom camp

        //S'obte el json del fitxer CONFIG_FILENAME i se li afegeix el camp
        JSONObject nouJson = JsonManager.getJSONObject(nomFitxer).put(nomCamp, contingut);

        //Try - catch - resources per a crear el fitxer
        try (FileWriter file = new FileWriter(PATH + nomFitxer)) {

            //S'afegeix el contingut guardat en l'auxiliar nouJson
            file.write(nouJson.toString(1));
        } catch (IOException e) {
            System.out.println("[Error]: Impossible sobreescriure el fitxer " + nomFitxer);
        }
    }

    /**
     * Elimina un camp del JSON "CONFIG_FILENAME"
     *
     * @param camps camps que es volen eliminar del json
     * @throws FileNotFoundException En cas de no trobar-se l'arxiu CONFIG_FILENAME es llença aquesta excepcio
     */

    public static void eliminaCamps(String nomFitxer, String... camps) throws FileNotFoundException {

        //Es guarda una copia actual de JSON i se li elimina el camp desitjat si aquest existeix en el json
        JSONObject nouJson = JsonManager.getJSONObject(nomFitxer);
        for (String campPerBorrar : camps) {
            if (nouJson.has(campPerBorrar))
                nouJson.remove(campPerBorrar);
        }

        //Try - catch - resources per a crear el fitxer
        try (FileWriter file = new FileWriter(PATH + nomFitxer)) {

            //S'afegeix el contingut guardat en l'auxiliar nouJson
            file.write(nouJson.toString(1));
        } catch (IOException e) {
            System.out.println("[Error]: Impossible sobreescriure el fitxer " + nomFitxer);
        }
    }

    private static JSONObject getJSONObject(String nomFitxer) {
        //Es llegeix l'arxiu json sencer i es retorna en forma de JSONObject
        try {
            return new JSONObject((new Scanner(new File(PATH + nomFitxer)).useDelimiter("}").next()) + "}");
        } catch (FileNotFoundException e) {
            try (FileWriter file = new FileWriter(PATH + nomFitxer)) {

                //S'afegeix el contingut guardat en l'auxiliar nouJson
                file.write("{}");

            } catch (IOException ee) {
                System.out.println("[Error]: Impossible sobreescriure el fitxer " + nomFitxer);
            }
            try {
                return new JSONObject((new Scanner(new File(PATH + nomFitxer)).useDelimiter("}").next()) + "}");
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
        }
        return null;
    }
}
