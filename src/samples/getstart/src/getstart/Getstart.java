package getstart;

import escpos.EscPos;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Getstart {
    public void printinfo(){
        try {
            EscPos escpos = new EscPos(System.out);
            escpos.info();
            System.out.close();
        } catch (IOException ex) {
            Logger.getLogger(Getstart.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    public static void main(String[] args) {
        Getstart obj = new Getstart();
        obj.printinfo();
    }
    
}
