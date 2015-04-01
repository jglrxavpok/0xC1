#version 410

out vec4 finalColor;
in vec2 texCoord0;
in vec4 baseColor;
uniform sampler2D texture;
uniform int lightNumber;

void main()
{
	vec4 color = texture2D(texture, texCoord0) * baseColor;
	vec3 rgbPart = color.rgb / lightNumber; 
	finalColor = vec4(rgbPart, color.w);
}