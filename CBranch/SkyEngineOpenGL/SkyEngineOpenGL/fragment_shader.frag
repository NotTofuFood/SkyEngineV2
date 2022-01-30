#version 410
layout(location=0) out vec4 color;
vec2 res = vec2(1980 , 1080);

int imod(int a, int b)
{
	return a - a / b * b;
}
int xor(int a, int b)
{
	int result = 0;
	int x = 1;
	for(int i = 0; i <= 8; ++i)
    {
        if (imod(a,2) != imod(b,2))
            result += x;
        a /= 2;
        b /= 2;
        x *= 2;
	}
	return result;
}

bool drawWall(vec3 pos, vec2 uv) {
    int thickness = 1;

    float v = float( pos.x ) / res.x;
    float vy1 = float( res.y-pos.y ) / res.y;
    float vy2 = float( res.y-pos.z ) / res.y;
    float vHalfWidth = ( float( thickness ) / ( res.x ) ) / 2.;
    float vHalfHeight = ( float( thickness ) / ( res.y ) ) / 2.;

	if ( uv.x > v - vHalfWidth && uv.x < v + vHalfWidth && (uv.y > vy1-vHalfHeight && uv.y < vy2+vHalfHeight))
       return true;
	return false;
}

void main()
{

	vec2 uv = gl_FragCoord.xy / res.xy;
    vec2 xy = uv * vec2(320.0,200.0);
	if(drawWall(vec3(250, 500,100), uv)) {
		color = vec4(1,1,1,1.0);
	} else {
		color = vec4(0,0,0,1.0);
	}
}