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
package escpos.image;

import escpos.EscPosConst;
import static escpos.EscPosConst.ESC;
import java.io.ByteArrayOutputStream;

/**
 * Supply ESC/POS Graphics print Image commands.<p>
 * using <code>GS(L</code>
 */
public class GraphicsImageWrapper implements EscPosConst, ImageWrapperInterface{
    
    /**
     * Values for Raster Bit Image mode.
     * @see #setGraphicsImageBxBy(escpos.image.GraphicsImageWrapper.GraphicsImageBxBy) 
     */
    public enum GraphicsImageBxBy{
        Normal_Default(1,1),  
        DoubleWidth(2,1),
        DoubleHeight(1,2), 
        Quadruple(2, 2); 
        public int bx;
        public int by;
        private GraphicsImageBxBy(int bx, int by){
            this.bx = bx;
            this.by = by;
        }
    }
    
    private Justification justification;
    GraphicsImageBxBy graphicsImageBxBy;


    public GraphicsImageWrapper(){
        justification = EscPosConst.Justification.Left_Default;
        graphicsImageBxBy = GraphicsImageBxBy.Normal_Default;
    }
    
    /**
     * Set horizontal justification of bar-code
     * @param justification left, center or right
     * @return this object
     */
    public GraphicsImageWrapper setJustification(EscPosConst.Justification justification) {
        this.justification = justification;
        return this;
    }
    
    /**
     * set values of Bx and By referring to the image size. <p> 
     * @param graphicsImageBxBy values used on function 112
     * @return this object
     * @see #getBytes(escpos.image.EscPosImage) 
     */
    public GraphicsImageWrapper setGraphicsImageBxBy(GraphicsImageBxBy graphicsImageBxBy) {
        this.graphicsImageBxBy = graphicsImageBxBy;
        return this;
    }
    
    
    /**
     * Bit Image commands Assembly into ESC/POS bytes. <p>
     *  
     * Select justification <p>
     * ASCII ESC a n <p>
     *  
     * function 112 Store the graphics data in the print buffer  <p>
     * GS(L pL pH m fn a bx by c xL xH yL yH d1...dk  <p>
     * 
     * function 050 Prints the buffered graphics data <p>
     * GS ( L pL pH m fn  <p>
     * 
     * @param image to be printed
     * @return bytes of ESC/POS
     * @see EscPosImage#getRasterBytes() 
     * @see EscPosImage#getRasterSizeInBytes() 
     */ 
    @Override
    public byte[] getBytes(EscPosImage image) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        //
        bytes.write(ESC);
        bytes.write('a');
        bytes.write(justification.value);
        //
        int paramSize = image.getRasterSizeInBytes() + 10;
        int pL = paramSize & 0xFF;
        int pH = (paramSize & 0xFF00) >> 8 ;

        bytes.write(GS);
        bytes.write('(');
        bytes.write('L');
        bytes.write(pL); // pl
        bytes.write(pH); // ph
        bytes.write(48); // m
        bytes.write(112); //fn
        bytes.write(48); // a
        bytes.write(graphicsImageBxBy.bx); // bx
        bytes.write(graphicsImageBxBy.by); // by
        bytes.write(49); // c

        //  bits in horizontal direction for the bit image
        int horizontalBits = image.getWidthOfImageInBits();
        int xL = horizontalBits & 0xFF;
        int xH = (horizontalBits & 0xFF00) >> 8 ;
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
        // write bytes
        byte [] rasterBytes = image.getRasterBytes().toByteArray();
        bytes.write(rasterBytes,0,rasterBytes.length);
        
        // function 050
        bytes.write(GS);
        bytes.write('(');
        bytes.write('L');
        bytes.write(2); // pl
        bytes.write(0); // ph
        bytes.write(48); //m
        bytes.write(50); //fn
        
        
        

        //
        return bytes.toByteArray();
        
    }
    
}
