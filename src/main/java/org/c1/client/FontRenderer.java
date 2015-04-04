package org.c1.client;

import java.util.*;

import com.google.common.collect.*;

import org.c1.client.render.*;
import org.c1.maths.*;

public class FontRenderer {

    private TextureAtlas atlas;
    private String supportedChars;
    private TextInfos textInfos;
    private float scale;
    private HashMap<TextInfos, VertexArray> cache;
    private VertexArray verticesArray;

    private final static class TextInfos {
        protected String text;
        protected int color;
        protected float posX;
        protected float posY;
        protected float scale;

        protected TextInfos() {

        }

        @Override
        public int hashCode() {
            final int BASE = 17;
            final int MULTIPLIER = 31;

            int result = BASE;
            result = (int) (MULTIPLIER * result + posX);
            result = (int) (MULTIPLIER * result + posY);
            result = MULTIPLIER * result + color;
            result = MULTIPLIER * result + Float.floatToRawIntBits(scale);
            result = MULTIPLIER * result + text.hashCode();

            return result;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof TextInfos) {
                TextInfos infos = (TextInfos) o;
                return infos.text.equals(text) && infos.color == color && infos.posX == posX && infos.posY == posY && infos.scale == scale;
            }
            return false;
        }

    }

    public FontRenderer(TextureAtlas atlas) {
        this(atlas, null);
    }

    public FontRenderer(TextureAtlas atlas, String supportedChars) {
        scale = 1f;
        this.atlas = atlas;
        this.supportedChars = supportedChars;

        cache = Maps.newHashMap();
        textInfos = new TextInfos();

        verticesArray = new VertexArray();
    }

    public void renderString(String text, float x, float y, int color) {
        if (text.replace(" ", "").trim().isEmpty())
            return;
        textInfos.text = text;
        textInfos.color = color;
        textInfos.posX = x;
        textInfos.posY = y;
        textInfos.scale = scale;
        if (cache.containsKey(textInfos)) {
            VertexArray array = cache.get(textInfos);
            atlas.getTexture().bind(0);
            array.bind();
            array.render();
            return;
        }
        verticesArray.clear();

        int currentIndex = 0;

        float xo = 0;
        int alpha = color >> 24 & 0xFF;
        int red = color >> 16 & 0xFF;
        int green = color >> 8 & 0xFF;
        int blue = color & 0xFF;
        float r = (float) red / 255f;
        float g = (float) green / 255f;
        float b = (float) blue / 255f;
        float a = (float) alpha / 255f;
        Quaternion colorQuaternion = new Quaternion(r, g, b, a);
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == ' ') {
                xo += getCharWidth('a') * scale + getCharSpacing() * scale;
                continue;
            }
            int index = indexOf(c);
            if (index < 0)
                continue;
            int xIndex = index % atlas.getFrequencyX();
            int yIndex = index / atlas.getFrequencyX();
            TextureRegion region = atlas.getRegion(xIndex, yIndex);

            float z = (float) i / (float) text.length();
            verticesArray.addVertex(new Vec3f(xo + x, y, z), new Vec2f(region.minU(), region.minV()), new Vec3f(), colorQuaternion);
            verticesArray.addVertex(new Vec3f(xo + x + getCharWidth(c), y, z), new Vec2f(region.maxU(), region.minV()), new Vec3f(),
                    colorQuaternion);
            verticesArray.addVertex(new Vec3f(xo + x + getCharWidth(c), y + getCharHeight(c), z), new Vec2f(region.maxU(), region.maxV()),
                    new Vec3f(), colorQuaternion);
            verticesArray.addVertex(new Vec3f(xo + x, y + getCharHeight(c), z), new Vec2f(region.minU(), region.maxV()), new Vec3f(),
                    colorQuaternion);

            verticesArray.addIndex(currentIndex + 1);
            verticesArray.addIndex(currentIndex + 0);
            verticesArray.addIndex(currentIndex + 2);

            verticesArray.addIndex(currentIndex + 2);
            verticesArray.addIndex(currentIndex + 0);
            verticesArray.addIndex(currentIndex + 3);
            xo += getCharWidth(c) + getCharSpacing();
            currentIndex += 4;
        }
        verticesArray.upload();

        atlas.getTexture().bind(0);
        verticesArray.bind();
        verticesArray.render();

        TextInfos textInfos1 = new TextInfos();
        textInfos1.text = text;
        textInfos1.color = color;
        textInfos1.posX = x;
        textInfos1.posY = y;
        textInfos1.scale = scale;
        cache.put(textInfos1, verticesArray);
        verticesArray = new VertexArray();
    }

    private float getCharSpacing() {
        return -6f * scale;
    }

    public float getCharHeight(char c) {
        return 16f * scale;
    }

    public float getCharWidth(char c) {
        return 16f * scale;
    }

    public float getStringWidth(String text) {
        float width = 0f;
        for (char c : text.toCharArray()) {
            width += getCharWidth(c) * scale + getCharSpacing() * scale;
        }
        return width;
    }

    private int indexOf(char c) {
        if (supportedChars != null) {
            return supportedChars.indexOf(c);
        }
        return c;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }
}
