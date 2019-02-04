# escpos-coffee
## Description
Java library for ESC/POS printer commands. Can send text, images and barcodes to the printer.
All commands are send to one OutputStream, than you can redirect to printer, file or network.

## Getting Start
Sending hello world to printer.
```
  EscPos escpos = new EscPos(System.out);
  escpos.writeLF("Hello Wold");
  escpos.feed(5);
  escpos.cut(EscPos.CutMode.FULL);
```

## Compiling
The lib and samples was made with Netbeans 8.2 and can compile with ant:
```
cd src/escpos_coffee
ant clean
ant  jar
```


