/*
 * Use of this source code is governed by the MIT license that can be
 * found in the LICENSE file.
 */
package com.github.anastaciocintra.output;

import java.awt.print.PrinterJob;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;

/**
 * Supply OutputStream to the printer.
 * <p>
 * PrinterOutputStream send data directing to the printer. The instance cannot
 * be reused and the last command should be <code>close()</code>, after that,
 * you need to create another instance to send data to the printer.
 */
public class PrinterOutputStream extends PipedOutputStream {

    protected final PipedInputStream pipedInputStream;
    protected final Thread threadPrint;

    /**
     * creates one instance of PrinterOutputStream.
     * <p>
     * Create one print based on print service. Start print job linked (this)
     * output stream.
     *
     * @param printService value used to create the printer job
     * @exception IOException if an I/O error occurs.
     * @see #getPrintServiceByName(java.lang.String)
     * @see #getDefaultPrintService()
     */
    public PrinterOutputStream(PrintService printService) throws IOException {

        UncaughtExceptionHandler uncaughtException = (Thread t, Throwable e) -> {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, e.getMessage(),e);
        };

        pipedInputStream = new PipedInputStream();
        super.connect(pipedInputStream);

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

        threadPrint = new Thread(runnablePrint);
        threadPrint.setUncaughtExceptionHandler(uncaughtException);
        threadPrint.start();
    }

    /**
     * creates one instance of PrinterOutputStream with default print service.
     * <p>
     * @exception IOException if an I/O error occurs.
     * @see #PrinterOutputStream(javax.print.PrintService)
     * @see #getDefaultPrintService()
     */
    public PrinterOutputStream() throws IOException {
        this(getDefaultPrintService());
    }

    /**
     * Set UncaughtExceptionHandler to make special error treatment.
     * <p>
     * Make special treatment of errors on your code.
     *
     * @param uncaughtException used on (another thread) print.
     */
    public void setUncaughtException(UncaughtExceptionHandler uncaughtException) {
        threadPrint.setUncaughtExceptionHandler(uncaughtException);
    }

    /**
     * Get the name of all printers on the system.
     *
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
     * Get default system printer. This call is slow, try to use it only once
     * and reuse the PrintService variable.
     *
     * @return default printer.
     */
    public static PrintService getDefaultPrintService() {
        PrintService foundService = PrintServiceLookup.lookupDefaultPrintService();
        if (foundService == null) {
            throw new IllegalArgumentException("Default Print Service is not found");
        }
        return foundService;

    }

    /**
     * Get print having its name containing the passed string.
     * <p>
     * This call is slow, try to use it only once and reuse the PrintService
     * variable.
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
