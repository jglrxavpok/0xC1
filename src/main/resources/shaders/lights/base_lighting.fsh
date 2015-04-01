#include lights/base_sampling.fsh
in vec2 texCoord0;
in vec3 worldPos0;
in mat3 tbnMatrix;
in vec3 normal0;
in vec4 shadowMapCoords0;
in vec4 baseColor;

out vec4 fragColor;

uniform sampler2D diffuse;
uniform sampler2D R_shadowMap;

uniform float dispMapScale;
uniform float dispMapBias;

uniform float R_shadowVarianceMin;
uniform float R_shadowLightBleedingReduction;
uniform vec3 R_shadowColor = vec3(0,0,0);

bool inRange(float val)
{
	return val >= 0 && val <= 1.0;
}

float calcShadowMapEffect(sampler2D shadowMap, vec4 shadowMapCoords)
{
	vec3 shadowMapCoord1 = shadowMapCoords.xyz/shadowMapCoords.w;
	if(inRange(shadowMapCoord1.x) && inRange(shadowMapCoord1.y) && inRange(shadowMapCoord1.z))
		return sampleVarianceShadowMap(shadowMap, shadowMapCoord1.xy, shadowMapCoord1.z, R_shadowVarianceMin, R_shadowLightBleedingReduction);
	return 0.5f;
}

void main()
{
	vec3 directionToEye = normalize(Cam_eyePos - worldPos0);
	vec2 texCoords = texCoord0;
//	if(R_parallaxDispMappingEnabled)
//		texCoords = calcParallaxTexCoords(dispMap, tbnMatrix, directionToEye, texCoord0, dispMapScale, dispMapBias);

	vec3 normal = normal0;
//	if(R_normalMappingOn)
//		normal = normalize( (tbnMatrix * (( (texture2D(normalMap, texCoords).xyz - 0.5 ) * 2.0) )) );

	vec4 lightAmount = vec4(1,1,1,1);
//	if(!R_lightingOff)
		lightAmount = calcLightEffect(normal, worldPos0);
//	if(R_shadowingEnabled)
		lightAmount = lightAmount * mix(vec4(1), vec4(R_shadowColor,1), 1.0-calcShadowMapEffect(R_shadowMap, shadowMapCoords0));
	vec4 color = texture2D(diffuse, texCoords.xy) * baseColor * lightAmount;
    fragColor = color;
}