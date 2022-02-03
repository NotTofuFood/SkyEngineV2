#include <stdio.h>

#include <math.h>

#define MIN_VAL 0.0001

#define WIDTH 320
#define HEIGHT 200

enum INTERSECTION_CASE {
  INTERSECTION,
  NO_INTERSECTION
};

typedef struct Vector3 {
  float x;
  float y;
  float z;
}
Vector3;

typedef struct Object {

  Vector3 position;
  Vector3 hit_position;
  Vector3 color;

  float diffuse_strength;
  float specular_strength;
  float shininess;

  float radius;
  float t;

}
Object;

Vector3 rays[WIDTH][HEIGHT];

float getX(Vector3 vec3) {
  return vec3.x;
};
float getY(Vector3 vec3) {
  return vec3.y;
};
float getZ(Vector3 vec3) {
  return vec3.z;
};

float getDotProduct(Vector3 v1, Vector3 v2) {
  return (v1.x * v2.x) + (v1.y * v2.y) + (v1.z * v2.z);
}

void constructVec3(Vector3 * vec, float x, float y, float z) {
  vec -> x = x;
  vec -> y = y;
  vec -> z = z;
}

Vector3 vector3(float x, float y, float z) {
  Vector3 new_vec;
  constructVec3( & new_vec, x, y, z);
  return new_vec;
}

Vector3 vecAdd(Vector3 v1, Vector3 v2) {
  Vector3 new_vector;
  new_vector.x = v1.x + v2.x;
  new_vector.y = v1.y + v2.y;
  new_vector.z = v1.z + v2.z;
  return new_vector;
};

Vector3 vecAddScaler(Vector3 v1, float scaler) {
  Vector3 new_vector;
  new_vector.x = v1.x + scaler;
  new_vector.y = v1.y + scaler;
  new_vector.z = v1.z + scaler;
  return new_vector;
};

Vector3 vecSubtract(Vector3 v1, Vector3 v2) {
  Vector3 new_vector;
  new_vector.x = v1.x - v2.x;
  new_vector.y = v1.y - v2.y;
  new_vector.z = v1.z - v2.z;
  return new_vector;
};

Vector3 vecSubtractScaler(Vector3 v1, float v2) {
  Vector3 new_vector;
  new_vector.x = v1.x - v2;
  new_vector.y = v1.y - v2;
  new_vector.z = v1.z - v2;
  return new_vector;
};

Vector3 vecMultiplyScaled(Vector3 v1, Vector3 v2) {
  Vector3 new_vector;
  new_vector.x = (v1.y * v2.z) - (v1.z * v2.y);
  new_vector.y = (v1.z * v2.x) - (v1.x * v2.z);
  new_vector.z = (v1.x * v2.y) - (v1.y * v2.x);
  return new_vector;
};

Vector3 vecMultiply(Vector3 v1, float scalar) {
  Vector3 new_vector;
  new_vector.x = (v1.y * scalar) - (v1.z * scalar);
  new_vector.y = (v1.z * scalar) - (v1.x * scalar);
  new_vector.z = (v1.x * scalar) - (v1.y * scalar);
  return new_vector;
};

Vector3 vecMultiplyScaler(Vector3 v1, float scale) {
  Vector3 new_vector;
  new_vector.x = v1.x * scale;
  new_vector.y = v1.y * scale;
  new_vector.z = v1.z * scale;
  return new_vector;
};

Vector3 vecDivideScaler(Vector3 v1, float scale) {
  Vector3 new_vector;
  new_vector.x = v1.x / scale;
  new_vector.y = v1.y / scale;
  new_vector.z = v1.z / scale;
  return new_vector;
};

float vecLength3D(Vector3 v1) {
  float x = v1.x * v1.x;
  float y = v1.y * v1.y;
  float z = v1.z * v1.z;
  return sqrt(x + y + z);
};

Vector3 getNormalizedVector(Vector3 vec) {
  Vector3 unit;
  unit = vecDivideScaler(vec, vecLength3D(vec));
  return unit;
}

Vector3 lookAtPoint(Vector3 vec, float look) {
  return vecAdd(vec, vecMultiply(getNormalizedVector(vec), look));
}

float getAngleOfVectors(Vector3 v1, Vector3 v2) {
  float dot = getDotProduct(v1, v2);
  float length = vecLength3D(v1) * vecLength3D(v2);
  return dot / length;
}

void reverseVector(Vector3 * vec) {
  vec -> x = -vec -> x;
  vec -> y = -vec -> y;
  vec -> z = -vec -> z;
};

float getClampedValue(float val, float restriction_0, float restriction_1) {
  return fmax(restriction_0, fmin(restriction_1, val));
}

float normalizeNumber(float val) {
  return val - 0.0 / 1.0 - 0.0;
}

enum INTERSECTION_CASE intersect_wall_plane(Vector3 ray, Vector3 ray_r, Vector3 plane_pos, Vector3 plane_r) {
      //thank you https://www.scratchapixel.com/lessons/3d-basic-rendering/minimal-ray-tracer-rendering-simple-shapes/ray-plane-and-ray-disk-intersection
    // for the plane intersection function
    float denom = getDotProduct(plane_r, ray_r);
    float t;
    if (denom > MIN_VAL) { 
        Vector3 com = vecSubtract(plane_pos, ray); 
        t = getDotProduct(com, plane_r) / denom; 
        return INTERSECTION; 
    } 

  return NO_INTERSECTION;
}

int main() {
    
for (auto row = 0; row < WIDTH; row++) {
		for (uint16_t col = 0; col < HEIGHT; col++) {
			float dir_x = (row + 0.5) - WIDTH / 2.;
			float dir_y = (col + 0.5) - HEIGHT / 2.;
			float dir_z = -HEIGHT / (2.*tan(FOV*TORADS / 2.));
			Vector3 ray; 
			ray.x = dir_X;
			ray.y = dir_z;
			
			rays[row][col] = Vector3(manager.vector3(dir_x, dir_z, dir_y));
		}
	}
	
  return 0;
}
