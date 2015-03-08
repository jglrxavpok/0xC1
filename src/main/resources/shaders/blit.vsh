#version 330

layout(location = 1) in vec3 pos;
layout(location = 2) in vec2 texCoords;
out vec2 texCoord0;

uniform mat4 modelview;
uniform mat4 projection;

void main() {
	gl_Position = projection * modelview * vec4(pos,1.0);
	texCoord0 = texCoords;
}