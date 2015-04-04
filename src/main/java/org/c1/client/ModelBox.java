package org.c1.client;

import com.google.common.collect.Lists;
import org.c1.client.render.StaticRegion;
import org.c1.client.render.TextureRegion;
import org.c1.maths.Vec2f;
import org.c1.maths.Vec3f;

import java.util.List;

/**
 * Created by jglrxavpok on 04/04/2015.
 */
public class ModelBox {

    private Vec3f size;
    private Vec3f pos;
    private List<ModelFace> faces;
    private TextureRegion topRegion;
    private TextureRegion southRegion;
    private TextureRegion northRegion;
    private TextureRegion bottomRegion;
    private TextureRegion eastRegion;
    private TextureRegion westRegion;

    public ModelBox(Vec3f pos, Vec3f size) {
        this.pos = pos;
        this.size = size;
        setTextureRegion(new StaticRegion(0,0,1,1));
        computeFaces();
    }

    public Vec3f getPos() {
        return pos;
    }

    public Vec3f getSize() {
        return size;
    }

    public void setTextureRegion(TextureRegion region) {
        this.topRegion = region;
        this.bottomRegion = region;
        this.eastRegion = region;
        this.westRegion = region;
        this.northRegion = region;
        this.southRegion = region;
    }

    public void computeFaces() {
        faces = Lists.newArrayList();
        faces.add(northFace());
        faces.add(southFace());
        faces.add(bottomFace());
        faces.add(topFace());
        faces.add(westFace());
        faces.add(eastFace());
    }

    private ModelFace southFace() {
        ModelFace face = new ModelFace();
        face.addVertex(new Vec3f(new Vec3f(0,0,size.z()).add(pos)), new Vec2f(southRegion.maxU(), southRegion.minV()), new Vec3f(0,0,1));
        face.addVertex(new Vec3f(new Vec3f(0,size.y(),size.z()).add(pos)), new Vec2f(southRegion.maxU(), southRegion.maxV()), new Vec3f(0,0,1));
        face.addVertex(new Vec3f(new Vec3f(size.x(),size.y(),size.z()).add(pos)), new Vec2f(southRegion.minU(), southRegion.maxV()), new Vec3f(0,0,1));
        face.addVertex(new Vec3f(new Vec3f(size.x(), 0, size.z()).add(pos)), new Vec2f(southRegion.minU(), southRegion.minV()), new Vec3f(0,0,1));

        face.addIndex(1);
        face.addIndex(0);
        face.addIndex(2);

        face.addIndex(2);
        face.addIndex(0);
        face.addIndex(3);

        return face;
    }

    private ModelFace northFace() {
        ModelFace face = new ModelFace();
        face.addVertex(new Vec3f(new Vec3f(0,0,0).add(pos)), new Vec2f(northRegion.minU(), northRegion.minV()), new Vec3f(0,0,-1));
        face.addVertex(new Vec3f(new Vec3f(0,size.y(),0).add(pos)), new Vec2f(northRegion.minU(), northRegion.maxV()), new Vec3f(0,0,-1));
        face.addVertex(new Vec3f(new Vec3f(size.x(),size.y(),0).add(pos)), new Vec2f(northRegion.maxU(), northRegion.maxV()), new Vec3f(0,0,-1));
        face.addVertex(new Vec3f(new Vec3f(size.x(),0,0).add(pos)), new Vec2f(northRegion.maxU(), northRegion.minV()), new Vec3f(0,0,-1));

        face.addIndex(2);
        face.addIndex(0);
        face.addIndex(1);

        face.addIndex(3);
        face.addIndex(0);
        face.addIndex(2);
        return face;
    }

    private ModelFace bottomFace() {
        ModelFace face = new ModelFace();
        face.addVertex(new Vec3f(new Vec3f(0,0,0).add(pos)), new Vec2f(bottomRegion.minU(), bottomRegion.minV()), new Vec3f(0,-1,0));
        face.addVertex(new Vec3f(new Vec3f(0,0,size.z()).add(pos)), new Vec2f(bottomRegion.minU(), bottomRegion.maxV()), new Vec3f(0,-1,0));
        face.addVertex(new Vec3f(new Vec3f(size.x(),0,size.z()).add(pos)), new Vec2f(bottomRegion.maxU(), bottomRegion.maxV()), new Vec3f(0,-1,0));
        face.addVertex(new Vec3f(new Vec3f(size.x(),0,0).add(pos)), new Vec2f(bottomRegion.maxU(), bottomRegion.minV()), new Vec3f(0,-1,0));

        face.addIndex(1);
        face.addIndex(0);
        face.addIndex(2);

        face.addIndex(2);
        face.addIndex(0);
        face.addIndex(3);
        return face;
    }

