#include "Wall.h"

Wall::Wall(float x1, float y1, float x2, float y2) {
	this->x1 = x1;
	this->y1 = y1;
	this->x2 = x2;
	this->y2 = y2;
}

float Wall::getX1() {
	return this->x1;
}
void Wall::setX1(float val) {
	this->x1 = val;
}
float Wall::getX2() {
	return this->x2;
}
void Wall::setX2(float val) {
	this->x2 = val;
}
float Wall::getY1() {
	return this->y1;
}
void Wall::setY1(float val) {
	this->y1 = val;
}
float Wall::getY2() {
	return this->y2;
}
void Wall::setY2(float val) {
	this->y2 = val;
}
bool Wall::getWallType() {
	if (abs(x1 - x2) > abs(y1 - y2)) {
		return true;
	}
	else {
		return false;
	}
}