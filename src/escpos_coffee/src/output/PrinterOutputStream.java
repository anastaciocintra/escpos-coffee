/*
 * The MIT License
 *
 * Copyright 2019 Marco Antonio Anastacio Cintra.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package output;

import java.awt.print.PrinterJob;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.lang.Thread.UncaughtExceptionHandler;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;


/**
 * Supply OutputStream to the printer. <p>
 * PrinterOutputStream send data directing to the printer. The instance cannot
 * be reused, the last command is <code>close()</code>, 
 * after that, you need to create another instance to send data to the printer.
 */
public class PrinterOutputStream extends PipedOutputStream {

    private final PrintService printService;
    private final PipedInputStream pipedInputStream;

    /**
     * creates one instance of PrinterOutputStream.
     * @param printService value used to create the printer job
     * @param uncaughtException because print job run in another thread, 
     * them its parameter is used to caught errors.
     * @exception  IOException  if an I/O error occurs.
     * @see #getPrintServiceByName(java.lang.String) 
     * @see #getDefaultPrintService() 
     * @see #backgroundPrint(java.lang.Thread.UncaughtExceptionHandler) 
     */
    public PrinterOutputStream(PrintService printService, UncaughtExceptionHandler uncaughtException) throws IOException {
        this.printService = printService;

        pipedInputStream = new PipedInputStream();
        super.connect(pipedInputStream);
        backgroundPrint(uncaughtException);
    }



    /**
     * Send stream to the printer. <p>
     * Created automatically after constructor, this function runs in background 
     * and will finish only when close is called.
     * 
     * @param uncaughtException because print job run in another thread, 
     * them its parameter is used to caught errors.
     * @exception  IOException  if an I/O error occurs.
     */
    private void backgroundPrint(UncaughtExceptionHandler uncaughtException) throws IOException {
        Runnable runnablePrint = () -> {
            try {
                DocFlavor df = DocFlavor.INPUT_STREAM.AUTOSENSE;
                Doc d = new SimpleDoc(pipedInputStream, df, null);

                DocPrintJob job = printService.createPrintJob();
                job.print(d, null);
            } catch (PrintException ex) {
                throw new RuntimeException(ex);
            }
        };

        Thread threadPrint = new Thread(runnablePrint);
        threadPrint.setUncaughtExceptionHandler(uncaughtException);
        threadPrint.start();
    }

    /**
     * Get the name of all printers on the system.
     * @return list of printers names.
     */
    public static String[] getListPrintServicesNames() {
        PrintService[] printServices = PrinterJob.lookupPrintServices();
        String[] printServicesNames = new String[printServices.length];
        for (int i = 0; i < printServices.length; i++) {
            printServicesNames[i] = printServices[i].getName();
        }
        return printServicesNames;
    }

    /**
     * Get default system printer.
     * This call is slow, try to use it only once and reuse the PrintService variable.
     * @return  default printer.
     */
    public static PrintService getDefaultPrintService() {
        PrintService foundService = PrintServiceLookup.lookupDefaultPrintService();
        if (foundService == null) {
            throw new IllegalArgumentException("Default Print Service is not found");
        }
        return foundService;
        
    }
    
    /**
     * Get print having its name containing the passed string. <p> 
     * This call is slow, try to use it only once and reuse the PrintService variable.
     * 
     * @param printServiceName name of the printer to find.
     * @return found printer;
     */
    public static PrintService getPrintServiceByName(String printServiceName) {
        PrintService[] printServices = PrinterJob.lookupPrintServices();
        PrintService foundService = null;

        
        for (PrintService service : printServices) {
            if (service.getName().compareTo(printServiceName) == 0) {
                foundService = service;
                break;
            }
        }
        if (foundService != null) {
            return foundService;
        }

        for (PrintService service : printServices) {
            if (service.getName().compareToIgnoreCase(printServiceName) == 0) {
                foundService = service;
                break;
            }
        }
        if (foundService != null) {
            return foundService;
        }
        
        for (PrintService service : printServices) {
            if (service.getName().toLowerCase().contains(printServiceName.toLowerCase())) {
                foundService = service;
                break;
            }
        }
        if (foundService == null) {
            throw new IllegalArgumentException("printServiceName is not found");
        }
        return foundService;
    }

}
