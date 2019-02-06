/*
 * Use of this source code is governed by the MIT license that can be
 * found in the LICENSE file.
 */

package source;

import escpos.EscPos;
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

public class BarcodeSample {

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
            
            escpos.writeLF(title,"Barcode");
            escpos.feed(2);
            BarCode barcode = new BarCode();

            escpos.writeLF("barcode default options CODE93 system");
            escpos.feed(2);
            escpos.write(barcode, "hello barcode");
            escpos.feed(3);

            escpos.writeLF("barcode write HRI above");
            barcode.setHRIPosition(BarCode.BarCodeHRIPosition.AboveBarCode);
            escpos.feed(2);
            escpos.write(barcode, "hello barcode");
            escpos.feed(3);

            escpos.writeLF("barcode write HRI below");
            barcode.setHRIPosition(BarCode.BarCodeHRIPosition.BelowBarCode);
            escpos.feed(2);
            escpos.write(barcode, "hello barcode");
            escpos.feed(3);
            
            escpos.writeLF("barcode right justification ");
            barcode.setHRIPosition(BarCode.BarCodeHRIPosition.NotPrinted_Default);
            barcode.setJustification(EscPosConst.Justification.Right);
            escpos.feed(2);
            escpos.write(barcode, "hello barcode");
            escpos.feed(3);

            escpos.writeLF("barcode height 200 ");
            barcode.setJustification(EscPosConst.Justification.Left_Default);
            barcode.setBarCodeSize(2, 200);
            escpos.feed(2);
            escpos.write(barcode, "hello barcode");
            escpos.feed(3);
            

            escpos.writeLF("barcode UPCA system ");
            barcode.setSystem(BarCode.BarCodeSystem.UPCA);
            barcode.setHRIPosition(BarCode.BarCodeHRIPosition.BelowBarCode);
            barcode.setBarCodeSize(2, 100);
            escpos.feed(2);
            escpos.write(barcode, "12345678901");
            escpos.feed(3);
            
            escpos.feed(5);
            escpos.cut(EscPos.CutMode.PART);
            
            
            escpos.writeLF(title,"QR Code");
            escpos.feed(2);
            QRCode qrcode = new QRCode();

            escpos.writeLF("QRCode default options");
            escpos.feed(2);
            escpos.write(qrcode, "hello qrcode");
            escpos.feed(3);

            escpos.writeLF("QRCode size 6 and center justified");
            escpos.feed(2);
            qrcode.setSize(7);
            qrcode.setJustification(EscPosConst.Justification.Center);
            escpos.write(qrcode, "hello qrcode");
            escpos.feed(3);

            escpos.feed(5);
            escpos.cut(EscPos.CutMode.PART);
            
            escpos.writeLF(title,"PDF 417");
            escpos.feed(2);
            PDF417 pdf417 = new PDF417();

            escpos.writeLF("pdf417 default options");
            escpos.feed(2);
            escpos.write(pdf417, "hello PDF 417");
            escpos.feed(3);

            escpos.writeLF("pdf417 height 5");
            escpos.feed(2);
            pdf417.setHeight(5);
            escpos.write(pdf417, "hello PDF 417");
            escpos.feed(3);

            escpos.writeLF("pdf417 error level 4");
            escpos.feed(2);
            pdf417 = new PDF417().setErrorLevel(PDF417.PDF417ErrorLevel._4);
            escpos.write(pdf417, "hello PDF 417");
            escpos.feed(3);
            
            escpos.feed(5);
            escpos.cut(EscPos.CutMode.FULL);
            
            
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
