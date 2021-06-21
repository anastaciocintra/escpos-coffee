/*
 * Use of this source code is governed by the MIT license that can be
 * found in the LICENSE file.
 */
package com.github.anastaciocintra.escpos.barcode;

import com.github.anastaciocintra.escpos.EscPosConst;

public interface BarCodeWrapperInterface<T> {
    public byte[] getBytes(String data);
    public T setJustification(EscPosConst.Justification justification);
}
