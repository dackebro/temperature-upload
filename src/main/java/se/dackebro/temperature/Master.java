package se.dackebro.temperature;

import javax.swing.*;

/**
 * Initiates the Gui class and makes the gui visible.
 *
 * @author Erik Dackebro
 * @version 2015-03-22
 */
public class Master {

    /**
     * Run the constructor of this class.
     *
     * @param args arguments (not used)
     */
    public static void main(final String[] args) {
        Master master = new Master();
    }

    /**
     * Initiates the Gui class, sets size,
     * sets visibility and that the
     * program closes once cross is clicked.
     */
    public Master() {
        Gui go = new Gui();
        go.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        go.setSize(300,400);
        go.setVisible(true);
    }
}
