package main.com.app;

import main.com.logic.InputProcessor;
import main.com.logic.OutputProcessor;

/**
 *
 * @author gino.tkj@gmail.com
 */
public class Main {

    /**
     * If no argument is provided then the input file present inside
     * main.com.app.logic package is picked up as input file by default.
     *
     * @param args
     */
    public static void main(String[] args) {
        String filePath = null;
        if (args.length != 0) {
            filePath = args[0];
        }
        filePath = "C:\\Users\\gino\\Documents\\NetBeansProjects\\Galaxy Merchant Trading Guide\\src\\Inbound\\Input.txt";
        try {
            InputProcessor.ProcessFile(filePath);
            InputProcessor.MapTokentoIntegerValue();
            OutputProcessor.processReplyForQuestion();
        } catch (Exception e) {
            System.out.println("Oops !! File Not Found ");
        }
    }

}
