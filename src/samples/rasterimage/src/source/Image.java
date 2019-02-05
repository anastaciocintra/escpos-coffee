
package source;

import escpos.EscPos;
import escpos.EscPosConst;
import escpos.Style;
import escpos.image.Bitonal;
import escpos.image.BitonalThreshold;
import escpos.image.EscPosImage;
import escpos.image.RasterBitImageWrapper;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.print.PrintService;
import output.PrinterOutputStream;


public class Image {
    public void GraphcsImageWrapper(String printerName){

        // get the printer service by name passed on command line...
        //this call is slow, try to use it only once and reuse the PrintService variable.
        PrintService printService = PrinterOutputStream.getPrintServiceByName(printerName);
        EscPos escpos;
        try {
            /*
             * to print one image we need to have:
             * - one BufferedImage.
             * - one bitonal algorithm to define what and how print on image.
             * - one image wrapper to determine the command set to be used on 
             * image printing and how to customize it.
             */
            
             // specify the algorithm that defines what and how "print or not print" on each coordinate of the BufferedImage.
             // in this case, threshold 127
            Bitonal algorithm = new BitonalThreshold(127); 
            // creating the EscPosImage, need buffered image and algorithm.
            URL githubURL = getURL("github.png"); 
            BufferedImage  githubBufferedImage = ImageIO.read(githubURL);
            EscPosImage escposImage = new EscPosImage(githubBufferedImage, algorithm);     
            
            // this wrapper uses esc/pos sequence: "GS 'v' '0'"
            RasterBitImageWrapper imageWrapper = new RasterBitImageWrapper();

            
            
            escpos = new EscPos(new PrinterOutputStream(printService));
            
            
            escpos.writeLF(new Style().setFontSize(Style.FontSize._2, Style.FontSize._2)
                    ,"RasterBitImageWrapper");
            
            escpos.writeLF("default size");
            escpos.write(imageWrapper, escposImage);
            
            escpos.feed(5);
            escpos.writeLF("Double Height");
            imageWrapper.setRasterBitImageMode(RasterBitImageWrapper.RasterBitImageMode.DoubleHeight);
            escpos.write(imageWrapper, escposImage);

            escpos.feed(5);
            escpos.writeLF("Double Width");
            imageWrapper.setRasterBitImageMode(RasterBitImageWrapper.RasterBitImageMode.DoubleWidth);
            escpos.write(imageWrapper, escposImage);

            escpos.feed(5);
            escpos.writeLF("Quadruple size");
            imageWrapper.setRasterBitImageMode(RasterBitImageWrapper.RasterBitImageMode.Quadruple);
            escpos.write(imageWrapper, escposImage);

            escpos.feed(5);
            escpos.writeLF("print on Left");
            imageWrapper.setRasterBitImageMode(RasterBitImageWrapper.RasterBitImageMode.Normal_Default);
            imageWrapper.setJustification(EscPosConst.Justification.Left_Default);
            escpos.write(imageWrapper, escposImage);
            escpos.feed(5);
            escpos.writeLF("print on Right");
            imageWrapper.setJustification(EscPosConst.Justification.Right);
            escpos.write(imageWrapper, escposImage);
            escpos.feed(5);
            escpos.writeLF("print on Center");
            imageWrapper.setJustification(EscPosConst.Justification.Center);
            escpos.write(imageWrapper, escposImage);
            
            escpos.feed(5);
            escpos.cut(EscPos.CutMode.FULL);
            
            
            escpos.close();
            
        } catch (IOException ex) {
            Logger.getLogger(Image.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private URL  getURL(String imageName){
        String strPath =  "images/" +  imageName;
        return getClass()
                      .getClassLoader()
                      .getResource(strPath);
    }
    

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java -jar image.jar (\"printer name\")");
            System.out.println("Printer list to use:");
            String[] printServicesNames = PrinterOutputStream.getListPrintServicesNames();
            for (String printServiceName : printServicesNames) {
                System.out.println(printServiceName);
            }

            System.exit(0);
        }
        Image obj = new Image();
        obj.GraphcsImageWrapper(args[0]);
        
    }
    
}
