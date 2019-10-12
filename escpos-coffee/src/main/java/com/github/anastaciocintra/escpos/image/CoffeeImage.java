package com.github.anastaciocintra.escpos.image;


public interface CoffeeImage {
    public abstract int getWidth();
    public abstract int getHeight();
    public abstract CoffeeImage getSubimage(int x, int y, int w, int h);
    public abstract int getRGB(int x, int y);
}
