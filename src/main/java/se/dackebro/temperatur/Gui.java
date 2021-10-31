package se.dackebro.temperatur;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This class holds all control over the GUI.
 * Amongst other things it controls what happens
 * once a button is pressed and handles all
 * communication with the upload class.
 *
 * @author Erik Dackebro
 * @version 2015-03-22
 */
public class Gui extends JFrame {

    //An upload
    Upload upload;

    //region buttons, text fields and labels
    private JButton confirmSettings;
    private JButton startLoop;
    private JButton refresh;
    private JLabel labelPath;
    private JLabel labelHash;
    private JLabel labelInterval;
    private JTextField textPath;
    private JTextField textHash;
    private JTextField textInterval;
    private JLabel sysout;
    //endregion

    /**
     * Create the Gui.
     * Add controls.
     * Connect buttons to events.
     */
    public Gui() {
        super("upload to temperatur.nu");
        setLayout(new FlowLayout());
        upload = new Upload();

        confirmSettings = new JButton("Spara ändringar");
        startLoop = new JButton("Starta loopen");
        refresh = new JButton("Updatera system information");
        labelPath = new JLabel("Sökväg till last.htm:");
        labelHash = new JLabel("Hash kod:");
        labelInterval = new JLabel("Intervall (s):");
        textPath = new JTextField("F:\\Temperatur.nu\\Värden", 20);
        textHash = new JTextField("0c4e88a2b432146501c63d9182d9aaf9", 20);
        textInterval = new JTextField("60", 20);
        sysout = new JLabel("");

        //Decide what happens when buttons are pressed
        confirmSettings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                upload.setFileLocation(textPath.getText());
                upload.setHash(textHash.getText());
                upload.setInterval(textInterval.getText());
            }
        });

        startLoop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                upload.startLoop();
            }
        });

        refresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sysout.setText(upload.getSysout());
            }
        });

        add(labelPath);
        add(textPath);
        add(labelHash);
        add(textHash);
        add(labelInterval);
        add(textInterval);
        add(confirmSettings);
        add(startLoop);
        add(refresh);
        add(sysout);
    }

}
