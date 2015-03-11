#version 410
    
#include lights/fragment_lighting.fsh
    
uniform SpotLight spotLight;
    
vec4 calcLightEffect(vec3 normal, vec3 worldPos0)
{
	vec4 color = calcSpotLight(spotLight, normal, worldPos0);
 	return color;
}
   
#include lights/base_lighting.fsh