    private ModelFace topFace() {
        ModelFace face = new ModelFace();
        face.addVertex(new Vec3f(new Vec3f(0,size.y(),0).add(pos)), new Vec2f(topRegion.minU(), topRegion.minV()), new Vec3f(0,1,0));
        face.addVertex(new Vec3f(new Vec3f(0,size.y(),size.z()).add(pos)), new Vec2f(topRegion.minU(), topRegion.maxV()), new Vec3f(0,1,0));
        face.addVertex(new Vec3f(new Vec3f(size.x(),size.y(),size.z()).add(pos)), new Vec2f(topRegion.maxU(), topRegion.maxV()), new Vec3f(0,1,0));
        face.addVertex(new Vec3f(new Vec3f(size.x(),size.y(),0).add(pos)), new Vec2f(topRegion.maxU(), topRegion.minV()), new Vec3f(0,1,0));

        face.addIndex(2);
        face.addIndex(0);
        face.addIndex(1);

        face.addIndex(3);
        face.addIndex(0);
        face.addIndex(2);
        return face;
    }

    private ModelFace westFace() {
        ModelFace face = new ModelFace();
        face.addVertex(new Vec3f(new Vec3f(0,0,0).add(pos)), new Vec2f(westRegion.maxU(), westRegion.minV()), new Vec3f(-1,0,0));
        face.addVertex(new Vec3f(new Vec3f(0,0,size.z()).add(pos)), new Vec2f(westRegion.minU(), westRegion.minV()), new Vec3f(-1,0,0));
        face.addVertex(new Vec3f(new Vec3f(0,size.y(),size.z()).add(pos)), new Vec2f(westRegion.minU(), westRegion.maxV()), new Vec3f(-1,0,0));
        face.addVertex(new Vec3f(new Vec3f(0,size.y(),0).add(pos)), new Vec2f(westRegion.maxU(), westRegion.maxV()), new Vec3f(-1,0,0));

        face.addIndex(2);
        face.addIndex(0);
        face.addIndex(1);

        face.addIndex(3);
        face.addIndex(0);
        face.addIndex(2);
        return face;
    }

    private ModelFace eastFace() {
        ModelFace face = new ModelFace();
        face.addVertex(new Vec3f(new Vec3f(size.x(),0,0).add(pos)), new Vec2f(westRegion.minU(), westRegion.minV()), new Vec3f(1,0,0));
        face.addVertex(new Vec3f(new Vec3f(size.x(),0,size.z()).add(pos)), new Vec2f(westRegion.maxU(), westRegion.minV()), new Vec3f(1,0,0));
        face.addVertex(new Vec3f(new Vec3f(size.x(),size.y(),size.z()).add(pos)), new Vec2f(westRegion.maxU(), westRegion.maxV()), new Vec3f(1,0,0));
        face.addVertex(new Vec3f(new Vec3f(size.x(),size.y(),0).add(pos)), new Vec2f(westRegion.minU(), westRegion.maxV()), new Vec3f(1,0,0));

        face.addIndex(1);
        face.addIndex(0);
        face.addIndex(2);

        face.addIndex(2);
        face.addIndex(0);
        face.addIndex(3);
        return face;
    }

    public TextureRegion getTopRegion() {
        return topRegion;
    }

    public void setTopRegion(TextureRegion topRegion) {
        this.topRegion = topRegion;
    }

    public TextureRegion getSouthRegion() {
        return southRegion;
    }

    public void setSouthRegion(TextureRegion southRegion) {
        this.southRegion = southRegion;
    }

    public TextureRegion getNorthRegion() {
        return northRegion;
    }

    public void setNorthRegion(TextureRegion northRegion) {
        this.northRegion = northRegion;
    }

    public TextureRegion getBottomRegion() {
        return bottomRegion;
    }

    public void setBottomRegion(TextureRegion bottomRegion) {
        this.bottomRegion = bottomRegion;
    }

    public TextureRegion getEastRegion() {
        return eastRegion;
    }

    public void setEastRegion(TextureRegion eastRegion) {
        this.eastRegion = eastRegion;
    }

    public TextureRegion getWestRegion() {
        return westRegion;
    }

    public void setWestRegion(TextureRegion westRegion) {
        this.westRegion = westRegion;
    }

    public List<ModelFace> getFaces() {
        return faces;
    }
}
