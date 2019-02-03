/*
 * The MIT License
 *
 * Copyright 2019 Marco Antonio Anastacio Cintra.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package escpos;

public interface EscPosConst {
    
    /**
     *
     */
    public final String version = "1.0.0";
    public final int NUL = 0;
    public final int LF  = 10;
    public  final int ESC = 27;
    public  final int GS = 29;    

    
    /**
     * Values for print justification.
     * @see Style#setJustification(escpos.EscPosConst.Justification) 
     */
    public enum Justification{
        Left_Default(48),
        Center(49),
        Right(50);
        public int value;
        private Justification(int value){
            this.value = value;
        }
    }
    
    
    
}
