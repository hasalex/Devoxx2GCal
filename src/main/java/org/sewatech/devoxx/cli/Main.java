package org.sewatech.devoxx.cli;

import org.apache.log4j.Logger;
import org.sewatech.devoxx.gcal.ScheduleCopier;
import org.sewatech.devoxx.model.Event;
import org.sewatech.devoxx.model.UserCredentials;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created : 26 oct. 2010
 *
 * @author Alexis Hassler
 * @since 1.0
 */
public class Main {
    private static final Logger logger = Logger.getLogger(Main.class);

    public static void main(String[] args) {
        try {
            ScheduleCopier scheduleCopier = new ScheduleCopier();
            scheduleCopier.fromScheduleId = Event.DEVOXX2010_ID;
            scheduleCopier.toGCalTitle = "My Devoxx Schedule";

            UserCredentials userCredentials = new UserCredentials();
            userCredentials.userName = read("User: ");
            userCredentials.password = readPassword("Password: ");
            scheduleCopier.userCredentials = userCredentials;

            scheduleCopier.copy();

        } catch (Exception e) {
            logger.error(e);
        }
    }

    private static String read(String prompt) throws IOException {
        System.out.print(prompt);

        BufferedReader entreeClavier = new BufferedReader(new InputStreamReader(System.in));
        return entreeClavier.readLine();
    }


    private static String readPassword(String prompt) {
        // Does not work in IDE consoles
        System.out.print(prompt);

        EraserThread et = new EraserThread();
        Thread mask = new Thread(et);
        mask.start();

        BufferedReader entreeClavier = new BufferedReader(new InputStreamReader(System.in));
        String password = "";

        try {
            password = entreeClavier.readLine();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        et.stopMasking();
        return password;
    }


}
