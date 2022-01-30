#pragma once
#include "Wall.h"
#include "Point.h"
#include "ExtraMath.h"
#include "AngleTable.h"
#include <vector>
class Ray {
public:
	Ray(float x, float y, float degrees);
	void updateRotation(AngleTable angles);
	void raycast(std::vector<Wall> walls, int wall_index);
	float getX1();
	void setX1(float val);
	float getX2();
	void setX2(float val);
	float getY1();
	void setY1(float val);
	float getY2();
	void setY2(float val);
private:
	float x1, y1, x2, y2;
	float degrees;
	float distance;

	bool isPortal;

	int wall_texture_id;

	bool hitHorizontal = false;
	bool hit_something = false;

	Wall wall = Wall(-ENGINE_MAX_VAL, -ENGINE_MAX_VAL, -ENGINE_MAX_VAL, -ENGINE_MAX_VAL);

	double objectDistance = 0;

	void setDistance(float dist);
};