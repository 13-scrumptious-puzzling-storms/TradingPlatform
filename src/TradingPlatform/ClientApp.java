package TradingPlatform;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class ClientApp {

    public static NetworkManager networkManager;
    private static GUILogin guiLogin;
    private static GUIMain guiMain;

    private static Boolean loggedIn;

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        loggedIn = false;

        // Initialise the client-side network protocol
        networkManager = new NetworkManager();
        Thread networkThread = new Thread(networkManager);
        networkThread.start();

        guiLogin = new GUILogin();
        Thread guiLoginThread = new Thread(guiLogin);
        guiLoginThread.start();

        // networkTest();
        // Get the logged in user
        new GUILogin();
    }

    public static void launchProgram(int userID) throws IOException, ClassNotFoundException {
        loggedIn = true;
        User user = new User(1);
        guiMain = new GUIMain(user);
    }

    public static void displayError(String errorMessage) {
        Component parentComponent;
        if (loggedIn) {
            parentComponent = guiLogin;
        } else {
            parentComponent = guiMain;
        }
        JOptionPane.showConfirmDialog(parentComponent, errorMessage, "Warning", JOptionPane.PLAIN_MESSAGE, JOptionPane.WARNING_MESSAGE);
    }

    private static void networkTest() throws IOException, ClassNotFoundException {
        OrganisationalUnit organisationalUnit = new OrganisationalUnit();
        System.out.println("Organisational Unit Name for ID 1: " + organisationalUnit.getName(1));

        OrganisationAsset oAss = new OrganisationAsset();
        oAss.getOrganisationalUnitAssetTable(1);
    }
}
