package se.dackebro.temperature;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.net.*;
import java.util.Calendar;

/**
 * Application to:
 *          read temperatures from a html file
 *          find the lowest temperature
 *          upload the temperature to www.temperatur.nu
 *              by navigating to a website that includes a
 *              hashcode and the temperature
 *
 * @autor Erik Dackebro
 * @version 2015-03-21
 */
public class Upload {
    //region field variables

    //The location and name of the file
    private String fileLocationName;
    //The hashcode
    private String hash;
    //How often to run
    private int interval;

    //The first line where data is located
    private static final int FIRST_LINE = 18;
    //The second line where data is located
    private static final int SECOND_LINE = 27;
    //Holds all sysout data
    private ArrayList<String> sysoutData;
    //Holds true if the upload loop already is running
    private boolean running;

    //endregion

    //region Working methods

    /**
     * Constructor to set some default values
     */
    public Upload() {
        fileLocationName = "C:\\Users\\Erik\\Desktop";
        interval = 60 * 1000;
        hash = "0c4e88a2b432146501c63d9182d9aaf9";
        sysoutData = new ArrayList<String>();
        running = false;
    }

    /**
     * Start the uploading loop.
     */
    public void startLoop() {
        if (!running) {
            running = false;
            tick();
            // the timer variable must be a javax.swing.Timer
            new javax.swing.Timer(interval, new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    tick();
                }
            }).start();
            addToPrint("Uppladningsslingan startar.");
        } else {
            addToPrint("Uppladdnings slingan körs redan.");
        }
    }

    /**
     * Add this data to the new data to get if the user wants to print the data.
     * The data windows will only contain of maximum five elements.
     *
     * @param newData The data to add
     */
    private void addToPrint(String newData) {
        if (sysoutData.size() > 5) {
            sysoutData.remove(0);
        }
        sysoutData.add(newData);
    }

    /**
     * Generate a string of error/success messages to have in sysout label.
     *
     * @return The string to print in sysout.
     */
    public String getSysout() {
        String toPrint = "<html>";
        for (String element : sysoutData) {
            toPrint += element + "<br>";
        }
        return toPrint;
    }

    /**
     * Make one upload.
     */
    private void tick() {
        final double DEAFULT_VALUE = 1000.000000001;

        //while (true) {
            String[] toAnalyze = read();
            double[] values = new double[2];
            values[0] = DEAFULT_VALUE;
            values[1] = DEAFULT_VALUE;

            values[0] = analyze(toAnalyze[0]);
            values[1] = analyze(toAnalyze[1]);

            if (values[0] != DEAFULT_VALUE && values[1] != DEAFULT_VALUE) {
                double low = compare(values);
                navigate(buildAddress(low));
            }//if

            /*try {
                sleep();
            } catch (InterruptedException e) {
                System.out.println("Avbruten, försöker igen.");
            }//catch*/
        //}//while
    }

    /**
     * Sleep set time.
     *
     * @throws InterruptedException If the sleep command is
     */
    private void sleep() throws InterruptedException {
        Thread.sleep(interval);
    }

    /**
     * Read lines from specified text file.
     * Prints error message if the file is not found.
     *
     * @return The lines read.
     */
    private String[] read() {

        //To store the lines read (guess we don't have more than 45 lines)
        ArrayList<String> lines = new ArrayList<String>(45);

        try {
            //Create an object of the file to read
            File file = new File(fileLocationName);
            //To read from the file
            FileReader fileReader = new FileReader(file);
            //To read the text on the line
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            //The line read
            String lineText = "";
            //Current line number
            int lineNumber = 0;
            //To indicate whether the two lines are read
            boolean notFinised = true;
            //Read the lines in the file and put them in the ArrayList
            while ((lineText = bufferedReader.readLine()) != null) {
                lines.add(lineText);
            }
            //Close the reader
            fileReader.close();
        //Catch errors (i.e. file not found)
        } catch (final IOException e) {
            addToPrint(e.getMessage());
        }

        //return the correct two lines
        return new String[]{lines.get(FIRST_LINE-1), lines.get(SECOND_LINE-1)};
    }

    /**
     * Analyse the line and search for temperature values.
     *
     * @param data A line read from a file.
     * @return The value in string form.
     * @throws IllegalArgumentException There is no value in this line.
     */
    private double analyze(String data) throws IllegalArgumentException {
        int incision = data.indexOf(">");
        data = data.substring(incision + 1);
        incision = data.indexOf("<");
        data = data.substring(0, incision);
        data = data.replace(",", ".");
        try {
            return Double.parseDouble(data);
        } catch (IllegalArgumentException e) {
            addToPrint("Denna rad innehåller inga temperaturvärden. Testa standardinställningarna för html i LogTemp.");
            throw new IllegalArgumentException("This line does not contain a temperature, please try default settings in LogTemp.");
        }
    }

    /**
     * Finds the lowest value of measured values
     * @param values Values to compare between
     * @return The lowest value.
     */
    private double compare(double[] values) {
        return Math.min(values[0], values[1]);
    }

    /**
     * Build the address to navigate to.
     * @param value The lowest value to include in the address.
     * @return The address to navigate to.
     */
    private String buildAddress(double value) {
        String address = "http://www.temperatur.nu/rapportera.php?hash=" + hash + "&t=" + value;
        addToPrint(address);
        return address;
    }

    /**
     * try to navigate to the specified webb address.
     * @param address The address to navigate to.
     * @return true if the attempt succeeded, false otherwise.
     */
    private void navigate(String address) {
        try {
            URL url = new URL(address);
            URLConnection myURLConnection = url.openConnection();
            myURLConnection.connect();

            BufferedReader in = new BufferedReader(new InputStreamReader(
                    myURLConnection.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null)
                System.out.println(inputLine);
            in.close();

            //To get current time
            Calendar cal = Calendar.getInstance();
            cal.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            addToPrint("Uppdaterat " + sdf.format(cal.getTime()));
        }
        catch (Exception e) {
            addToPrint(e.getMessage());
        }

    }

    //endregion

    //region Setters

    /**
     * Set the hash
     *
     * @param hash New hash
     */
    public void setHash(String hash) {
        System.out.println(hash);
        this.hash = hash;
    }

    /**
     * Set the location and of file (\\last.htm is added automatic)
     *
     * @param fileLocation Location of the file
     */
    public void setFileLocation(String fileLocation) {
        fileLocation = fileLocation.replace("\\", "\\\\");
        this.fileLocationName = fileLocation + "\\\\last.htm";
    }

    /**
     * Try to set the time.
     *
     * @param interval
     */
    public void setInterval(String interval) {
        try {
            int newInterval = Integer.parseInt(interval) * 1000;
            if (newInterval <= 0) {
                throw new IllegalArgumentException();
            }
            this.interval = newInterval;
            addToPrint("Interval set to: " + this.interval + " ms.");
        } catch (Exception e) {
            addToPrint(interval + " är ej ett giltigt intervall. Intervallet satt till 60");
        }//catch
    }

    //endregion

}
