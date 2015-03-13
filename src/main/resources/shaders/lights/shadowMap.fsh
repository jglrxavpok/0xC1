#version 410

in vec4 finalWorldPos;
out vec4 finalColor;

void main()
{
	float depth = finalWorldPos.z;
	float dx = dFdx(depth);
	float dy = dFdy(depth);
	float variance = depth*depth + 0.25*(dx*dx+dy*dy);
	finalColor = vec4(depth, variance,
		0,1.0);
}
