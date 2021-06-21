/*
 * Use of this source code is governed by the MIT license that can be
 * found in the LICENSE file.
 */
package com.github.anastaciocintra.escpos.image;

import com.github.anastaciocintra.escpos.EscPosConst;
import static com.github.anastaciocintra.escpos.EscPosConst.ESC;
import java.io.ByteArrayOutputStream;

/**
 * Supply ESC/POS Raster bit Image commands.<p>
 * using <code>GS 'v' '0'</code>
 */
public class RasterBitImageWrapper implements EscPosConst, ImageWrapperInterface{
    /**
     * Values for Raster Bit Image mode.
     * @see #setRasterBitImageMode(RasterBitImageMode)
     */
    public enum RasterBitImageMode{
        Normal_Default(0),  
        DoubleWidth(1),
        DoubleHeight(2), 
        Quadruple(3) 
        ;
        public int value;
        private RasterBitImageMode(int value){
            this.value = value;
        }
    }
    
    protected Justification justification;
    protected RasterBitImageMode rasterBitImageMode;


    public RasterBitImageWrapper(){
        justification = EscPosConst.Justification.Left_Default;
        rasterBitImageMode = RasterBitImageMode.Normal_Default;
    }
    
    /**
     * Set horizontal justification of bar-code
     * @param justification left, center or right
     * @return this object
     */
    public RasterBitImageWrapper setJustification(EscPosConst.Justification justification) {
        this.justification = justification;
        return this;
    }
    
    /**
     * Set the mode of Raster Bit Image.<p>
     * 
     * @param rasterBitImageMode mode to be used with GS v 0
     * @return this object
     * @see #getBytes(EscPosImage)
     */
    public RasterBitImageWrapper setRasterBitImageMode(RasterBitImageMode rasterBitImageMode) {
        this.rasterBitImageMode = rasterBitImageMode;
        return this;
    }
    
    
    /**
     * Bit Image commands Assembly into ESC/POS bytes. <p>
     *  
     * Select justification <p>
     * ASCII ESC a n <p>
     *  
     * Print raster bit image <p>
     * ASCII GS v 0 m xL xH yL yH d1...dk <p>
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
        bytes.write(GS);
        bytes.write('v');
        bytes.write('0');
        bytes.write(rasterBitImageMode.value);
        //
        //  bytes in horizontal direction for the bit image
        int horizontalBytes = image.getHorizontalBytesOfRaster();
        int xL = horizontalBytes & 0xFF;
        int xH = (horizontalBytes & 0xFF00) >> 8 ;
        // 
        //  bits in vertical direction for the bit image
        int verticalBits = image.getHeightOfImageInBits();
        // getting first and second bytes separatted
        int yL = verticalBits & 0xFF;
        int yH = (verticalBits & 0xFF00) >> 8 ;
        
        bytes.write(xL);
        bytes.write(xH);
        bytes.write(yL);
        bytes.write(yH);
        // write raster bytes
        byte [] rasterBytes = image.getRasterBytes().toByteArray();
        bytes.write(rasterBytes,0,rasterBytes.length);
        

        //
        return bytes.toByteArray();
        
    }
    
}
