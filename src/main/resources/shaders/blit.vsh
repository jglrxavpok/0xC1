#version 410

layout(location = 1) in vec3 pos;
layout(location = 2) in vec2 texCoords;
layout(location = 5) in vec4 color;

out vec2 texCoord0;
out vec4 finalWorldPos;
out vec4 baseColor;

uniform mat4 modelview;
uniform mat4 projection;

void main() {
	finalWorldPos = projection * modelview * vec4(pos,1.0);
	gl_Position = finalWorldPos;
	texCoord0 = texCoords;
	baseColor = color;
}