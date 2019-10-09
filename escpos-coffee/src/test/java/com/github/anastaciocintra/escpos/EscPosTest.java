package com.github.anastaciocintra.escpos;

import org.junit.jupiter.api.Test;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import static com.github.anastaciocintra.escpos.EscPosConst.ESC;
import static org.junit.jupiter.api.Assertions.*;

class EscPosTest {


    @Test
    void setPrinterCharacterTableTest() throws Exception{
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        EscPos escpos = new EscPos(result);

        escpos.setPrinterCharacterTable(10);
        escpos.close();
        ByteArrayOutputStream expected = new ByteArrayOutputStream();
        expected.write(ESC);
        expected.write('t');
        expected.write(10);

        assertArrayEquals(expected.toByteArray(), result.toByteArray());

    }
    @Test
    void pulsePinTest() throws Exception{
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        EscPos escpos = new EscPos(result);

        escpos.pulsePin(EscPos.PinConnector.Pin_2, 50, 75);
        escpos.close();

        ByteArrayOutputStream expected = new ByteArrayOutputStream();
        expected.write(ESC);
        expected.write('p');
        expected.write(EscPos.PinConnector.Pin_2.value);
        expected.write(50);
        expected.write(75);

        assertArrayEquals(expected.toByteArray(), result.toByteArray());

    }


}
