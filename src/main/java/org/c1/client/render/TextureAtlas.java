package org.c1.client.render;

import java.io.*;

import org.c1.maths.*;

public class TextureAtlas {

    private Texture texture;
    private int tileWidth;
    private int tileHeight;
    private TextureRegion[][] atlasRegions;
    private int xFrequency;
    private int yFrequency;
    private Sprite[][] atlasSprites;

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
        atlasSprites = new Sprite[xFrequency][yFrequency];
        for (int x = 0; x < xFrequency; x++) {
            for (int y = 0; y < yFrequency; y++) {
                Vec2f minUV = getTexelCoords(x * tileWidth, texture.getHeight() - (y + 1) * tileHeight);
                Vec2f maxUV = getTexelCoords((x + 1) * tileWidth - 1, texture.getHeight() - y * tileHeight - 1);
                StaticRegion region = new StaticRegion(minUV.x(), minUV.y(), maxUV.x(), maxUV.y());
                atlasRegions[x][y] = region;
                atlasSprites[x][y] = new Sprite(texture, region);
                atlasSprites[x][y].setSize(tileWidth, tileHeight);
            }
        }
    }

    /**
     * Performs a half-pixel correction and then returns the UV coords based on the pixel coordinates
     */
    private Vec2f getTexelCoords(int x, int y) {
        float xpos = x + .5f;
        float ypos = y + .5f;
        float u = xpos / (float) texture.getWidth();
        float v = ypos / (float) texture.getHeight();
        return new Vec2f(u, v);
    }

    public TextureRegion[][] getRegions() {
        return atlasRegions;
    }

    public TextureRegion getRegion(int column, int row) {
        return atlasRegions[column][row];
    }

    public Sprite[][] getSprite() {
        return atlasSprites;
    }

    public Sprite getSprite(int column, int row) {
        return atlasSprites[column][row];
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
