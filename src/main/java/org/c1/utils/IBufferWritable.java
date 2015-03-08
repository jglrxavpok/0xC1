package org.c1.utils;

import java.nio.*;

public interface IBufferWritable {

    void write(FloatBuffer buffer);

    int getSize();
}
