#version 330

in vec2 texCoord0;
out vec4 finalColor;
uniform sampler2D texture;
uniform int lightNumber;

void main()
{
	vec4 color = texture2D(texture, texCoord0);
	vec3 rgbPart = color.rgb / lightNumber; 
	finalColor = vec4(rgbPart, color.w);
}