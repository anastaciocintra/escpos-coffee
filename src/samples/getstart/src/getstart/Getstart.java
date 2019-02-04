
package getstart;

import escpos.EscPos;
import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.PrintService;
import output.PrinterOutputStream;

public class Getstart {
    public void printInfo(String printerName){
        // UncaughtExceptionHandler this class is used to catch errors that 
        // might happen in the print thread.
        UncaughtExceptionHandler uncaughtException = (Thread t, Throwable e) -> {
            Logger.getLogger(Getstart.class.getName()).log(Level.SEVERE, null, e);
        };  
        //this call is slow, try to use it only once and reuse the PrintService variable.
        PrintService printService = PrinterOutputStream.getPrintServiceByName(printerName);
        try {
            EscPos escpos = new EscPos(new PrinterOutputStream(printService, uncaughtException));
            escpos.info();
            // do not forget to close...
            escpos.close();
            
        } catch (IOException ex) {
            Logger.getLogger(Getstart.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

        public static void main(String[] args) {
            if(args.length!=1){
                System.out.println("Usage: java -jar getstart.jar (\"printer name\")");
                System.out.println("Printer list to use:");
                String[] printServicesNames = PrinterOutputStream.getListPrintServicesNames();
                for(String printServiceName: printServicesNames){
                    System.out.println(printServiceName);
                }
                
                System.exit(0);
            }
            Getstart obj = new Getstart();
            obj.printInfo(args[0]);

        
    }
    
}
