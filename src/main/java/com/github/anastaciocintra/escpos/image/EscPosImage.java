/*
 * Use of this source code is governed by the MIT license that can be
 * found in the LICENSE file.
 */
package com.github.anastaciocintra.escpos.image;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Supply raster patterns images 
 */
public class EscPosImage {
    protected final CoffeeImage image;

    protected final Bitonal bitonalAlgorithm;

    protected ByteArrayOutputStream baCachedEscPosRaster = new ByteArrayOutputStream();
    protected List< ByteArrayOutputStream > CashedEscPosRasterRows_8 = new ArrayList();
    protected List< ByteArrayOutputStream > CachedEscPosRasterRows_24 = new ArrayList();


    /**
     * creates an EscPosImage
     * @param image normal RGB image
     * @param bitonalAlgorithm Algorithm that transform RGB to bitonal  
     * @see #getBitonalVal(int, int) 
     */
    public EscPosImage(CoffeeImage image, Bitonal bitonalAlgorithm){
        this.image = image;
        this.bitonalAlgorithm = bitonalAlgorithm;
    }


    
    public int getHorizontalBytesOfRaster(){
        return ((image.getWidth() % 8) > 0) ? (image.getWidth() / 8)+1:(image.getWidth() / 8);
    }
    
    public int getWidthOfImageInBits(){
        return image.getWidth();
    }

    public int getHeightOfImageInBits(){
        return image.getHeight();
    }
    
    public int getRasterSizeInBytes(){
        if(baCachedEscPosRaster.size() > 0) return baCachedEscPosRaster.size();
        baCachedEscPosRaster = image2EscPosRaster();
        return baCachedEscPosRaster.size();
    }
    /**
     * get raster bytes of image in Rows pattern.<p>
     * Utilize cached bytes if available
     * 
     * @param bitsPerColumn_8_or_24 possible values are 8 or 24
     * @return a list of rows in raster pattern
     */
    public List< ByteArrayOutputStream > getRasterRows(int bitsPerColumn_8_or_24){
        if(bitsPerColumn_8_or_24 == 8){
            if(CashedEscPosRasterRows_8.size() > 0) {
                return CashedEscPosRasterRows_8;
            }

            CashedEscPosRasterRows_8 = image2Rows(bitsPerColumn_8_or_24);
            return CashedEscPosRasterRows_8;
            
        }else{
            if(CachedEscPosRasterRows_24.size() > 0){
                return CachedEscPosRasterRows_24;
            }

            CachedEscPosRasterRows_24 = image2Rows(bitsPerColumn_8_or_24);
            return CachedEscPosRasterRows_24;
            
        }
    }
    
    /**
     * transform RGB image to raster Rows format
     * @param bitsPerColumn_8_or_24 possible values are 8 or 24
     * @return a list of rows in raster pattern
     */
    protected  List< ByteArrayOutputStream > image2Rows(int bitsPerColumn_8_or_24){
        List< ByteArrayOutputStream > lRasterRows = new ArrayList();
        
        
        List<CoffeeImage> lRGBImageRows = new ArrayList();
        for(int y = 0; y < image.getHeight();y+=bitsPerColumn_8_or_24){
            int height = bitsPerColumn_8_or_24;
            if((y + height) > image.getHeight()){
                height = image.getHeight() - y;
            }
            CoffeeImage row = (CoffeeImage) image.getSubimage(0, y, image.getWidth(), height);
            lRGBImageRows.add(row);
        }

        int heightOffset = 0;

        for(CoffeeImage RGBRow   : lRGBImageRows){
            ByteArrayOutputStream baColumBytes = new ByteArrayOutputStream();
            for(int x = 0; x < RGBRow.getWidth();x++){
                int col = 0;
                int max_y = Integer.min(bitsPerColumn_8_or_24, RGBRow.getHeight());
                int bit = 0;
                int bitsWritten = 0;
                for(int y = 0; y < max_y; y++){
                    int val = getBitonalVal(x, y+heightOffset);
                    col = col | (val << (7 - bit));
                    bit++;
                    if(bit == 8){
                        baColumBytes.write(col);
                        bitsWritten +=8;
                        col = 0;
                        bit = 0;
                    }

                }
                if(bit > 0){
                    baColumBytes.write(col);
                    bitsWritten+=8;
                    while(bitsWritten < bitsPerColumn_8_or_24){
                        baColumBytes.write(0);
                        bitsWritten +=8;
                    }
                }
            }
            lRasterRows.add(baColumBytes);
            heightOffset+=bitsPerColumn_8_or_24;
        }
        
        return lRasterRows;

        
    }
    
    /**
     * get raster bytes of image. <p>
     * Utilize cached bytes if available.
     * @return bytes of raster image.
     */
    public ByteArrayOutputStream getRasterBytes(){
        if(baCachedEscPosRaster.size() > 0) return baCachedEscPosRaster;
        baCachedEscPosRaster = image2EscPosRaster();
        return baCachedEscPosRaster;
    }
    
    /** 
     * Call the custom algorithm to determine print or not print on each coordinates. <p>
     * @param x the X coordinate of the image 
     * @param y the Y coordinate of the image 
     * @return  0 or 1     
     * @see #EscPosImage(CoffeeImage, Bitonal) (BufferedImage, Bitonal)  
     * @see Bitonal#getBitonalVal(CoffeeImage, int, int)  
     */
    protected int getBitonalVal(int x, int y) {
        return bitonalAlgorithm.getBitonalVal(image, x, y);
        
    }
    
    /**
     * transform RGB image in raster format.
     * @return raster byte array 
     */
    protected ByteArrayOutputStream image2EscPosRaster(){
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        int  Byte;
        int  bit;
        for(int y = 0; y < image.getHeight(); y++){
            Byte = 0;
            bit = 0;
            for(int x = 0; x < image.getWidth(); x++){
                int val = getBitonalVal(x, y);
                Byte = Byte | (val << (7 - bit));
                bit++;
                if(bit == 8){
                    byteArray.write(Byte);
                    Byte = 0;
                    bit = 0;
                }  
            }
            if (bit > 0) {
                byteArray.write(Byte);

            }
            
        }
        return byteArray;
    }
    
}
