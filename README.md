
# escpos-coffee

Java library for ESC/POS printer commands. Can send text, images and barcodes to the printer.
All commands are send to one OutputStream, then you can redirect to printer, file or network.
## [Wiki](https://github.com/anastaciocintra/escpos-coffee/wiki)




![GitHub](https://img.shields.io/github/license/anastaciocintra/escpos-coffee)
![Maven Central](https://img.shields.io/maven-central/v/com.github.anastaciocintra/escpos-coffee)
![Sonatype Nexus (Snapshots)](https://img.shields.io/nexus/s/com.github.anastaciocintra/escpos-coffee?server=https%3A%2F%2Foss.sonatype.org)

## Getting Started
The EscPos works with OutputStream to send its commands. Here we have two examples that show different output streams.

Creating printer output stream:
```java
  PrintService printService = PrinterOutputStream.getPrintServiceByName("printerName");
  PrinterOutputStream printerOutputStream = new PrinterOutputStream(printService);
  EscPos escpos = new EscPos(printerOutputStream);
  escpos.writeLF("Hello Wold");
  escpos.feed(5);
  escpos.cut(EscPos.CutMode.FULL);
  escpos.close();

```

Sending hello world to system out:
```java
  EscPos escpos = new EscPos(System.out);
  escpos.writeLF("Hello Wold");
  escpos.feed(5);
  escpos.cut(EscPos.CutMode.FULL);
  escpos.close();
```
See on samples directory to view more codes.



### Downloading
Download code and binaries from the [last release of escpos-coffee](https://github.com/anastaciocintra/escpos-coffee/releases/latest).

### Installation with Maven 

```xml
<dependency>
  <groupId>com.github.anastaciocintra</groupId>
  <artifactId>escpos-coffee</artifactId>
  <version>4.2.0-SNAPSHOT</version>
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
    implementation 'com.github.anastaciocintra:escpos-coffee:4.2.0-SNAPSHOT'
}
```


### Installation without Maven or Gradle

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
