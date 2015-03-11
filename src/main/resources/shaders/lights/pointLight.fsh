#version 330

#include lights/fragment_lighting.fsh

uniform PointLight pointLight;

vec4 calcLightEffect(vec3 normal, vec3 worldPos0)
{
	vec4 color = calcPointLight(pointLight, normal, worldPos0);
	if(length(color.rgb) == 0)
		color = vec4(1,1,1,1);
	return color;
}

#include lights/base_lighting.fsh
