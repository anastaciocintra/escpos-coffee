/*
 * Use of this source code is governed by the MIT license that can be
 * found in the LICENSE file.
 */
package com.github.anastaciocintra.escpos;

import com.github.anastaciocintra.escpos.image.EscPosImage;
import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Objects;
import java.util.Properties;

import com.github.anastaciocintra.escpos.barcode.BarCodeWrapperInterface;
import com.github.anastaciocintra.escpos.image.ImageWrapperInterface;

/**
 * Write some usual ESC/POS commands to the OutPutStream.<p>
 * ESC/POS was developed by <i>Seiko Epson Corporation</i><p>
 * Most receipt printers on the market recognize these commands.<p>
 * and can be used to print texts, barcodes or images
 */


public class EscPos implements Closeable, Flushable, EscPosConst {

    /**
     * values of character code table.
     *
     * @see #setCharacterCodeTable(CharacterCodeTable)
     */
    public enum CharacterCodeTable {
        CP437_USA_Standard_Europe(0, "cp437"),
        Katakana(1),
        CP850_Multilingual(2, "cp850"),
        CP860_Portuguese(3, "cp860"),
        CP863_Canadian_French(4, "cp863"),
        CP865_Nordic(5, "cp865"),
        CP851_Greek(11),
        CP853_Turkish(12),
        CP857_Turkish(13, "cp857"),
        CP737_Greek(14, "cp737"),
        ISO8859_7_Greek(15, "iso8859_7"),
        WPC1252(16, "cp1252"),
        CP866_Cyrillic_2(17, "cp866"),
        CP852_Latin2(18, "cp852"),
        CP858_Euro(19, "cp858"),
        KU42_Thai(20),
        TIS11_Thai(21),
        TIS18_Thai(26),
        TCVN_3_1_Vietnamese(30),
        TCVN_3_2_Vietnamese(31),
        PC720_Arabic(32),
        WPC775_BalticRim(33),
        CP855_Cyrillic(34, "cp855"),
        CP861_Icelandic(35, "cp861"),
        CP862_Hebrew(36, "cp862"),
        CP864_Arabic(37, "cp864"),
        CP869_Greek(38, "cp869"),
        ISO8859_2_Latin2(39, "iso8859_2"),
        ISO8859_15_Latin9(40, "iso8859_15"),
        CP1098_Farsi(41, "cp1098"),
        CP1118_Lithuanian(42),
        CP1119_Lithuanian(43),
        CP1125_Ukrainian(44),
        WCP1250_Latin2(45, "cp1250"),
        WCP1251_Cyrillic(46, "cp1251"),
        WCP1253_Greek(47, "cp1253"),
        WCP1254_Turkish(48, "cp1254"),
        WCP1255_Hebrew(49, "cp1255"),
        WCP1256_Arabic(50, "cp1256"),
        WCP1257_BalticRim(51, "cp1257"),
        WCP1258_Vietnamese(52, "cp1258"),
        KZ_1048_Kazakhstan(53),
        User_defined_page(255);
        public int value;
        public String charsetName;

        private CharacterCodeTable(int value) {
            this.value = value;
            this.charsetName = "cp437";
        }

        private CharacterCodeTable(int value, String charsetName) {
            this.value = value;
            this.charsetName = charsetName;
        }
    }

    /**
     * Values for CutMode.
     *
     * @see #cut(CutMode)
     */
    public enum CutMode {
        FULL(48),
        PART(49);
        public int value;

        private CutMode(int value) {
            this.value = value;
        }
    }

    /**
     * Values for pin connector
     *
     * @see #pulsePin(PinConnector, int, int)
     */
    public enum PinConnector {
        Pin_2(48),
        Pin_5(49);
        public int value;

        private PinConnector(int value) {
            this.value = value;
        }
    }

    protected OutputStream outputStream;
    protected String charsetName;
    protected Style style;

    /**
     * creates an instance based on outputStream.
     *
     * @param outputStream can be one file, System.out or printer...
     * @see java.io.OutputStream
     */
    public EscPos(OutputStream outputStream) {
        this.outputStream = outputStream;
        this.setCharsetName(CharacterCodeTable.CP437_USA_Standard_Europe.charsetName);
        style = new Style();
    }

