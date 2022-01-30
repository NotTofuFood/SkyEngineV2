#include <GL/glew.h>
#include <GLFW/glfw3.h>
#include <stdio.h>  
#include <stdlib.h> 
#include <iostream>
#include <time.h>
#include <vector>
#include "ShaderManager.h"

#include "Camera.h"

#define WIDTH 1980
#define HEIGHT 1080
#define CHUNK_SIZE 2
#define CHUNK_SIZE_Y 2

unsigned int createShader(char* vertex, char* frag);

bool firstMouse = true;

float lastX = WIDTH / 2.0;
float lastY = HEIGHT / 2.0;

float screen_verts[] = {
	1.f, 1.f, 0.0f,  // top right
	1.f, -1.f, 0.0f,  // bottom right
	-1.f, -1.f, 0.0f,  // bottom left
	-1.f, 1.f, 0.0f   // top left 
};
unsigned int indices[] = {
	0, 1, 3,
	1, 2, 3
};

/* Raycasting Defs */

Camera cam = Camera(300,300, WIDTH);
std::vector<Wall> walls;

void RenderScene3D(std::vector<Wall> walls) {
	cam.update(WIDTH);
	for (int i = 0; i < WIDTH; i++) {
		for (int wall = 0; wall < walls.size(); wall++) {
			cam.rays.at(i).raycast(walls, wall);
		}
	}
}

int main(void)
{
	/* Init Raycaster */
	srand(NULL);
	for (int wall = 0; wall < 30; wall++) {
		walls.push_back(Wall(rand(), 2, 2, 2));
	}

	/* End */
	GLFWwindow* window;

	if (!glfwInit())
		return -1;

	window = glfwCreateWindow(WIDTH, HEIGHT, "Sky Voxel - Aiden Thakurdial (Version 0.0.0)", NULL, NULL);
	if (!window)
	{
		glfwTerminate();
		return -1;
	}

	glfwMakeContextCurrent(window);

	if (glewInit() != GLEW_OK) {
		std::cout << "Error with initializing GLEW" << std::endl;
	}

	//glfwSetCursorPosCallback(window, mouse_callback);

	// tell GLFW to capture our mouse
	glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);

	ShaderManager shader_manager = ShaderManager();

	unsigned int program = shader_manager.loadShader("vertex_shader.vert", "fragment_shader.frag");
	glUseProgram(program);

	/* End */

	//glEnable(GL_DEPTH_TEST);

	float horizontal_camera = 0;
	float verticle_camera = 0;

	float deltaTime = 0.0f;	// Time between current frame and last frame
	float lastFrame = 0.0f; // Time of last frame

	float currentFrame = glfwGetTime();
	deltaTime = currentFrame - lastFrame;
	lastFrame = currentFrame;

	unsigned int VAO;

	glGenVertexArrays(1, &VAO);
	glBindVertexArray(VAO);

	unsigned int VBO;
	glGenBuffers(1, &VBO);

	glBindBuffer(GL_ARRAY_BUFFER, VBO);
	glBufferData(GL_ARRAY_BUFFER, sizeof(screen_verts), screen_verts, GL_STATIC_DRAW);

	unsigned int EBO;
	glGenBuffers(1, &EBO);

	glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO);
	glBufferData(GL_ELEMENT_ARRAY_BUFFER, sizeof(indices), indices, GL_STATIC_DRAW);

	glVertexAttribPointer(0, 3, GL_FLOAT, GL_FALSE, 3 * sizeof(float), (void*) 0);
	glEnableVertexAttribArray(0);

	glBindBuffer(GL_ARRAY_BUFFER, 0);
	glBindVertexArray(0);

	glBindVertexArray(VAO);
	glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO);

	double timeDelta = 1000 / 30;
	double timeAccumulator = 0;

	while (!glfwWindowShouldClose(window))
	{
		double timeSimulatedThisIteration = 0;
		double startTime = glfwGetTime();

		while (timeAccumulator >= timeDelta)
		{
			/* Raycast */
			RenderScene3D(walls);
			/* End */
			timeAccumulator -= timeDelta;
			timeSimulatedThisIteration += timeDelta;
		}

		/* Set Uniforms */

		/* Define Uniforms*/


		/* Camera */

		/* Movement */

		/* End */

		/* End */

		/* Render here */

		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glClearColor(0.,0.,0., 1);

		glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);


		glfwSwapBuffers(window);

		glfwPollEvents();
	}
	glDisableVertexAttribArray(0);
	glfwTerminate();
	return 0;
}