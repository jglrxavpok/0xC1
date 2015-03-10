package org.c1.client.render;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.util.*;

import com.google.common.collect.*;

import org.c1.maths.*;
import org.c1.utils.*;

public class VertexArray {

    private ArrayList<Vec3f> positions;
    private ArrayList<Integer> indices;
    private ArrayList<Vec2f> texCoords;
    private ArrayList<Vec3f> normals;
    private int vao;
    private int vbo;
    private int tbo;
    private int ibo;
    private int nbo;
    private int count;
    private ArrayList<Vec3f> tangents;
    private int tangentsbo;

    public VertexArray() {
        positions = Lists.newArrayList();
        texCoords = Lists.newArrayList();
        normals = Lists.newArrayList();
        tangents = Lists.newArrayList();

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

    public void upload() {
        computeTangents();
        count = indices.size();
        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, BufferHelper.floatBuffer(positions), GL_STATIC_DRAW);
        glVertexAttribPointer(Shader.POS_INDEX, 3, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(Shader.POS_INDEX);

        tbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, tbo);
        glBufferData(GL_ARRAY_BUFFER, BufferHelper.floatBuffer(texCoords), GL_STATIC_DRAW);
        glVertexAttribPointer(Shader.TEXT_INDEX, 2, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(Shader.TEXT_INDEX);

        nbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, nbo);
        glBufferData(GL_ARRAY_BUFFER, BufferHelper.floatBuffer(normals), GL_STATIC_DRAW);
        glVertexAttribPointer(Shader.NORMAL_INDEX, 3, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(Shader.NORMAL_INDEX);

        tangentsbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, tangentsbo);
        glBufferData(GL_ARRAY_BUFFER, BufferHelper.floatBuffer(tangents), GL_STATIC_DRAW);
        glVertexAttribPointer(Shader.TANGENT_INDEX, 3, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(Shader.TANGENT_INDEX);

        ibo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, BufferHelper.intBuffer(indices), GL_STATIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    public void bind() {
        glBindVertexArray(vao);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
    }

    public void unbind() {
        glBindVertexArray(0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    public void render() {
        glDrawElements(GL_TRIANGLES, count, GL_UNSIGNED_INT, 0);
    }

    private void computeTangents() {
        tangents.clear();
        for (int i = 0; i < positions.size(); i++)
            tangents.add(Vec3f.NULL.copy());

        for (int i = 0; i < indices.size(); i += 3) {
            int i0 = indices.get(i);
            int i1 = indices.get(i + 1);
            int i2 = indices.get(i + 2);

            Vec3f edge1 = positions.get(i1).copy().sub(positions.get(i0));
            Vec3f edge2 = positions.get(i2).copy().sub(positions.get(i0));

            double deltaU1 = texCoords.get(i1).x() - texCoords.get(i0).x();
            double deltaU2 = texCoords.get(i2).x() - texCoords.get(i0).x();
            double deltaV1 = texCoords.get(i1).y() - texCoords.get(i0).y();
            double deltaV2 = texCoords.get(i2).y() - texCoords.get(i0).y();

            double dividend = (deltaU1 * deltaV2 - deltaU2 * deltaV1);
            double f = dividend == 0.0f ? 0.0f : 1.0f / dividend;

            Vec3f tangent = new Vec3f((float) (f * (deltaV2 * edge1.x() - deltaV1 * edge2.x())), (float) (f * (deltaV2 * edge1.y() - deltaV1 * edge2.y())), (float) (f * (deltaV2 * edge1.z() - deltaV1 * edge2.z())));

            // Bitangent example, in Java
            // Vector3 bitangent = Vector3.get((float)(f * (-deltaU2 *
            // edge1.getX() - deltaU1 * edge2.getX())), (float)(f * (-deltaU2 *
            // edge1.getY() - deltaU1 * edge2.getY())), (float)(f * (-deltaU2 *
            // edge1.getZ() - deltaU1 * edge2.getZ())));

            tangents.set(i0, tangents.get(i0).copy().add(tangent));
            tangents.set(i1, tangents.get(i1).copy().add(tangent));
            tangents.set(i2, tangents.get(i2).copy().add(tangent));
        }

        for (int i = 0; i < tangents.size(); i++)
            tangents.set(i, tangents.get(i).copy().norm());
    }
}
