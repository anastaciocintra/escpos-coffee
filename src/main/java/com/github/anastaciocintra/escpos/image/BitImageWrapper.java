/*
 * Use of this source code is governed by the MIT license that can be
 * found in the LICENSE file.
 */
package com.github.anastaciocintra.escpos.image;

import com.github.anastaciocintra.escpos.EscPosConst;
import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Supply ESC/POS Bit Image commands.<p>
 * using <code>ESC '*'</code>
 */
public class BitImageWrapper implements ImageWrapperInterface, EscPosConst{
    /**
     * Values for Bit Image Mode.
     * 
     * @see #setMode(BitImageMode)
     */
    public enum BitImageMode{
        _8DotSingleDensity(0,8),  
        _8DotDoubleDensity(1,8),  
        _24DotSingleDensity(32,24),  
        _24DotDoubleDensity_Default(33,24)
        ;
        public int value;
        public int bitsForVerticalData;
        private BitImageMode(int value, int bitsPerSlice){
            this.value = value;
            this.bitsForVerticalData = bitsPerSlice;
        }
    }
    
    
    protected Justification justification;

    protected BitImageMode mode;

    
    
    public BitImageWrapper(){
        justification = EscPosConst.Justification.Left_Default;
        mode = BitImageMode._24DotDoubleDensity_Default;
        
    }
    
    
    /**
     * Set horizontal justification of bar-code
     * @param justification left, center or right
     * @return this object
     */
    public BitImageWrapper setJustification(EscPosConst.Justification justification) {
        this.justification = justification;
        return this;
    }
    
    /**
     * Select bit-image mode. <p>
     * @param mode mode to be used on command ESC *
     * @return this object
     * @see #getBytes(EscPosImage)
     */
    public BitImageWrapper setMode(BitImageMode mode) {
        this.mode = mode;
        return this;
    }
    

    
    /**
     * Bit Image commands Assembly into ESC/POS bytes. <p>
     * 
     * Select justification <p>
     * ASCII ESC a n <p>
     * 
     * Set lineSpace in bytes <p>
     * ASCII ESC '3' n <p>
     * 
     * write all rows of the raster image <p>
     * ASCII ESC ✻ m nL nH d1 ... dk <p>
     * 
     * @param image to be printed
     * @return bytes of ESC/POS
     * @see EscPosImage
     */
    
    @Override
    public byte[] getBytes(EscPosImage image) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        //
        bytes.write(ESC);
        bytes.write('a');
        bytes.write(justification.value);

        // 
        bytes.write(ESC);
        bytes.write('3');
        bytes.write(16);
        
        // getting first and second bytes separatted
        int nL = image.getWidthOfImageInBits() & 0xFF;
        int nH = (image.getWidthOfImageInBits() & 0xFF00) >> 8 ;
        
        List< ByteArrayOutputStream > RasterColumns = image.getRasterRows(mode.bitsForVerticalData);
        for(ByteArrayOutputStream  rol: RasterColumns){
            //write one rol to print
            bytes.write(ESC);
            bytes.write('*');
            bytes.write(mode.value);
            bytes.write(nL);
            bytes.write(nH);
            bytes.write(rol.toByteArray(),0,rol.size());
            
            bytes.write(LF);
            
        }
        
        //
        return bytes.toByteArray();
    }
    
}
