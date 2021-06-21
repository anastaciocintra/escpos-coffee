/*
 * Use of this source code is governed by the MIT license that can be
 * found in the LICENSE file.
 */
package com.github.anastaciocintra.escpos.barcode;

import com.github.anastaciocintra.escpos.EscPosConst;
import java.io.ByteArrayOutputStream;


/**
 * Supply ESC/POS QRCode bar-code commands
 */
public class QRCode implements EscPosConst, BarCodeWrapperInterface{
    
    /**
     * Values for QRCode model.
     * @see #setModel(QRModel)
     */
    public enum QRModel{
        _1_Default(48),  
        _2(49),  
        ;
        public int value;
        private QRModel(int value){
            this.value = value;
        }
    }
    
    /**
     * Values for QR Error Correction Level.
     * 
     */
    public enum QRErrorCorrectionLevel{
        QR_ECLEVEL_L(48), // 7%
        QR_ECLEVEL_M_Default(49), //15%
        QR_ECLEVEL_Q(50), //25%
        QR_ECLEVEL_H(51)  //30%
        ;
        public int value;
        private QRErrorCorrectionLevel(int value){
            this.value = value;
        }
    }
    
    protected Justification justification;
    protected QRModel model;
    protected int size;
    protected QRErrorCorrectionLevel errorCorrectionLevel;
    
    public QRCode(){
        justification = Justification.Left_Default;
        model = QRModel._1_Default;
        size = 3;
        errorCorrectionLevel = QRErrorCorrectionLevel.QR_ECLEVEL_M_Default;
    }



    /**
     * Set horizontal justification. 
     * @param justification left, center or right.
     * @return this object.
     * 
     */
    public QRCode setJustification(Justification justification) {
        this.justification = justification;
        return this;
    }
    

    /**
     * Set model of bar-code.
     * 
     * @param model value used on function 65
     * @return this object.
     * @see #getBytes(java.lang.String) 
     */
    public QRCode setModel(QRModel model) {
        this.model = model;
        return this;
    }

    /**
     * Set the size of module in dots.
     * @param size value used on function 67
     * @return this object.
     * @throws IllegalArgumentException when size is not between 1 and 16.
     */
    public QRCode setSize(int size) throws IllegalArgumentException{
        if(size < 1 || size > 16){
            throw new IllegalArgumentException("size must be between 1 and 16" );
        }
        this.size = size;
        return this;
    }
    
    /**
     * Select the error correction level.<p>
     * @param errorCorrectionLevel value to be used on function 69
     * @return this object
     * @see #getBytes(java.lang.String) 
     */
    public QRCode setErrorCorrectionLevel(QRErrorCorrectionLevel errorCorrectionLevel) {
        this.errorCorrectionLevel = errorCorrectionLevel;
        return this;
    }

    /**
     * QRCode Assembly into ESC/POS bytes. <p>
     * 
     * Select justification <p>
     * ASCII ESC a n <p>
     * 
     * Function 065: Selects the model for QR Code. <p>
     * ASCII GS ( k pL pH cn 65 n1 n2 <p>
     * 
     * Function 067: Sets the size of the module for QR Code in dots. <p>
     * ASCII GS ( k pL pH cn 67 n <p>
     * 
     * Function 069: Selects the error correction level for QR Code. <p>
     * ASCII GS ( k pL pH cn 69 n <p>
     * 
     * Function 080: Store the data in the symbol storage area <p>
     * ASCII GS ( k pL pH cn 80 m d1...dk <p>
     * 
     * Function 081: Print the symbol data in the symbol storage area <p>
     * ASCII GS ( k pL pH cn 81 m <p>
     * 
     * 
     * @param data to be printed in barcode
     * @return bytes of ESC/POS commands to print the barcode  
     */
    @Override
    public byte[] getBytes(String data){
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        
        //
        bytes.write(ESC);
        bytes.write('a');
        bytes.write(justification.value);

        // Function 065
        bytes.write(GS);
        bytes.write('(');
        bytes.write('k');
        bytes.write(4); // pL size of bytes
        bytes.write(0); // pH size of bytes
        bytes.write(49); // cn
        bytes.write(65); // fn
        bytes.write(model.value); // n1
        bytes.write(0); // n2

        // Function 067
        bytes.write(GS);
        bytes.write('(');
        bytes.write('k');
        bytes.write(3); // pL size of bytes
        bytes.write(0); // pH size of bytes
        bytes.write(49); // cn
        bytes.write(67); // fn
        bytes.write(size); // n

        // Function 069
        bytes.write(GS);
        bytes.write('(');
        bytes.write('k');
        bytes.write(3); // pL size of bytes
        bytes.write(0); // pH size of bytes
        bytes.write(49); // cn
        bytes.write(69); // fn
        bytes.write(errorCorrectionLevel.value); // n
        

        // Function 080
        int numberOfBytes = data.length() + 3;
        int pL = numberOfBytes & 0xFF;
        int pH = (numberOfBytes & 0xFF00) >> 8 ;

        bytes.write(GS);
        bytes.write('(');
        bytes.write('k');
        bytes.write(pL); // pL size of bytes
        bytes.write(pH); // pH size of bytes
        bytes.write(49); // cn
        bytes.write(80); // fn
        bytes.write(48); // m
        bytes.write(data.getBytes(),0,data.length());
        
        // Function 081
        bytes.write(GS);
        bytes.write('(');
        bytes.write('k');
        bytes.write(3); // pL size of bytes
        bytes.write(0); // pH size of bytes
        bytes.write(49); // cn
        bytes.write(81); // fn
        bytes.write(48); // m


        return bytes.toByteArray();
    }
        
}
