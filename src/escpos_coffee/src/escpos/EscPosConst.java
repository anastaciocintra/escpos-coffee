/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package escpos;

/**
 *
 * @author marco
 */
public interface EscPosConst {
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
