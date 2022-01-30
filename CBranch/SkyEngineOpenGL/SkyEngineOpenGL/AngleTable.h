#pragma once
#include <math.h>
class AngleTable 
{
public: 
	double cos_a[60000];
	double sin_a[60000];

	double angle = 0;

	AngleTable() {
		for (int i = 0; i < 60000; i++)
		{
			angle += 0.000174533;
			cos_a[i] = cos(angle);
			sin_a[i] = sin(angle);
		}
	}

};
