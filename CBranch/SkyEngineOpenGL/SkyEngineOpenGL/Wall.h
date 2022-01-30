#pragma once
#include <math.h>
class Wall {
public:
	Wall(float x1, float y1, float x2, float y2);
	float getX1();
	void setX1(float val);
	float getX2();
	void setX2(float val);
	float getY1();
	void setY1(float val);
	float getY2();
	void setY2(float val);
	bool getWallType();
private:
	float x1, y1, x2, y2;
};