#pragma once

#include <GL/glew.h>
#include <iostream>

class TextureManager
{
public:

	TextureManager();

	unsigned char* initTexture(char* name, unsigned int type);

	void useTexture(unsigned char* id);

	int texture_width;
	int texture_height;
	int texture_channels;
	unsigned int texture_id;
	unsigned char* texture_use_id;

};