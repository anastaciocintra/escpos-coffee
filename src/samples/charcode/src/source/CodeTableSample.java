/*
 * Use of this source code is governed by the MIT license that can be
 * found in the LICENSE file.
 */

package source;

import escpos.EscPos;
import escpos.EscPos.CharacterCodeTable;
import escpos.EscPosConst;
import escpos.Style;
import escpos.barcode.BarCode;
import escpos.barcode.PDF417;
import escpos.barcode.QRCode;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.PrintService;
import output.PrinterOutputStream;

public class CodeTableSample {

    public void Sample(String printerName){

        // get the printer service by name passed on command line...
        //this call is slow, try to use it only once and reuse the PrintService variable.
        PrintService printService = PrinterOutputStream.getPrintServiceByName(printerName);
        EscPos escpos;
        try {
            escpos = new EscPos(new PrinterOutputStream(printService));
            
            Style title = new Style()
                    .setFontSize(Style.FontSize._2, Style.FontSize._2)
                    .setJustification(EscPosConst.Justification.Center);
            
            escpos.writeLF(title,"Code Table");
            escpos.feed(2);

            escpos.writeLF("Using code table of the France");
            escpos.setCharacterCodeTable(CharacterCodeTable.CP863_Canadian_French);
            escpos.feed(2);
            escpos.writeLF("Liberté et Fraternité.");
            escpos.feed(3);


            escpos.writeLF("Using Portuguese code table");
            escpos.setCharacterCodeTable(CharacterCodeTable.CP860_Portuguese);
            escpos.writeLF("Programação java.");
            
            escpos.feed(5);
            escpos.cut(EscPos.CutMode.FULL);
            
            
            escpos.close();
            
        } catch (IOException ex) {
            Logger.getLogger(CodeTableSample.class.getName()).log(Level.SEVERE, null, ex);
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
            CodeTableSample obj = new CodeTableSample();
            obj.Sample(args[0]);

    }
    
}
