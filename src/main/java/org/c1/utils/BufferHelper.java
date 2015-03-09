package org.c1.utils;

import java.nio.*;
import java.util.*;

import org.lwjgl.*;

public class BufferHelper {

    public static FloatBuffer floatBuffer(List<? extends IBufferWritable> list) {
        int size = 0;
        for (IBufferWritable w : list)
            size += w.getSize();
        FloatBuffer buffer = BufferUtils.createFloatBuffer(size);
        for (IBufferWritable w : list) {
            w.write(buffer);
        }
        buffer.flip();
        return buffer;
    }

    public static IntBuffer intBuffer(List<Integer> indices) {
        IntBuffer buffer = BufferUtils.createIntBuffer(indices.size());
        for (int i : indices)
            buffer.put(i);
        buffer.flip();
        return buffer;
    }

    public static IntBuffer intBuffer(int[] indices) {
        IntBuffer buffer = BufferUtils.createIntBuffer(indices.length);
        for (int i : indices)
            buffer.put(i);
        buffer.flip();
        return buffer;
    }

}
