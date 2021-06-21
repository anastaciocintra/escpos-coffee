/*
 * Use of this source code is governed by the MIT license that can be
 * found in the LICENSE file.
 */
package com.github.anastaciocintra.escpos.barcode;

import com.github.anastaciocintra.escpos.EscPosConst;
import java.io.ByteArrayOutputStream;

/**
 * Supply ESC/POS PDF417 bar-code commands
 */
public class PDF417 implements EscPosConst, BarCodeWrapperInterface{


    /**
     * Values of Error Correction Level.<p>
     * Used on function 069
     * 
     * @see #setErrorLevel(PDF417ErrorLevel)
     */
    public enum PDF417ErrorLevel{
        /**
         * Error Level Zero.
         */
        _0(48),  
        /**
         * Error Level One.
         */
        _1_Default(49),
        /**
         * Error Level Two.
         */
        _2(50),
        /**
         * Error Level Tree.
         */
        _3(51),
        /**
         * Error Level Four.
         */
        _4(52),
        /**
         * Error Level Five.
         */
        _5(53),
        /**
         * Error Level Six.
         */
        _6(54),
        /**
         * Error Level Seven.
         */
        _7(55),
        /**
         * Error Level Eight.
         */
        _8(56);
        public int value;
        private PDF417ErrorLevel(int value){
            this.value = value;
        }
    }
    
    /**
     * Values of PDF417 Option.<p>
     * Used on function 070
     * @see #setOption(PDF417Option)
     */
    public enum PDF417Option{
        Standard_Default(0),  
        Truncated(1)
        ;
        public int value;
        private PDF417Option(int value){
            this.value = value;
        }
    }
    
    
    
    protected Justification justification;
    protected int numberOfColumns;
    protected int numberOfRows;
    protected int width;
    protected int height;
    protected PDF417ErrorLevel errorLevel;
    protected PDF417Option option;


    
    /**
     * Creates object with default values. 
     */
    public PDF417(){
        justification = Justification.Left_Default;
        numberOfColumns = 0;
        numberOfRows = 0;
        width = 3;
        height = 3;
        errorLevel = PDF417ErrorLevel._1_Default;
        option = PDF417Option.Standard_Default;

    }

    /**
     * Set horizontal justification. 
     * @param justification left, center or right.
     * @return this object.
     * @see #getBytes(java.lang.String) 
     * 
     */
    public PDF417 setJustification(Justification justification) {
        this.justification = justification;
        return this;
    }


    /**
     * Set the number of columns in the data region. <p>
     * Used on function 065
     * 
     * @param numberOfColumns value used on function 065
     * @return this object
     * @throws IllegalArgumentException when numberOfColumns is not between 0 and 30 
     * @see #getBytes(java.lang.String) 
     */
    public PDF417 setNumberOfColumns(int numberOfColumns) throws IllegalArgumentException{
        if(numberOfColumns < 0 || numberOfColumns > 30){
            throw new IllegalArgumentException("numberOfColumns must be between 0 and 30" );
        }
        this.numberOfColumns = numberOfColumns;
        return this;
    }
    
    
    
    /**
     * Set the number of rows. <p>
     * Used on function 066
     * 
     * @param numberOfRows value used on function 066
     * @return this object
     * @throws IllegalArgumentException when numberOfRows is not between 3 and 90 and not equal 0
     * @see #getBytes(java.lang.String) 
     */
    public PDF417 setNumberOfRows(int numberOfRows) throws IllegalArgumentException{
        if(numberOfRows != 0 && (numberOfColumns < 3 || numberOfColumns > 90)){
            throw new IllegalArgumentException("numberOfRows must be 0 or between 3 and 90" );
        }
        this.numberOfRows = numberOfRows;
        return this;
    }
    
    
    /**
     * Set the width of the module. <p>
     * The module height is recommended to be set to 3-5.<p>
     * Used on function 067
     * @param width value used on function 067
     * @return this object
     * @throws IllegalArgumentException when width is not between 2 and 8
     * @see #getBytes(java.lang.String) 
     */
    public PDF417 setWidth(int width) throws IllegalArgumentException{
        if((width < 2 || width > 8)){
            throw new IllegalArgumentException("width must be between 2 and 8" );
        }
        this.width = width;
        return this;
    }


