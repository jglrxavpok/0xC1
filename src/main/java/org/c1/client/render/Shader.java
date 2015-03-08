package org.c1.client.render;

import static org.lwjgl.opengl.GL20.*;

import java.io.*;
import java.util.*;

import com.google.common.collect.*;

import org.c1.maths.*;
import org.c1.utils.*;

public class Shader {

    public class Uniform {

        private String type;
        private String name;
        private Shader shader;

        public Uniform(String type, String name) {
            this.type = type;
            this.name = name;
            this.shader = Shader.this;
        }

        public String getType() {
            return type;
        }

        public String getName() {
            return name;
        }

        public void setValuef(float value) {
            glUniform1f(shader.getUniformLocation(name), value);
        }

        public void setValuei(int value) {
            glUniform1i(shader.getUniformLocation(name), value);
        }

        public void setValueMat4(Mat4f m) {
            glUniformMatrix4(shader.getUniformLocation(name), false, BufferHelper.floatBuffer(Lists.newArrayList(m)));
        }
    }

    public final static int POS_INDEX = 1;
    public final static int TEXT_INDEX = 2;
    public final static int NORMAL_INDEX = 3;
    private ArrayList<Uniform> uniforms;
    private Map<String, Integer> uniformLocs;
    private int program;

    public Shader(String classpathStart) throws IOException {
        String vertexLoc = classpathStart + ".vsh";
        String fragLoc = classpathStart + ".fsh";
        init(vertexLoc, fragLoc);
    }

    public Shader(String vertexLoc, String fragLoc) throws IOException {
        init(vertexLoc, fragLoc);
    }

    private void init(String vertexLoc, String fragLoc) throws IOException {
        uniforms = Lists.newArrayList();
        uniformLocs = Maps.newHashMap();
        String vertexContent = IOUtils.read(vertexLoc, "UTF-8");
        String fragContent = IOUtils.read(fragLoc, "UTF-8");
        extractUniforms(vertexContent);
        extractUniforms(fragContent);

        int vertID = compile(vertexContent, "vertex");
        int fragID = compile(fragContent, "fragment");

        program = glCreateProgram();
        glAttachShader(program, vertID);
        glAttachShader(program, fragID);
        glLinkProgram(program);
        if (glGetProgrami(program, GL_LINK_STATUS) == 0) {
            String error = glGetProgramInfoLog(program, 1024);
            throw new RuntimeException("Error while linking shader: " + error);
        }
        glValidateProgram(program);
        if (glGetProgrami(program, GL_VALIDATE_STATUS) == 0) {
            String error = glGetProgramInfoLog(program, 1024);
            throw new RuntimeException("Error while validating shader: " + error);
        }
        glDeleteShader(vertID);
        glDeleteShader(fragID);
    }

    private int compile(String content, String type) {
        int shaderType = GL_VERTEX_SHADER;
        if (type.equals("fragment")) {
            shaderType = GL_FRAGMENT_SHADER;
        }
        int shaderID = glCreateShader(shaderType);
        glShaderSource(shaderID, content);
        glCompileShader(shaderID);

        if (glGetShaderi(shaderID, GL_COMPILE_STATUS) == 0) {
            String error = glGetShaderInfoLog(shaderID, 1024);
            throw new RuntimeException("Error while compiling " + type + " shader: " + error);
        }
        return shaderID;
    }

    private void extractUniforms(String content) {
        String[] lines = content.split("\n");
        for (String line : lines) {
            if (line.startsWith("uniform ")) {
                String declaration = line.replace("uniform ", "");
                int end = declaration.indexOf(";");
                declaration = declaration.substring(0, end);
                String[] parts = declaration.split(" ");
                String type = parts[0];
                String name = parts[parts.length - 1];
                Uniform uniform = new Uniform(type, name);
                uniforms.add(uniform);
            }
        }
    }

    public Uniform getUniform(String name) {
        for (Uniform u : uniforms) {
            if (u.getName().equals(name))
                return u;
        }
        return null;
    }

    public int getUniformLocation(String name) {
        if (uniformLocs.containsKey(name))
            return uniformLocs.get(name);
        int loc = glGetUniformLocation(program, name);
        uniformLocs.put(name, loc);
        return loc;
    }

    public void bind() {
        glUseProgram(program);
    }
}
