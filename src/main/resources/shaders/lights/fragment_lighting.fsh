uniform vec3 Cam_eyePos;
uniform float specularIntensity;
uniform float specularPower;
uniform sampler2D lightMap;

struct BaseLight
{
    vec3 color;
    float intensity;
};

struct Attenuation
{
    float constant;
    float linear;
    float exponent;
};

struct PointLight
{
    BaseLight base;
    Attenuation atten;
    vec3 position;
    float range;
};

struct SpotLight
{
    PointLight pointLight;
    vec3 direction;
    float cutoff;
};

struct DirectionalLight
{
    BaseLight base;
    vec3 direction;
};

vec4 calcLight(BaseLight base, vec3 direction, vec3 normal, vec3 worldPos)
{
    float diffuseFactor = dot(normal, -direction);
    
    vec4 diffuseColor = vec4(0,0,0,0);
    vec4 specularColor = vec4(0,0,0,0);
    
    if(diffuseFactor > 0)
    {
        diffuseColor = vec4(base.color, 1.0) * base.intensity * diffuseFactor;
        
        vec3 directionToEye = normalize(Cam_eyePos - worldPos);
        vec3 halfDirection = normalize(directionToEye - direction);
        float specularFactor = dot(halfDirection, normal);
        float lightMapValue = texture2D(lightMap, vec2(specularFactor, 0)).r;
        specularFactor = lightMapValue;

        specularFactor = pow(specularFactor, specularPower);
        
        if(specularFactor > 0)
        {
            specularColor = vec4(base.color, 1.0) * specularIntensity * specularFactor;
        }
    }
    
    return diffuseColor + specularColor;
}

vec4 calcPointLight(PointLight pointLight, vec3 normal, vec3 worldPos)
{
    vec3 direction = worldPos - pointLight.position;
    float distanceToPoint = length(direction);
    
    if(distanceToPoint > pointLight.range)
        return vec4(0,0,0,0);
    
    direction = normalize(direction);
    
    vec4 color = calcLight(pointLight.base, direction, normal, worldPos);
    // ax² + bx + c
    float attenuation = pointLight.atten.constant + pointLight.atten.linear * distanceToPoint + pointLight.atten.exponent * distanceToPoint * distanceToPoint
    + 0.000001; // Please not divide by 0
    return color / attenuation;
}

vec4 calcSpotLight(SpotLight spotLight, vec3 normal, vec3 worldPos)
{
    vec3 lightDirection = normalize(worldPos - spotLight.pointLight.position);
    float spotFactor = dot(lightDirection, spotLight.direction);
    vec4 color = vec4(0,0,0,0);
    if(spotFactor > spotLight.cutoff)
    {
        color = calcPointLight(spotLight.pointLight, normal, worldPos) * (1.0 - (1.0 - spotFactor)/(1.0-spotLight.cutoff));
    }
    
    return color;
}

vec4 calcDirectionalLight(DirectionalLight directionalLight, vec3 normal, vec3 worldPos)
{
    return calcLight(directionalLight.base, -directionalLight.direction, normal,worldPos);
}