#version 410

in vec4 finalWorldPos;

void main()
{
	float depth = finalWorldPos.z;

	float dx = dFdx(depth);
	float dy = dFdy(depth);
	float variance = depth*depth + 0.25*(dx*dx+dy*dy);
	gl_FragColor = vec4(depth, variance,
		0.0,0.0);
}
