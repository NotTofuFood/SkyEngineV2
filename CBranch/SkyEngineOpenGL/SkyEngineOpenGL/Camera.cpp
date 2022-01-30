#include "Camera.h"
#include <iostream>
Camera::Camera(float x, float y, int width) {
	this->x = x;
	this->y = y;
	for (int i = 0; i < width; i++) {
		rays.push_back(Ray(x, y, ENGINE_TORAD * (i*FOV*multiplier / width)));
	}
}

void Camera::update(int width) {
	for (int i = 0; i < width; i++) {
		rays.at(i).setY1(x);
		rays.at(i).setX1(y);
	}
}
