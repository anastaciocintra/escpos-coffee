/*
 * Use of this source code is governed by the MIT license that can be
 * found in the LICENSE file.
 */
package com.github.anastaciocintra.textprintmodestyle;

import com.github.anastaciocintra.escpos.EscPos;
import com.github.anastaciocintra.escpos.EscPosConst;
import com.github.anastaciocintra.escpos.PrintModeStyle;
import com.github.anastaciocintra.output.PrinterOutputStream;

import javax.print.PrintService;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TextPrintModeStyle {

    public void Sample(String printerName){

        // get the printer service by name passed on command line...
        //this call is slow, try to use it only once and reuse the PrintService variable.
        PrintService printService = PrinterOutputStream.getPrintServiceByName(printerName);
        EscPos escpos;
        try {
            escpos = new EscPos(new PrinterOutputStream(printService));

            PrintModeStyle normal = new PrintModeStyle();

            PrintModeStyle title = new PrintModeStyle()
                    .setFontSize(true, true)
                    .setJustification(EscPosConst.Justification.Center);

            PrintModeStyle subtitle = new PrintModeStyle()
                    .setBold(true)
                    .setUnderline(true);

            PrintModeStyle bold = new PrintModeStyle()
                    .setBold(true);

            escpos.writeLF(title,"My Market")
                    .feed(3)
                    .write(normal,"Client: ")
                    .writeLF(subtitle, "Jane Doe")
                    .feed(3)
                    .writeLF(normal,"Cup of coffee                      $1.00")
                    .writeLF(normal,"Botle of water                     $0.50")
                    .writeLF(normal,"----------------------------------------")
                    .feed(2)
                    .writeLF(bold,
                            "TOTAL                              $1.50")
                    .writeLF(normal,"----------------------------------------")
                    .feed(8)
                    .cut(EscPos.CutMode.FULL);


            escpos.close();

        } catch (IOException ex) {
            Logger.getLogger(TextPrintModeStyle.class.getName()).log(Level.SEVERE, null, ex);
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
        TextPrintModeStyle obj = new TextPrintModeStyle();
        obj.Sample(args[0]);

    }


}
