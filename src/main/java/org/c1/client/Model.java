package org.c1.client;

import static org.lwjgl.opengl.GL11.*;

import java.util.*;

import com.google.common.collect.*;

import org.c1.client.render.*;
import org.c1.maths.*;

public class Model {

    private List<ModelBox> boxes;
    private VertexArray vertexArray;
    private List<ModelFace> faces;
    private Texture texture;
    private boolean compiled;

    public Model() {
        vertexArray = new VertexArray();
        faces = Lists.newArrayList();
        boxes = Lists.newArrayList();
    }

    public void addFace(ModelFace face) {
        addFace(face, false);
    }

    public void addFace(ModelFace face, boolean copyInverted) {
        faces.add(face);
        if (copyInverted) {
            faces.add(face.inverted());
        }
        compiled = false;
    }

    public void compile() {
        compiled = true;
        int index = 0;
        boxes.forEach(box -> faces.addAll(box.getFaces()));
        for (ModelFace f : faces) {
            int maxIndex = 0;
            for (int i : f.getIndices()) {
                if (maxIndex <= i) {
                    maxIndex = i;
                }
                vertexArray.addIndex(index + i);
            }

            for (int i = 0; i < f.getPositions().size(); i++) {
                Vec3f pos = f.getPositions().get(i);
                Vec2f texCoord = f.getTexCoords().get(i);
                Vec3f normal = f.getNormals().get(i);
                vertexArray.addVertex(pos, texCoord, normal);
            }

            index += f.getPositions().size();
        }
        vertexArray.upload();
    }

    public ModelBox addBox(Vec3f pos, Vec3f size) {
        ModelBox box = new ModelBox(pos, size);
        boxes.add(box);
        return box;
    }

    public void render() {
        if (!compiled)
            compile();
        if (texture == null) {
            glBindTexture(GL_TEXTURE_2D, 0);
        } else {
            texture.bind(0);
        }
        vertexArray.bind();
        vertexArray.render();
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public Texture getTexture() {
        return texture;
    }
}
