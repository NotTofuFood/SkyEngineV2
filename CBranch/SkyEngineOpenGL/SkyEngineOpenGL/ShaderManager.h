#pragma once

#include <iostream>
#include <fstream>
#include <string>
#include <vector>
#include <GL/glew.h>

class ShaderManager
{
public:
	ShaderManager();

	unsigned int loadShader(char* vertex_location, char* fragment_location);
	void useProgram(unsigned int id);

	~ShaderManager();
};
