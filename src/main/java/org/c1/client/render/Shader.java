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

        public void setValue2f(Vec2f v) {
            glUniform2f(shader.getUniformLocation(name), v.x(), v.y());
        }

        public void setValue3f(Vec3f v) {
            glUniform3f(shader.getUniformLocation(name), v.x(), v.y(), v.z());
        }

        public void setValue3f(Quaternion q) {
            glUniform4f(shader.getUniformLocation(name), q.x(), q.y(), q.z(), q.w());
        }
    }

    public final static int POS_INDEX = 1;
    public final static int TEXT_INDEX = 2;
    public final static int NORMAL_INDEX = 3;
    public final static int TANGENT_INDEX = 4;
    public final static int COLOR_INDEX = 5;

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

        vertexContent = preprocess(vertexContent);
        fragContent = preprocess(fragContent);

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

    private String preprocess(String content) throws IOException {
        String[] lines = content.split("\n");
        StringBuffer buffer = new StringBuffer();
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
                System.out.println("New uniform: " + type + " " + name);

                buffer.append(line + "\n");
            } else if (line.startsWith("#include ")) {
                String path = line.replace("#include ", "").replace(" ", "").replace("\n", "").replace("\r", "");
                String includedContent = preprocess(IOUtils.read("shaders/" + path, "UTF-8"));
                buffer.append(includedContent);
            } else {
                buffer.append(line + "\n");
            }

        }

        return buffer.toString();
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

    public Uniform getUniform(String name) {
        for (Uniform u : uniforms) {
            if (u.getName().equals(name))
                return u;
        }
        if (getUniformLocation(name) != -1) {
            Uniform uniform = new Uniform("<unknown>", name);
            uniforms.add(uniform);
            return uniform;
        }
        return null;
    }

    public int getUniformLocation(String name) {
        if (uniformLocs.containsKey(name))
            return uniformLocs.get(name);
        int loc = glGetUniformLocation(program, name);
        if (loc < 0)
            System.err.println("Uniform not found: " + name);
        uniformLocs.put(name, loc);
        return loc;
    }

    public void bind() {
        glUseProgram(program);
    }

    public void update(RenderEngine engine) {
        ;
    }
}
