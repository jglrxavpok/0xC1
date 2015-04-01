#version 410

out vec4 color;
uniform sampler2D texture;

in vec2 texCoord0;
in vec4 baseColor;

uniform vec3 ambientColor;

void main() {
	vec4 sampleColor = texture2D(texture, texCoord0) * baseColor;
	vec3 colorMult = ambientColor;
	color = vec4(sampleColor.rgb * colorMult, sampleColor.w);
}