    /**
     * Set the row height. <p>
     * The module height is recommended to be set to 3-5.<p>
     * Used on function 068
     * @param height value used on function 068
     * @return this object
     * @throws IllegalArgumentException when height is not between 2 and 8
     * @see #getBytes(java.lang.String) 
     */
    public PDF417 setHeight(int height) throws IllegalArgumentException{
        if((height < 2 || height > 8)){
            throw new IllegalArgumentException("height must be between 2 and 8" );
        }
        this.height = height;
        return this;
    }

//    /**
//     * Reset the row height. <p>
//     * Used on function 068
//     * @return this object
//     * @see #setHeight(int)  
//     * @see #getBytes(java.lang.String) 
//     */
//    public PDF417 resetHeight(){
//        this.height = 3;
//        return this;
//    }

    /**
     * Set the error correction level.<p>
     * Used on function 069
     * 
     * @param errorLevel error level of function 069
     * @return this object
     * @see #getBytes(java.lang.String) 
     */
    public PDF417 setErrorLevel(PDF417ErrorLevel errorLevel) {
        this.errorLevel = errorLevel;
        return this;
    }


    /**
     * Select the option. <p>
     * Used on function 070
     * 
     * @param option options of function 070
     * @return this object
     * @see #getBytes(java.lang.String) 
     */
    public PDF417 setOption(PDF417Option option) {
        this.option = option;
        return this;
    }
    
    
    
    /**
     * BarCode Assembly into ESC/POS bytes. <p>
     * 
     * Select justification <p>
     * ASCII ESC a n <p>
     * 
     * Function 065: Set the number of columns in the data region <p>
     * ASCII GS ( k pL pH cn 65 n <p>
     * 
     * Function 066: Set the number of rows <p>
     * ASCII GS ( k pL pH cn 66 n <p>
     * 
     * Function 067: Sets the width of the module for PDF417 to n dots. <p>
     * ASCII GS ( k pL pH cn 67 n <p>
     * 
     * Function 068: Sets the row height for PDF417 to [n × (the width of the module)]. <p>
     * ASCII GS ( k pL pH cn 68 n <p>
     * 
     * Function 069: Sets the error correction level for PDF417. <p>
     * ASCII GS ( k pL pH cn 69 48 n <p>
     * 
     * Function 070: Select the options <p>
     * ASCII GS (k pL pH cn 70 n <p>
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
        bytes.write(3); // pL size of bytes
        bytes.write(0); // pH size of bytes
        bytes.write(48); // cn
        bytes.write(65); // fn
        bytes.write(numberOfColumns); // m
        
        // Function 066
        bytes.write(GS);
        bytes.write('(');
        bytes.write('k');
        bytes.write(3); // pL size of bytes
        bytes.write(0); // pH size of bytes
        bytes.write(48); // cn
        bytes.write(66); // fn
        bytes.write(numberOfRows); // m
        
        // Function 067
        bytes.write(GS);
        bytes.write('(');
        bytes.write('k');
        bytes.write(3); // pL size of bytes
        bytes.write(0); // pH size of bytes
        bytes.write(48); // cn
        bytes.write(67); // fn
        bytes.write(width); // m
        
        // Function 068
        bytes.write(GS);
        bytes.write('(');
        bytes.write('k');
        bytes.write(3); // pL size of bytes
        bytes.write(0); // pH size of bytes
        bytes.write(48); // cn
        bytes.write(68); // fn
        bytes.write(height); // m
        
        // Function 069
        bytes.write(GS);
        bytes.write('(');
        bytes.write('k');
        bytes.write(4); // pL size of bytes
        bytes.write(0); // pH size of bytes
        bytes.write(48); // cn
        bytes.write(69); // fn
        bytes.write(48); // m
        bytes.write(errorLevel.value); // n
        
        // Function 070
        bytes.write(GS);
        bytes.write('(');
        bytes.write('k');
        bytes.write(3); // pL size of bytes
        bytes.write(0); // pH size of bytes
        bytes.write(48); // cn
        bytes.write(70); // fn
        bytes.write(option.value); // m


        // Function 080
        int numberOfBytes = data.length() + 3;
        int pL = numberOfBytes & 0xFF;
        int pH = (numberOfBytes & 0xFF00) >> 8 ;

        bytes.write(GS);
        bytes.write('(');
        bytes.write('k');
        bytes.write(pL); // pL size of bytes
        bytes.write(pH); // pH size of bytes
        bytes.write(48); // cn
        bytes.write(80); // fn
        bytes.write(48); // m
        bytes.write(data.getBytes(),0,data.length());
        
        // Function 081
        bytes.write(GS);
        bytes.write('(');
        bytes.write('k');
        bytes.write(3); // pL size of bytes
        bytes.write(0); // pH size of bytes
        bytes.write(48); // cn
        bytes.write(81); // fn
        bytes.write(48); // m
        

        return bytes.toByteArray();
    }    

    
}
