/*
 * Use of this source code is governed by the MIT license that can be
 * found in the LICENSE file.
 */
package com.github.anastaciocintra.escpos;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Supply ESC/POS text style commands
 * Note: If your printer isn't compatible with this class, you can try to use PrintModeStyle class
 * @see PrintModeStyle
 */
public class Style implements EscPosConst {

    /**
     * Values of font name.
     *
     * @see #setFontName(FontName)
     */
    public enum FontName {
        Font_A_Default(48),
        Font_B(49),
        Font_C(50);
        public int value;

        private FontName(int value) {
            this.value = value;
        }
    }

    /**
     * Values of font size.
     *
     * @see #setFontSize(FontSize, FontSize)
     */
    public enum FontSize {
        _1(0),
        _2(1),
        _3(2),
        _4(3),
        _5(4),
        _6(5),
        _7(6),
        _8(7);
        public int value;

        private FontSize(int value) {
            this.value = value;
        }
    }

    /**
     * values of underline style.
     *
     * @see #setUnderline(Underline)
     */
    public enum Underline {
        None_Default(48),
        OneDotThick(49),
        TwoDotThick(50);
        public int value;

        private Underline(int value) {
            this.value = value;
        }
    }

    /**
     * values of color mode background / foreground reverse.
     *
     * @see #setColorMode(ColorMode)
     */
    public enum ColorMode {
        BlackOnWhite_Default(0),
        WhiteOnBlack(1);
        public int value;

        private ColorMode(int value) {
            this.value = value;
        }
    }

    protected FontName fontName;
    protected boolean bold;
    protected Underline underline;
    protected FontSize fontWidth;
    protected FontSize fontHeight;
    protected Justification justification;
    protected boolean defaultLineSpacing;
    protected int lineSpacing;
    protected ColorMode colorMode;

    /**
     * creates Style object with default values.
     */
    public Style() {
        reset();
    }

    /**
     * creates Style object with another Style instance values.
     *
     * @param another value to be copied.
     */
    public Style(Style another) {
        setFontName(another.fontName);
        setBold(another.bold);
        setFontSize(another.fontWidth, another.fontHeight);
        setUnderline(another.underline);
        setJustification(another.justification);
        defaultLineSpacing = another.defaultLineSpacing;
        setLineSpacing(another.lineSpacing);
        setColorMode(another.colorMode);
    }

    /**
     * Reset values to default.
     */
    public final void reset() {
        fontName = FontName.Font_A_Default;
        fontWidth = FontSize._1;
        fontHeight = FontSize._1;
        bold = false;
        underline = Underline.None_Default;
        justification = Justification.Left_Default;
        resetLineSpacing();
        colorMode = ColorMode.BlackOnWhite_Default;
    }

    /**
     * Set character font name.
     *
     * @param fontName used on ESC M n
     * @return this object
     * @see #getConfigBytes()
     */
    public final Style setFontName(FontName fontName) {
        this.fontName = fontName;
        return this;
    }

    /**
     * Set emphasized mode on/off
     *
     * @param bold used on ESC E n
     * @return this object
     */
    public final Style setBold(boolean bold) {
        this.bold = bold;
        return this;
    }

    /**
     * set font size
     *
     * @param fontWidth value used on GS ! n
     * @param fontHeight value used on GS ! n
     * @return this object
     * @see #getConfigBytes()
     */
    public final Style setFontSize(FontSize fontWidth, FontSize fontHeight) {
        this.fontWidth = fontWidth;
        this.fontHeight = fontHeight;
        return this;
    }

    /**
     * Set underline mode.
     *
     * @param underline value used on ESC – n
     * @return this object
     * @see #getConfigBytes()
     */
    public final Style setUnderline(Underline underline) {
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
    public final Style setJustification(Justification justification) {
        this.justification = justification;
        return this;
    }

    /**
     * Set line spacing.
     *
     * @param lineSpacing value used on ESC 3 n
     * @return this object
     * @throws IllegalArgumentException when lineSpacing is not between 0 and 255.
     * @see #getConfigBytes()
     */
    public final Style setLineSpacing(int lineSpacing) throws IllegalArgumentException {
        if (lineSpacing < 0 || lineSpacing > 255) {
            throw new IllegalArgumentException("lineSpacing must be between 0 and 255");
        }
        this.defaultLineSpacing = false;
        this.lineSpacing = lineSpacing;
        return this;
    }

    /**
     * Reset line spacing to printer default used on ESC 2
     *
     * @return this object
     * @see #getConfigBytes()
     */
    public final Style resetLineSpacing() {
        this.defaultLineSpacing = true;
        lineSpacing = 0;
        return this;
    }

    /**
     * set color mode background / foreground reverse.
     *
     * @param colorMode value used on GS B n
     * @return this object
     */
    public final Style setColorMode(ColorMode colorMode) {
        this.colorMode = colorMode;
        return this;
    }

    /**
     * Configure font Style.
     * <p>
     * Select character font.
     * <p>
     * ASCII ESC M n
     * <p>
     *
     * Turn emphasized(bold) mode on/off.
     * <p>
     * ASCII ESC E n
     * <p>
     *
     * set font size.
     * <p>
     * ASCII GS ! n
     * <p>
     *
     * select underline mode
     * <p>
     * ASCII ESC – n
     * <p>
     *
     * Select justification
     * <p>
     * ASCII ESC a n
     * <p>
     *
     * Select default line spacing
     * <p>
     * ASCII ESC 2
     * <p>
     *
     * Set line spacing
     * <p>
     * ASCII ESC 3 n
     * <p>
     *
     * Turn white/black reverse print mode on/off<p>
     * ASCII GS B n
     *
     * @return ESC/POS commands to configure style
     * @exception IOException if an I/O error occurs.
     */
    public byte[] getConfigBytes() throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        //
        bytes.write(ESC);
        bytes.write('M');
        bytes.write(fontName.value);
        //
        bytes.write(ESC);
        bytes.write('E');
        int n = bold ? 1 : 0;
        bytes.write(n);
        //
        n = fontWidth.value << 4 | fontHeight.value;
        bytes.write(GS);
        bytes.write('!');
        bytes.write(n);
        //
        bytes.write(ESC);
        bytes.write('-');
        bytes.write(underline.value);
        //
        bytes.write(ESC);
        bytes.write('a');
        bytes.write(justification.value);
        //
        if (defaultLineSpacing) {
            bytes.write(ESC);
            bytes.write('2');

        } else {
            bytes.write(ESC);
            bytes.write('3');
            bytes.write(lineSpacing);
        }
        //
        bytes.write(GS);
        bytes.write('B');
        bytes.write(colorMode.value);

        return bytes.toByteArray();
    }

}
