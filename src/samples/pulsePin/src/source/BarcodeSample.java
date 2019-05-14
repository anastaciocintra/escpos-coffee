/*
 * Use of this source code is governed by the MIT license that can be
 * found in the LICENSE file.
 */

package source;

import escpos.EscPos;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.PrintService;
import output.PrinterOutputStream;

public class BarcodeSample {

    public void Sample(String printerName){

        // get the printer service by name passed on command line...
        //this call is slow, try to use it only once and reuse the PrintService variable.
        PrintService printService = PrinterOutputStream.getPrintServiceByName(printerName);
        EscPos escpos;
        try {
            escpos = new EscPos(new PrinterOutputStream(printService));
            
            escpos.pulsePin(EscPos.PinConnector.Pin_2, 100, 300);
            
            
            escpos.close();
            
        } catch (IOException ex) {
            Logger.getLogger(BarcodeSample.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    public static void main(String[] args) {
            if(args.length!=1){
            System.out.println("Usage: java -jar xyz.jar (\"printer name\")");
                System.out.println("Printer list to use:");
                String[] printServicesNames = PrinterOutputStream.getListPrintServicesNames();
                for(String printServiceName: printServicesNames){
                    System.out.println(printServiceName);
                }
                
                System.exit(0);
            }
            BarcodeSample obj = new BarcodeSample();
            obj.Sample(args[0]);

    }
    
}
