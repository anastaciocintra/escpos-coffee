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

/**
 * Implements bitonal using one threshold value.
 */
public class BitonalThreshold extends Bitonal{
    private final int threshold;
    
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
