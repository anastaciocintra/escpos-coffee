# escpos-coffee

Java library for ESC/POS printer commands. Can send text, images and barcodes to the printer.
All commands are send to one OutputStream, than you can redirect to printer, file or network.

## Getting Start
Sending hello world to printer.
See samples directory to view complete code.
```
  EscPos escpos = new EscPos(System.out);
  escpos.writeLF("Hello Wold");
  escpos.feed(5);
  escpos.cut(EscPos.CutMode.FULL);
```
## Installing
Download compiled library from the last release;
Include jar lib on your project


## Compiling
I used the following development tools. But it might work with other versions or even other tools.
Apache Ant(TM) version 1.10.5
Netbeans 8.2
java version "1.8.0_172"

The lib and samples can be compiled with above commands:
```
cd src/escpos_coffee
ant clean
ant  jar
```



