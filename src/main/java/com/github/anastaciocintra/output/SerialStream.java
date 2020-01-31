/*
MIT License

Copyright (c) 2020 Marco Antonio Anastacio Cintra <anastaciocintra@gmail.com>.

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */

package com.github.anastaciocintra.output;

import com.fazecast.jSerialComm.SerialPort;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Supply OutputStream to the comm port.
 * <p>
 * Send data directing to the printer. The instance cannot
 * be reused and the last command should be <code>close()</code>, after that,
 * you need to create another instance to send data to the printer.
 *
 */
public class SerialStream extends PipedOutputStream {

    private final PipedInputStream pipedInputStream;
    private final Thread threadPrint;

    /**
     * create one instance based on port descriptor
     * @param portDescriptor  object corresponding to the user-specified port descriptor
     * On Windows machines, this descriptor should be in the form of "COM[*]".<br>
     * On Linux machines, the descriptor will look similar to "/dev/tty[*]".
     * @see SerialPort#getCommPort(String)
     * @throws IOException
     */
    public SerialStream(String portDescriptor) throws IOException {
        pipedInputStream = new PipedInputStream();
        super.connect(pipedInputStream);
        Thread.UncaughtExceptionHandler uncaughtException = (Thread t, Throwable e) -> {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
        };


        Runnable runnablePrint = () -> {
            try {
                SerialPort comPort = SerialPort.getCommPort(portDescriptor);
                if(!comPort.openPort()){
                    throw new IOException("Error on comPort.openPort call");
                }

                OutputStream outputStream = comPort.getOutputStream();
                try{
                    byte[] buf = new byte[1024];
                    while(true) {
                        int n = pipedInputStream.read(buf);
                        if( n < 0 ){
                            break;
                        }
                        outputStream.write(buf,0,n);
                    }

                }finally {
                    outputStream.close();
                    comPort.closePort();
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




