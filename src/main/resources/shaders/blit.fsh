#version 410

out vec4 color;
uniform sampler2D texture;

in vec2 texCoord0;
in vec4 baseColor;

void main() {
	vec4 sampleColor = texture2D(texture, texCoord0) * baseColor;
	color = sampleColor;
}