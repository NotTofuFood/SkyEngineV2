#pragma once
#include <vector>
#include "Ray.h"

class Camera {
public:
	float sensitivity = 1.5f;

	const double multiplier = 0.8;

	bool stop_up, stop_down;

	bool move_rot = false;

	AngleTable angles = AngleTable();

	Camera(float x, float y, int width);

	std::vector<Ray> rays;
	void update(int width);
private:
	int x;
	int y;

	int FOV;

	float rotation = 0.0f;

	float speed = 0.0f;

	float hsp = 0.0f;
	float vsp = 0.0f;
};