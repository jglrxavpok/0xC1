#version 330

out vec4 color;
uniform sampler2D texture;

in vec2 texCoord0;

uniform vec3 ambientColor;

void main() {
	vec4 sample = texture2D(texture, texCoord0);
	vec3 colorMult = ambientColor;
	color = vec4(sample.rgb * colorMult, sample.w);
}