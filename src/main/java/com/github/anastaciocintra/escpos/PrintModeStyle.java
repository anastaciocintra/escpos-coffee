/*
 * Use of this source code is governed by the MIT license that can be
 * found in the LICENSE file.
 */
package com.github.anastaciocintra.escpos;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Supply ESC/POS text style with the set of Print Mode commands
 * NOTE: You can use this class when your printer isn't compatible with Style class, otherwise, always
 * prefer to use Style class, this is because PrintModeStyle have less features than Style class.
 * Go to samples/textprintmodestyle to see how to use it.
 * @see Style
 *
 */
public class PrintModeStyle implements EscPosConst{
    /**
     * Values of font name.
     *
     * @see #setFontName(FontName)
     */
    public enum FontName {
        Font_A_Default(0),
        Font_B(1);
        public int value;

        private FontName(int value) {
            this.value = value;
        }
    }

    protected FontName fontName;
    protected boolean bold;
    protected boolean underline;
    protected boolean doubleWidth;
    protected boolean doubleHeight;
    protected Justification justification;


    /**
     * creates PrintModeStyle object with default values.
     */
    public PrintModeStyle() {

        reset();
    }

    /**
     * creates PrintModeStyle object with another PrintModeStyle instance values.
     *
     * @param another value to be copied.
     */
    public PrintModeStyle(PrintModeStyle another) {
        setFontName(another.fontName);
        setBold(another.bold);
        setFontSize(another.doubleWidth, another.doubleHeight);
        setUnderline(another.underline);
        setJustification(another.justification);
    }

    /**
     * Reset values to default.
     */
    public final void reset() {
        fontName = FontName.Font_A_Default;
        setBold(false);
        setFontSize(false,false);
        justification = Justification.Left_Default;
    }


    /**
     * Set character font name.
     *
     * @param fontName used on ESC ! n
     * @return this object
     * @see #getConfigBytes()
     */
    public final PrintModeStyle setFontName(FontName fontName) {
        this.fontName = fontName;
        return this;
    }

    /**
     * Set emphasized mode on/off
     *
     * @param bold used on ESC ! n
     * @return this object
     */
    public final PrintModeStyle setBold(boolean bold) {
        this.bold = bold;
        return this;
    }

    /**
     * set font size
     *
     * @param doubleWidth value used on ESC ! n
     * @param doubleHeight value used on ESC ! n
     * @return this object
     * @see #getConfigBytes()
     */
    public final PrintModeStyle setFontSize(boolean doubleWidth, boolean doubleHeight) {
        this.doubleWidth = doubleWidth;
        this.doubleHeight = doubleHeight;
        return this;
    }

    /**
     * Set underline mode.
     *
     * @param underline value used on ESC ! n
     * @return this object
     * @see #getConfigBytes()
     */
    public final PrintModeStyle setUnderline(boolean underline) {
        this.underline = underline;
        return this;
    }

    /**
     * Set Justification for text.
     *
     * @param justification value used on ESC a n
     * @return this object
     * @see #getConfigBytes()
     */
    public final PrintModeStyle setJustification(Justification justification) {
        this.justification = justification;
        return this;
    }

    /**
     * Configure font Style.
     * <p>
     * Selects the character font and styles (emphasize, double-height, double-width, and underline) together..
     * <p>
     * ASCII ESC ! n
     * <p>
     *
     * <p>
     * Select justification
     * <p>
     *
     * @return ESC/POS commands to configure style
     * @exception IOException if an I/O error occurs.
     */
    public byte[] getConfigBytes() throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        // bit combination ...
        int nVal = fontName.value;
        //
        //
        if(bold) nVal = nVal | 0x8;
        //
        //
        if(doubleHeight) nVal = nVal | 0x10;
        if(doubleWidth) nVal = nVal | 0x20;
        //
        //
        if(underline) nVal = nVal | 0x80;
        //
        //
        bytes.write(EscPosConst.ESC);
        bytes.write('!');
        bytes.write(nVal);

        //
        bytes.write(ESC);
        bytes.write('a');
        bytes.write(justification.value);


        return bytes.toByteArray();
    }


}
