/*
 * Use of this source code is governed by the MIT license that can be
 * found in the LICENSE file.
 */

package getstart;

import escpos.EscPos;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.PrintService;
import output.PrinterOutputStream;

public class Getstart {
    public void printInfo(String printerName){
        //this call is slow, try to use it only once and reuse the PrintService variable.
        PrintService printService = PrinterOutputStream.getPrintServiceByName(printerName);
        try {
            PrinterOutputStream printerOutputStream = new PrinterOutputStream(printService);
            EscPos escpos = new EscPos(printerOutputStream);
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
