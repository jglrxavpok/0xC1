#version 330

#include base_test.fsh

out vec4 color;
uniform sampler2D texture;

in vec2 texCoord0;

void main() {
	vec4 sample = texture2D(texture, texCoord0);
	color = test(sample);
}