    /**
     * Writes one byte directly to outputStream. Can be used to send customized
     * commands to printer.
     *
     * @param b the <code>byte</code>.
     * @return this object.
     * @exception IOException if an I/O error occurs. In particular, an
     * <code>IOException</code> may be thrown if the output stream has been
     * closed.
     * @see java.io.OutputStream#write(int)
     */
    public EscPos write(int b) throws IOException {
        this.outputStream.write(b);
        return this;
    }

    /**
     * Writes bytes directly to outputStream. Can be used to send customizes
     * commands to printer.
     *
     * @param b the data.
     * @param off the start offset in the data.
     * @param len the number of bytes to write.
     * @return this object.
     * @exception IOException if an I/O error occurs. In particular, an
     * <code>IOException</code> is thrown if the output stream is closed.
     * @see java.io.OutputStream#write(byte[], int, int)
     */
    public EscPos write(byte b[], int off, int len) throws IOException {
        this.outputStream.write(b, off, len);
        return this;
    }

    /**
     * call outputStrem.flush().
     *
     * @exception IOException if an I/O error occurs.
     * @see java.io.OutputStream#flush()
     */
    @Override
    public void flush() throws IOException {
        outputStream.flush();
    }

    /**
     * call close of outputStream.
     *
     * @exception IOException if an I/O error occurs.
     * @see java.io.OutputStream#close()
     */
    @Override
    public void close() throws IOException {
        this.outputStream.close();
    }

