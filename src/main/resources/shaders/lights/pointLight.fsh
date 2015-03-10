#version 330

#include lights/fragment_lighting.fsh

uniform PointLight pointLight;

vec4 calcLightEffect(vec3 normal, vec3 worldPos0)
{
	return calcPointLight(pointLight, normal, worldPos0);
}

#include lights/base_lighting.fsh
