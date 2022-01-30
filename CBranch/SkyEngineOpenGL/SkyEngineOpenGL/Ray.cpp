#include "Ray.h"

Ray::Ray(float x, float y, float degrees) {
	//angles = AngleTable();
	this->x1 = x;
	this->y1 = y;
	this->degrees = degrees;
	this->x2 = this->x1 + cos(this->degrees) * ENGINE_MAX_VAL;
	this->y2 = this->y1 + sin(this->degrees) * ENGINE_MAX_VAL;
}

void Ray::updateRotation(AngleTable angles) {
	x2 += angles.cos_a[(int)(abs(ENGINE_TODEG * (degrees)) * 100)] * ENGINE_MAX_VAL;
	y2 += angles.sin_a[(int)(abs(ENGINE_TODEG * (degrees)) * 100)] * ENGINE_MAX_VAL;
}

void Ray::setDistance(float dist) {
	this->distance = dist;
}

void Ray::raycast(std::vector<Wall> walls, int wall_index) {
	Point check = ExtraMath::pointIntersection(x1, y1, x2, y2, walls.at(wall_index).getX1(), walls.at(wall_index).getY1(), walls.at(wall_index).getX2(), walls.at(wall_index).getY2());
	if (!check.getNull()) {
		hit_something = true;
		x2 = check.getX();
		y2 = check.getY();
		wall = walls.at(wall_index);
		hitHorizontal = walls.at(wall_index).getWallType();
		double distance = ExtraMath::distance(x1, y1, x2, y2);
		//	isPortal = walls.get(wall_index).isPortal;
		wall_texture_id = wall_index;
		setDistance(distance);
	}
	else {
		hit_something = false;
	}
}

float Ray::getX1() {
	return this->x1;
}
void Ray::setX1(float val) {
	this->x1 = val;
}
float Ray::getX2() {
	return this->x2;
}
void Ray::setX2(float val) {
	this->x2 = val;
}
float Ray::getY1() {
	return this->y1;
}
void Ray::setY1(float val) {
	this->y1 = val;
}
float Ray::getY2() {
	return this->y2;
}
void Ray::setY2(float val) {
	this->y2 = val;
}