    /**
     * Each write will be send to output Stream.
     *
     * @param outputStream value to be used on writes
     * @return this object.
     */
    public EscPos setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
        return this;
    }

    /**
     * get output stream of this object.
     *
     * @return actual value of output stream.
     */
    public OutputStream getOutputStream() {
        return outputStream;
    }

    /**
     * Set style of actual instance.
     * <p>
     * @param style to be used on writes.
     * @return this object
     * @see #write(Style, String)
     */
    public EscPos setStyle(Style style) {
        this.style = style;
        return this;
    }

    /**
     * Get actual style of this object.
     *
     * @return actual value.
     * @see #setStyle(Style)
     */
    public Style getStyle() {
        return style;
    }

    /**
     * Set charsetName used on encodes of Strings.
     *
     * @param charsetName value used on String.getBytes
     * @return this object.
     * @see java.lang.String#getBytes(java.lang.String)
     * @see #write(java.lang.String)
     * @see #writeLF(java.lang.String)
     */
    public final EscPos setCharsetName(String charsetName) {
        this.charsetName = charsetName;
        return this;
    }

    /**
     * Get charsetName used by this object.
     *
     * @return actual charsetName.
     * @see #setCharsetName(java.lang.String)
     */
    public String getDefaultCharsetName() {
        return charsetName;
    }

    /**
     * Select character code table.
     * <p>
     * each table represent specifics codes for special characters, example when
     * we need to print accents characters. This function combine setCharsetName
     * to be used on  <code>String.getBytes</code> and setPrinterCharacterTable
     * to be used on printer. Then next time that you call <code>write</code>
     * with String parameter, then you can send special characters.
     *
     * @param table character table possibilities
     * @return this object.
     * @exception IOException if an I/O error occurs.
     * @exception IllegalArgumentException if characterCodeTable out of range 0
     * to 255
     */
    public EscPos setCharacterCodeTable(CharacterCodeTable table) throws IOException, IllegalArgumentException {
        setCharsetName(table.charsetName);
        setPrinterCharacterTable(table.value);
        return this;

    }

    /**
     * Select character code table on the printer.<p>
     * Is recommended that you use <code>setCharacterCodeTable</code>, but if
     * you need to use one specific code that is not included in
     * <code>CharacterCodeTable</code> or if the <code>CharacterCodeTable</code>
     * codes diverges of your printer documentation, then you should use this
     * function.<p>
     * ASCII ESC t n
     *
     * @param characterCodeTable code of table on printer to be selected.
     * @return this object.
     * @exception IOException if an I/O error occurs.
     * @exception IllegalArgumentException if characterCodeTable out of range 0
     * to 255
     */
    public EscPos setPrinterCharacterTable(int characterCodeTable) throws IOException, IllegalArgumentException {
        if (characterCodeTable < 0 || characterCodeTable > 255) {
            throw new IllegalArgumentException("characterCodeTable must be between 0 and 255");
        }

        write(ESC);
        write('t');
        write(characterCodeTable);
        return this;
    }

    /**
     * Write String to outputStream.
     * <p>
     * Configure Style by with ESC/POS commands, encode String by charsetName
     * and write to outputStream.
     *
     * @param style text style to be used.
     * @param text content to be encoded and write to outputStream.
     * @return this object.
     * @exception UnsupportedEncodingException If the named charset is not
     * supported
     * @exception IOException if an I/O error occurs. In particular, an
     * <code>IOException</code> is thrown if the output stream is closed.
     * @see #setCharsetName(java.lang.String)
     */
    public EscPos write(Style style, String text) throws UnsupportedEncodingException, IOException {
        byte[] configBytes = style.getConfigBytes();
        write(configBytes, 0, configBytes.length);
        this.outputStream.write(text.getBytes(charsetName));
        return this;
    }

    /**
     * Write String to outputStream.
     * <p>
     * Configure printModeStyle by with ESC/POS commands, encode String by charsetName
     * and write to outputStream.
     *
     * @param printModeStyle - text style to be used.
     * @param text - content to be encoded and write to outputStream.
     * @return this object.
     * @exception UnsupportedEncodingException If the named charset is not
     * supported
     * @exception IOException if an I/O error occurs. In particular, an
     * <code>IOException</code> is thrown if the output stream is closed.
     * @see #setCharsetName(java.lang.String)
     * @see PrintModeStyle
     */
    public EscPos write(PrintModeStyle printModeStyle, String text) throws UnsupportedEncodingException, IOException {
        byte[] configBytes = printModeStyle.getConfigBytes();
        write(configBytes, 0, configBytes.length);
        this.outputStream.write(text.getBytes(charsetName));
        return this;
    }

    /**
     * Calls write with default style.
     *
     * @param text content to be send.
     * @return this object.
     * @exception UnsupportedEncodingException If the named charset is not
     * supported
     * @exception IOException if an I/O error occurs. In particular, an
     * <code>IOException</code> is thrown if the output stream is closed.
     * @see #write(Style, String)
     */
    public EscPos write(String text) throws UnsupportedEncodingException, IOException {
        return write(style, text);
    }

    /**
     * Calls write and feed on end.
     *
     * @param style value to be send.
     * @param text content to be send.
     * @return this object
     * @exception UnsupportedEncodingException If the named charset is not
     * supported
     * @exception IOException if an I/O error occurs. In particular, an
     * <code>IOException</code> is thrown if the output stream is closed.
     * @see #write(Style, String)
     * @see #feed(int)
     */
    public EscPos writeLF(Style style, String text) throws UnsupportedEncodingException, IOException {
        write(style, text);
        write(LF);
        return this;
    }

    /**
     * Calls write and feed on end.
     *
     * @param printModeStyle value to be send.
     * @param text content to be send.
     * @return this object
     * @exception UnsupportedEncodingException If the named charset is not
     * supported
     * @exception IOException if an I/O error occurs. In particular, an
     * <code>IOException</code> is thrown if the output stream is closed.
     * @see #write(PrintModeStyle, String)
     */
    public EscPos writeLF(PrintModeStyle printModeStyle, String text) throws UnsupportedEncodingException, IOException {
        write(printModeStyle, text);
        write(LF);
        return this;
    }

    /**
     * Calls write and feed on end. The style to be used is de default. You can
     * configure this default style using {@link #getStyle() getStyle} and/or
     * {@link #setStyle(Style)}   setStyle}
     *
     * @param text content to be send.
     * @return this object.
     * @exception UnsupportedEncodingException If the named charset is not
     * supported
     * @exception IOException if an I/O error occurs. In particular, an
     * <code>IOException</code> is thrown if the output stream is closed.
     * @see #writeLF(Style, String)
     * @see #setStyle(Style)
     * @see #getStyle()
     */
    public EscPos writeLF(String text) throws UnsupportedEncodingException, IOException {
        return writeLF(style, text);
    }

    /**
     * Send bar-code content to the printer.
     *
     * @param barcode objects that implements BarCodeWrapperInterface.
     * @param data content of bar-code.
     * @return this object
     * @exception IOException if an I/O error occurs.
     * @see BarCodeWrapperInterface
     */
    public EscPos write(BarCodeWrapperInterface barcode, String data) throws IOException {
        byte[] bytes = barcode.getBytes(data);
        write(bytes, 0, bytes.length);
        return this;

    }

    /**
     * Send image to the printer.
     *
     * @param wrapper objects that implements ImageWrapperInterface.
     * @param image content to be print.
     * @return this object
     * @exception IOException if an I/O error occurs.
     * @see ImageWrapperInterface
     */
    public EscPos write(ImageWrapperInterface wrapper, EscPosImage image) throws IOException {
        byte[] bytes = wrapper.getBytes(image);
        write(bytes, 0, bytes.length);
        return this;

    }

    /**
     * Executes paper cutting. GS V
     *
     * @param mode FULL cut or PARTIAL cut
     * @return this object
     * @exception IOException if an I/O error occurs.
     * @see CutMode
     */
    public EscPos cut(CutMode mode) throws IOException {
        write(GS);
        write('V');
        write(mode.value);
        return this;
    }

    /**
     * Prints the data in the print buffer and feeds n lines.
     * <p>
     * ASCII ESC d
     *
     * @param style to be used on line spacing
     * @param nLines number of lines
     * @return this object.
     * @exception IOException if an I/O error occurs.
     * @exception IllegalArgumentException if nLines out of range 0 to 255
     */
    public EscPos feed(Style style, int nLines) throws IOException, IllegalArgumentException{
        if (nLines < 1 || nLines > 255) {
            throw new IllegalArgumentException("nLines must be between 1 and 255");
        }
        byte[] configBytes = style.getConfigBytes();
        write(configBytes, 0, configBytes.length);
        for(int n = 0; n < nLines; n++){
            write(LF);
        }
        return this;
    }
    /**
     * Call feed with default style.
     *
     * @param nLines number of lines with the line spacing of
     * <code>defaultStyle</code>
     * @return this object.
     * @exception IOException if an I/O error occurs.
     * @exception IllegalArgumentException if nLines out of range 0 to 255
     * @see #feed(Style, int)
     */
    public EscPos feed(int nLines) throws IOException, IllegalArgumentException {
        return feed(style, nLines);
    }

    /**
     * Initialize printer. Clears the data in the print buffer and resets the
     * printer.<p>
     * reset style of this object. ASCII ESC @
     * <p>
     * @return this object
     * @see #setStyle(Style)
     *
     * @exception IOException if an I/O error occurs.
     */
    public EscPos initializePrinter() throws IOException {
        write(ESC);
        write('@');
        style.reset();
        return this;
    }

    /**
     * Generate pulse.
     * <p>
     * ASCII ESC p m t1 t2<p>
     * The pulse for ON time is(t1× 2 msec) and for OFF time is(t2× 2 msec)
     * .<p>
     *
     * @param pinConnector specifies pin 2 or pin 5
     * @param t1 time one
     * @param t2 time two
     * @return this object.
     * @exception IOException if an I/O error occurs.
     * @exception IllegalArgumentException if t1 or t2 out of range 0 to 255
     */
    public EscPos pulsePin(
            PinConnector pinConnector,
             int t1,
             int t2) throws IOException, IllegalArgumentException {
        if (t1 < 0 || t1 > 255) {
            throw new IllegalArgumentException("t1 must be between 1 and 255");
        }
        if (t2 < 0 || t2 > 255) {
            throw new IllegalArgumentException("t2 must be between 1 and 255");
        }

        write(ESC);
        write('p');
        write(pinConnector.value);
        write(t1);
        write(t2);
        return this;

    }

    public EscPos info() throws UnsupportedEncodingException, IOException {
        final Properties properties = new Properties();
        properties.load(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("projectinfo.properties")));
        String Version = properties.getProperty("version");
        Style title = new Style()
                .setFontSize(Style.FontSize._3, Style.FontSize._3)
                .setColorMode(Style.ColorMode.WhiteOnBlack)
                .setJustification(Justification.Center);
        write(title, "EscPos Coffee");
        feed(5);
        writeLF("java driver for ESC/POS commands.");


        writeLF("Version: " + Version);

        feed(3);
        getStyle().setJustification(Justification.Right);
        writeLF("github.com");
        writeLF("anastaciocintra/escpos-coffee");
        feed(5);
        cut(CutMode.FULL);
        return this;
    }

}
