/*
 * Use of this source code is governed by the MIT license that can be
 * found in the LICENSE file.
 */
package com.github.anastaciocintra.escpos.image;


/**
 * Abstract base for  algorithms that transform RGB to bitonal.<p>
 * Used on {@link EscPosImage#EscPosImage EscPosImage constructor}.
 * Any dither algorithm can be implemented, like ordered grid or noise dither.
 * Generally, you need to call image.getRGB(x, y) and decide if return zero or one.
 * you need to Override only the {@link #zeroOrOne(int, int, int, int, int, int) zeroOrOne} method
 * but if you want, you can Override {@link  #getBitonalVal(CoffeeImage, int, int)}  getBitonalVal} too.
 */
public abstract class Bitonal {
    /**
     * Pre-work each coordinate (x, y) of image by discover RGBA values.
     * get the 8-bits values separated by alpha, red, blue and green and return by
     * calling {@link #zeroOrOne(int, int, int, int, int, int) } to make print or not decision.
     * @param image RGB image.
     * @param x the X coordinate of the pixel from which to get
     *          the pixel
     * @param y the Y coordinate of the pixel from which to get
     *          the pixel
     * @return  call zeroOrOne to make decision (0 or 1)
     * @see #zeroOrOne(int, int, int, int, int, int) 
     */
    public int getBitonalVal(CoffeeImage image,int x, int y){
        int RGBA = image.getRGB(x, y);
        int alpha = (RGBA >> 24) & 0xFF;
        int red = (RGBA >> 16) & 0xFF;
        int green = (RGBA >> 8) & 0xFF;
        int blue = RGBA & 0xFF;
        
        return zeroOrOne(alpha, red, green, blue, x, y);
        
    }
    /**
     * Subclasses need to translate the 8-bits RGBA colors to 0 or 1 (print or not) <p>
     * for any coordinate x, y of the BufferedImage.
     * 
     * @param alpha range from 0 to 255
     * @param red range from 0 to 255
     * @param green range from 0 to 255
     * @param blue range from 0 to 255
     * @param x the X coordinate of the image 
     * @param y the Y coordinate of the image 
     * @return  0 or 1     
     */
    public abstract int zeroOrOne(int alpha, int red, int green, int blue, int x, int y);
}
