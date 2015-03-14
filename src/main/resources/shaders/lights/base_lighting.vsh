#version 410

layout(location = 1) in vec3 position;
layout(location = 2) in vec2 texCoords;
layout(location = 3) in vec3 normal;
layout(location = 4) in vec3 tangent;

out vec2 texCoord0;
out vec3 worldPos0;
out mat3 tbnMatrix;
out vec3 normal0;
out vec4 shadowMapCoords0;

uniform mat4 modelview;
uniform mat4 projection;
uniform mat4 lightMatrix;

void main()
{
	gl_Position = projection * modelview * vec4(position, 1.0);
    texCoord0 = texCoords;
    normal0 = normal;
    shadowMapCoords0 = lightMatrix * modelview * vec4(position, 1.0);
    worldPos0 = (modelview * vec4(position, 1.0)).xyz;

    vec3 n = normalize((modelview * vec4(normal, 0.0)).xyz);
    vec3 t = normalize((modelview * vec4(tangent, 0.0)).xyz);
    t = -t;
    vec3 biTangent = cross(n, t);
    tbnMatrix = mat3(t, biTangent, n);
}