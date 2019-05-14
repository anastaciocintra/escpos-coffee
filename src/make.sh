rm -rf ~/tmp/escposcoffee*
mkdir ~/tmp/escposcoffee
mkdir ~/tmp/escposcoffee/javadoc
mkdir ~/tmp/escposcoffee/samples
mkdir ~/tmp/escposcoffee/samples/lib
cd escpos_coffee
ant clean; ant jar; ant javadoc
cp -R dist/javadoc /Users/marco/tmp/escposcoffee/
cp dist/escpos_coffee.jar ~/tmp/escposcoffee/samples/lib
cp dist/escpos_coffee.jar ~/tmp/escposcoffee
cd ../samples/barcode/;ant clean;ant jar;cp dist/*.jar ~/tmp/escposcoffee/samples
cd ../bitimage/;ant clean;ant jar;cp dist/*.jar ~/tmp/escposcoffee/samples
cd ../charcode;ant clean;ant jar;cp dist/*.jar ~/tmp/escposcoffee/samples
cd ../dithering;ant clean;ant jar;cp dist/*.jar ~/tmp/escposcoffee/samples
cd ../getstart;ant clean;ant jar;cp dist/*.jar ~/tmp/escposcoffee/samples
cd ../graphicsImage;ant clean;ant jar;cp dist/*.jar ~/tmp/escposcoffee/samples
cd ../rasterimage;ant clean;ant jar;cp dist/*.jar ~/tmp/escposcoffee/samples
cd ../textstyle;ant clean;ant jar;cp dist/*.jar ~/tmp/escposcoffee/samples
cd ../pulsePin;ant clean;ant jar;cp dist/*.jar ~/tmp/escposcoffee/samples

cd ~/tmp
tar cvzf escposcoffee.tgz escposcoffee/
zip -r escposcoffee.zip escposcoffee/
