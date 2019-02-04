package getstart;

import escpos.EscPos;
import java.awt.print.PrinterJob;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.swing.SwingUtilities;

public class Getstart {
    public void printinfo(){
        try {

        DocFlavor df = DocFlavor.INPUT_STREAM.AUTOSENSE;
        PipedOutputStream pos = new PipedOutputStream();
        PipedInputStream pis = new PipedInputStream(pos);
//        EscPos os = new EscPos(pos);
        Doc d = new SimpleDoc(pis, df, null);
//        PrintService P = PrintServiceLookup.lookupDefaultPrintService();
        PrintService P = getPrintByName("epson tm-t20");
        ;

        if (P != null) {
            DocPrintJob job = P.createPrintJob();
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        job.print(d, null);
                    } catch (PrintException ex) {
                    }
                }
            });
        }


            EscPos escpos = new EscPos(pos);
//            escpos.info();
            escpos.writeLF("Hello Wold");
            escpos.feed(5);
            escpos.cut(EscPos.CutMode.FULL);
            pos.close();
        } catch (IOException ex) {
            Logger.getLogger(Getstart.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    private PrintService getPrintByName(String printerName){
    PrintService[] services = PrinterJob.lookupPrintServices();
        PrintService service = null;
        // Retrieve a print service from the array
        for (int index = 0; service == null && index < services.length; index++) {

            if (services[index].getName().compareToIgnoreCase(printerName) == 0) {
                service = services[index];
                break;
            }
        }        
        return service;

    }
    public static void main(String[] args) {
        Getstart obj = new Getstart();
        obj.printinfo();
    }
    
}
