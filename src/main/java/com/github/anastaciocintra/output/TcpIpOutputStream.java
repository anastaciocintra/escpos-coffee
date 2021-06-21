/*
 * Use of this source code is governed by the MIT license that can be
 * found in the LICENSE file.
 */

package com.github.anastaciocintra.output;


import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Supply OutputStream to the TCP/IP printer.
 * <p>
 * Send data directing to the printer. The instance cannot
 * be reused and the last command should be <code>close()</code>, after that,
 * you need to create another instance to send data to the printer.
 */
public class TcpIpOutputStream  extends PipedOutputStream {
    protected final PipedInputStream pipedInputStream;
    protected final Thread threadPrint;



    /**
     * creates one instance of TcpIpOutputStream.
     * <p>
     *
     * @param host - the IP address
     * @param port - the port number
     * @exception IOException if an I/O error occurs.
     * @exception RuntimeException if an error occurs while in thread
     * @see java.net.Socket
     */

    public TcpIpOutputStream(String host, int port) throws IOException {
        pipedInputStream = new PipedInputStream();
        super.connect(pipedInputStream);
        Thread.UncaughtExceptionHandler uncaughtException = (Thread t, Throwable e) -> {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, e.getMessage(),e);
        };


        Runnable runnablePrint = () -> {

            try (Socket socket = new Socket(host,port)) {

                OutputStream outputStream = socket.getOutputStream();


                byte[] buf = new byte[1024];
                while(true) {
                    int n = pipedInputStream.read(buf);
                    if( n < 0 ) break;
                    outputStream.write(buf,0,n);
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }

        };

        threadPrint = new Thread(runnablePrint);
        threadPrint.setUncaughtExceptionHandler(uncaughtException);
        threadPrint.start();


    }

    /**
     * creates one instance of TcpIpOutputStream using default port 9100
     * <p>
     *
     * @param host - the IP address
     * @exception IOException if an I/O error occurs.
     * @exception RuntimeException if an error occurs while in thread
     * @see java.net.Socket
     */
    public TcpIpOutputStream(String host) throws IOException {
        this(host,9100);
    }

    /**
     * Set UncaughtExceptionHandler to make special error treatment.
     * <p>
     * Make special treatment of errors on your code.
     *
     * @param uncaughtException used on (another thread) print.
     */
    public void setUncaughtException(Thread.UncaughtExceptionHandler uncaughtException) {
        threadPrint.setUncaughtExceptionHandler(uncaughtException);
    }

}
