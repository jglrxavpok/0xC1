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

    public VertexArray() {
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

    public void upload() {
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
}
