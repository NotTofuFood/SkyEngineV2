#include "Point.h"

Point::Point(float x, float y) {
	this->x = x;
	this->y = y;
}

void Point::setX(float x) {
	this->x = x;
}

void Point::setY(float y) {
	this->y = y;
}

float Point::getX() {
	return this->x;
}

float Point::getY() {
	return this->y;
}

bool Point::getNull() {
	return this->null;
}

void Point::setNull(bool b) {
	this->null = b;
}