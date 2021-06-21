/*
 * Use of this source code is governed by the MIT license that can be
 * found in the LICENSE file.
 */
package com.github.anastaciocintra.escpos.image;

/**
 * Implements bitonal using one threshold value.
 */
public class BitonalThreshold extends Bitonal{
    protected final int threshold;
    
    /**
     * construct BitonalThreshold
     * @param threshold unique threshold value with range 0 to 255.
     */
    public BitonalThreshold(int threshold){
        if(threshold < 0 || threshold > 255){
            throw new IllegalArgumentException("threshold range must be between 0 and 255");
        }
        this.threshold = threshold;
    }

    /**
     * construct BitonalThreshold with default value.
     */
    public BitonalThreshold(){
        this.threshold = 127;
    }

    /**
     * translate RGBA colors to 0 or 1 (print or not). <p>
     * the return is based on threshold value.
     * @param alpha range from 0 to 255
     * @param red range from 0 to 255
     * @param green range from 0 to 255
     * @param blue range from 0 to 255
     * @param x the X coordinate of the image 
     * @param y the Y coordinate of the image 
     * @return  0 or 1     
     * @see Bitonal#zeroOrOne(int, int, int, int, int, int) 
     */
    
    @Override
    public int zeroOrOne(int alpha, int red, int green, int blue, int x, int y) {
        int luminance = 0xFF;
        if (alpha > 127) {
            luminance = (red + green + blue) / 3;
        }
        return (luminance < threshold) ? 1 : 0;    }
    
}
