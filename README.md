# escpos-coffee

[https://github.com/anastaciocintra/escpos-coffee](https://github.com/anastaciocintra/escpos-coffee)

![GitHub](https://img.shields.io/github/license/anastaciocintra/escpos-coffee)
[![Java CI with Maven](https://github.com/anastaciocintra/escpos-coffee/actions/workflows/maven.yml/badge.svg)](https://github.com/anastaciocintra/escpos-coffee/actions/workflows/maven.yml)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.anastaciocintra/escpos-coffee.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.github.anastaciocintra%22%20AND%20a:%22escpos-coffee%22)
![Sonatype Nexus (Snapshots)](https://img.shields.io/nexus/s/com.github.anastaciocintra/escpos-coffee?server=https%3A%2F%2Foss.sonatype.org)


Java library for ESC/POS printer commands. Can send text, images and barcodes to the printer.
All commands are send to one OutputStream, then you can redirect to printer, file or network.

## Compatible / Tested platforms

* Linux 
* FreeBsd 
* Windows
* MacOS
* Android Mobile 


## [Wiki](https://github.com/anastaciocintra/escpos-coffee/wiki)

## [Code Examples](https://github.com/anastaciocintra/escpos-coffee-samples) 




## Getting Started

sending "hello world" to the printer

```java
import com.github.anastaciocintra.escpos.EscPos;
import com.github.anastaciocintra.output.PrinterOutputStream;

import javax.print.PrintService;
import java.io.IOException;

public class HelloWorld {
    public static void main(String[] args) throws IOException {
        if(args.length!=1){
            System.out.println("Usage: java -jar escpos-simple.jar (\"printer name\")");
            System.out.println("Printer list to use:");
            String[] printServicesNames = PrinterOutputStream.getListPrintServicesNames();
            for(String printServiceName: printServicesNames){
                System.out.println(printServiceName);
            }

            System.exit(0);
        }

        PrintService printService = PrinterOutputStream.getPrintServiceByName(args[0]);
        PrinterOutputStream printerOutputStream = new PrinterOutputStream(printService);
        EscPos escpos = new EscPos(printerOutputStream);
        escpos.writeLF("Hello world");
        escpos.feed(5).cut(EscPos.CutMode.FULL);
        escpos.close();
    }
}

```


### Installation with Maven 

```xml
<dependency>
  <groupId>com.github.anastaciocintra</groupId>
  <artifactId>escpos-coffee</artifactId>
  <version>4.1.0</version>
</dependency>
```

### Installation with Gradle 

Step 1. Add the repository to your build file
```
repositories {
    mavenCentral()
}
```

Step 2. Add the dependency
```
dependencies {
    implementation 'com.github.anastaciocintra:escpos-coffee:4.1.0'
}
```


### Installation without Maven or Gradle

Download code from the [last release of escpos-coffee](https://github.com/anastaciocintra/escpos-coffee/releases/latest).


The project can be compiled with the below command:

```

mvn clean package

```

Then the jar file will be generated inside the 'target/' folder, just add the jar file to your classpath.


## Samples

You can find all samples codes on [https://github.com/anastaciocintra/escpos-coffee-samples](https://github.com/anastaciocintra/escpos-coffee-samples) 


### getstart sample
[getstart](https://github.com/anastaciocintra/escpos-coffee-samples/tree/master/usual/getstart) - Send info of the library to the printer.

![output](sample_images/info.png?raw=true "output")


### textstyle sample
[textstyle](https://github.com/anastaciocintra/escpos-coffee-samples/tree/master/usual/textstyle) - Shows how to construnct one simple receipt.

![output](sample_images/style.png?raw=true "output")


Also this sample show how simple is to create diferent text styles, like title, subtitle, bold, etc.


```java
  Style title = new Style()
          .setFontSize(Style.FontSize._3, Style.FontSize._3)
          .setJustification(EscPosConst.Justification.Center);
```



### graphicsimage, bitimage and rasterimage samples

Shows how to work with ImageWrapper.

Then you will see things like how to print on center-justified one image, like this: 

```java
    escpos.writeLF("print on Center");
    imageWrapper.setJustification(EscPosConst.Justification.Center);
    escpos.write(imageWrapper, escposImage);
```

### dithering sample
[dithering](https://github.com/anastaciocintra/escpos-coffee-samples/tree/master/usual/dithering) - 
Shows how to work with BitonalThreshold and BitonalOrderedDither. 

![output](sample_images/threshould.png?raw=true "output")

![output](sample_images/ordered_dither.png?raw=true "output")


Bellow, we can see how to use ordered dither class.

```java
  algorithm = new BitonalOrderedDither();
  EscPosImage escposImage = new EscPosImage(new CoffeeImageImpl(imageBufferedImage), algorithm);     
  escpos.write(imageWrapper, escposImage);

```
### barcode sample
[barcode](https://github.com/anastaciocintra/escpos-coffee-samples/tree/master/usual/barcode) - 
Shows barcode, PDF417 and qrcode.

![output](sample_images/barcode.png?raw=true "output")

![output](sample_images/qrcode.png?raw=true "output")

![output](sample_images/pdf417.png?raw=true "output")


Bellow, code to send barcode to the printer

```java
  BarCode barcode = new BarCode();
  escpos.write(barcode, "hello barcode");
```

### codetable  sample
[charcode](https://github.com/anastaciocintra/escpos-coffee-samples/tree/master/usual/charcode) - 
Shows how to send texts from different languages.

```java
  escpos.setCharacterCodeTable(CharacterCodeTable.CP863_Canadian_French);
  escpos.writeLF("Liberté et Fraternité.");
```

### Others
[Android](https://github.com/anastaciocintra/escpos-coffee-samples/tree/master/miscellaneous/AndroidImage) - How to use this lib on Android Studio project.

[BarcodeGen](https://github.com/anastaciocintra/escpos-coffee-samples/tree/master/miscellaneous/BarcodeGen) - How to generate barcode image and print it

[PdfPrinting](https://github.com/anastaciocintra/escpos-coffee-samples/tree/master/miscellaneous/PdfPrinting) - How to print pdf files

[CoffeeBitmap](https://github.com/anastaciocintra/escpos-coffee-samples/tree/master/miscellaneous/CoffeeBitmap) - How to construct html/css receipts

![output](sample_images/htmlcss.png?raw=true "output")


## Versioning

Using [SemVer](https://semver.org) for versioning.

Lastest release [here](https://github.com/anastaciocintra/escpos-coffee/releases/latest).


## Contributting 
Contributors are welcome, 
but before you do it its important to read and agree with [CODE_OF_CONDUCT.md](https://github.com/anastaciocintra/escpos-coffee/blob/master/CODE_OF_CONDUCT.md) and [CONTRIBUTING.md](https://github.com/anastaciocintra/escpos-coffee/blob/master/CONTRIBUTING.md).

## Acknowledgments
I would like to thanks [Michael Billington](https://github.com/mike42) and contributors for the great work on the [mike42/escpos-php](https://github.com/mike42/escpos-php) project that inspired me to start this project.
