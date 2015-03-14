package org.c1.client;

import java.util.*;

import com.google.common.collect.*;

import org.c1.maths.*;

public class ModelFace {

    private List<Vec3f> positions;
    private List<Vec2f> texCoords;
    private List<Vec3f> normals;
    private List<Integer> indices;

    public ModelFace() {
        positions = Lists.newArrayList();
        texCoords = Lists.newArrayList();
        normals = Lists.newArrayList();
        indices = Lists.newArrayList();
    }

    public void addVertex(Vec3f pos, Vec2f texCoord, Vec3f normal) {
        positions.add(pos);
        texCoords.add(texCoord);
        normals.add(normal);
    }

    public void addIndex(int index) {
        indices.add(index);
    }

    public List<Integer> getIndices() {
        return indices;
    }

    public List<Vec3f> getPositions() {
        return positions;
    }

    public List<Vec2f> getTexCoords() {
        return texCoords;
    }

    public List<Vec3f> getNormals() {
        return normals;
    }

    public ModelFace inverted() {
        ModelFace face = new ModelFace();
        int[] finalIndices = new int[indices.size()];
        for (int i = 0; i < finalIndices.length; i++) { // TODO: better inverting algorithm to fix lights problem
            finalIndices[i] = indices.get(finalIndices.length - i - 1);
        }
        face.positions.addAll(positions);
        face.texCoords.addAll(texCoords);
        for (Vec3f n : normals) {
            face.normals.add(n.neg());
        }
        List<Integer> indicesList = Lists.newArrayList();
        for (int index : finalIndices)
            indicesList.add(index);
        face.indices.addAll(indicesList);
        return face;
    }
}
