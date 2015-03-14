package org.c1.client.render;

import java.io.*;

public class TextureAtlas {

    private Texture texture;
    private int tileWidth;
    private int tileHeight;
    private TextureRegion[][] atlasRegions;
    private int xFrequency;
    private int yFrequency;

    public TextureAtlas(String sourceClasspath, int tileWidth, int tileHeight) throws IOException {
        texture = new Texture(sourceClasspath);
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        init();
    }

    public TextureAtlas(Texture source, int tileWidth, int tileHeight) {
        this.texture = source;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        init();
    }

    private void init() {
        xFrequency = texture.getWidth() / tileWidth;
        yFrequency = texture.getHeight() / tileHeight;
        atlasRegions = new TextureRegion[xFrequency][yFrequency];
        for (int x = 0; x < xFrequency; x++) {
            for (int y = 0; y < yFrequency; y++) {
                float minU = (float) (x * tileWidth) / (float) (texture.getWidth());
                float maxU = (float) ((x + 1) * tileWidth) / (float) (texture.getWidth());
                float minV = (float) 1f - ((y + 1) * tileHeight) / (float) (texture.getHeight());
                float maxV = (float) 1f - (y * tileHeight) / (float) (texture.getHeight());
                StaticRegion region = new StaticRegion(minU, minV, maxU, maxV);
                atlasRegions[x][y] = region;
            }
        }
    }

    public TextureRegion[][] getRegions() {
        return atlasRegions;
    }

    public TextureRegion getRegion(int column, int row) {
        return atlasRegions[column][row];
    }

    public Texture getTexture() {
        return texture;
    }

    public int getTileWidth() {
        return tileWidth;
    }

    public int getTileHeight() {
        return tileHeight;
    }

    public int getFrequencyX() {
        return xFrequency;
    }

    public int getFrequencyY() {
        return yFrequency;
    }
}
