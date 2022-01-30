#pragma once
#include "Point.h"
#include "math.h"
#define ENGINE_TORAD 0.0174533
#define ENGINE_TODEG 57.2958
#define ENGINE_MAX_VAL 2147483647
class ExtraMath {
public: 
	static Point ExtraMath::pointIntersection(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
		float t1 = (x1 - x3) * (y3 - y4) - (y1 - y3) * (x3 - x4);
		float t2 = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);

		float u1 = (x1 - x2) * (y1 - y3) - (y1 - y2) * (x1 - x3);
		float u2 = t2;

		if (t2 == 0) {
			Point p = Point(-123, -123);
			p.setNull(true);
			return p;
		}

		float t = t1 / t2;

		float u = -(u1 / u2);

		float x;
		float y;

		if ((0 <= t && t <= 1) && (0 <= u && u <= 1)) {
			x = x1 + (t * (x2 - x1));
			y = y1 + (t * (y2 - y1));
			return Point(x, y);
		}
		else {
			Point p = Point(-123, -123);
			p.setNull(true);
			return p;
		}
	}
	static double ExtraMath::distance(double x1, double y1, double x2, double y2) {
		return sqrt(pow(x2 - x1, 2) + pow(y2 - y1, 2));
	}

	static double ExtraMath::clamp(double value, double min_val, double max_val) {
		return fmin(fmax(value, min_val), max_val);
	}

	static double ExtraMath::lerp(double a, double b, double f) {
		return a + f * (b - a);
	}

	static double ExtraMath::smoothstep(double edge0, double edge1, double x) {
		x = clamp((x - edge0) / (edge1 - edge0), 0.0, 1.0);
		return x * x * (3 - 2 * x);
	}
};