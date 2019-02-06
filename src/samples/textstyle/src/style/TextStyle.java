/*
 * Use of this source code is governed by the MIT license that can be
 * found in the LICENSE file.
 */

package style;

import escpos.EscPos;
import escpos.EscPosConst;
import escpos.Style;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.PrintService;
import output.PrinterOutputStream;

/**
 * Show how to use text styles. 
 * @author Marco Cintra
 */
public class TextStyle {

    public void Sample(String printerName){

        // get the printer service by name passed on command line...
        //this call is slow, try to use it only once and reuse the PrintService variable.
        PrintService printService = PrinterOutputStream.getPrintServiceByName(printerName);
        EscPos escpos;
        try {
            escpos = new EscPos(new PrinterOutputStream(printService));
            
            Style title = new Style()
                    .setFontSize(Style.FontSize._3, Style.FontSize._3)
                    .setJustification(EscPosConst.Justification.Center);

            Style subtitle = new Style(escpos.getStyle())
                    .setBold(true)
                    .setUnderline(Style.Underline.OneDotThick);
            Style bold = new Style(escpos.getStyle())
                    .setBold(true);
            
            escpos.writeLF(title,"My Market")
                    .feed(3)
                    .write("Client: ")
                    .writeLF(subtitle, "John Doe")
                    .feed(3)
                    .writeLF("Cup of coffee                      $1.00")
                    .writeLF("Botle of water                     $0.50")
                    .writeLF("----------------------------------------")
                    .feed(2)
                    .writeLF(bold, 
                             "TOTAL                              $1.50")
                    .writeLF("----------------------------------------")
                    .feed(8)
                    .cut(EscPos.CutMode.FULL);
            
            
            escpos.close();
            
        } catch (IOException ex) {
            Logger.getLogger(TextStyle.class.getName()).log(Level.SEVERE, null, ex);
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
            TextStyle obj = new TextStyle();
            obj.Sample(args[0]);

    }
    
}
