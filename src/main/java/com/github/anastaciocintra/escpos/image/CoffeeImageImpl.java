package com.github.anastaciocintra.escpos.image;

import java.awt.image.BufferedImage;

/**
 * implements CoffeeImage using Java BufferedImage
 * @see CoffeeImage
 * @see BufferedImage
 */
public class CoffeeImageImpl implements CoffeeImage {

    protected BufferedImage image;
    public CoffeeImageImpl(BufferedImage image) {
        this.image = image;
    }

    @Override
    public int getWidth() {
        return image.getWidth();
    }

    @Override
    public int getHeight() {
        return image.getHeight();
    }

    @Override
    public CoffeeImage getSubimage(int x, int y, int w, int h) {
        return new CoffeeImageImpl(image.getSubimage(x,y,w,h));
    }

    @Override
    public int getRGB(int x, int y) {
        return image.getRGB(x,y);
    }
}
