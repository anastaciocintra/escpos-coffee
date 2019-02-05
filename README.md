# escpos-coffee

Java library for ESC/POS printer commands. Can send text, images and barcodes to the printer.
All commands are send to one OutputStream, than you can redirect to printer, file or network.

## Getting Start
The EscPos works with OutputStream to send its commands. Here we have two examples that show different output streams.

Creating printer output stream:
```
  PrintService printService = PrinterOutputStream.getPrintServiceByName("printerName");
  PrinterOutputStream printerOutputStream = new PrinterOutputStream(printService);
  EscPos escpos = new EscPos(printerOutputStream);
  escpos.writeLF("Hello Wold");
  escpos.feed(5);
  escpos.cut(EscPos.CutMode.FULL);
```

Sending hello world to system out:
```
  EscPos escpos = new EscPos(System.out);
  escpos.writeLF("Hello Wold");
  escpos.feed(5);
  escpos.cut(EscPos.CutMode.FULL);
```
See on samples directory to view more codes.

# Installing
Download compiled library from the last release;
Include jar lib on your project.


# Compiling
I used the following development tools. But it might work with other versions or even another tools.
Apache Ant(TM) version 1.10.5
Netbeans 8.2
java version "1.8.0_172"

The lib and samples can be compiled with above commands:
```
cd src/escpos_coffee
ant clean
ant  jar
```
## Samples
You can find samples code on src/samples directory.
running:
```
java -jar samplename.jar "printer name"
```
coding:
# getstart

Simply print info of the library.



