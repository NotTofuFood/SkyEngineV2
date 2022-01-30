#pragma once
class Point {
public: 
	Point(float x, float y);
	float getX();
	float getY();
	void setX(float x);
	void setY(float y);
	bool null = false;
	bool getNull();
	void setNull(bool b);
private: 
	float x;
	float y;
};