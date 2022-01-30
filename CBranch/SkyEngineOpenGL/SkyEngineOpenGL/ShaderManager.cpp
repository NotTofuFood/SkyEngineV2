#include "ShaderManager.h"


ShaderManager::ShaderManager()
{
}

unsigned int ShaderManager::loadShader(char* vertex_location, char* fragment_location) {
	
	std::ifstream vertex_file(vertex_location);
	std::ifstream fragment_file(fragment_location);

	std::string data;
	std::string data_read;

	if (vertex_file) {

		while (std::getline(vertex_file, data_read)) {
			data.append(data_read + "\n");
		}

	}
	else {
		std::cout << "No Shader (Vertex) Exists At Path: " << vertex_location << std::endl; 
	}

	const char *vertex = data.c_str();

	std::string fragment_data;
	std::string fragment_data_read;

	if (fragment_file) {

		while (std::getline(fragment_file, fragment_data_read)) {
			fragment_data.append(fragment_data_read + "\n");
		}

	}
	else {
		std::cout << "No Shader (Fragment) Exists At Path: " << fragment_location << std::endl;
	}

	const char *fragment = fragment_data.c_str();

	vertex_file.close();
	fragment_file.close();

	unsigned int program;
	program = glCreateProgram();

	unsigned int vertex_shader = glCreateShader(GL_VERTEX_SHADER);
	unsigned int fragment_shader = glCreateShader(GL_FRAGMENT_SHADER);

	glShaderSource(vertex_shader, 1, &vertex, NULL);
	glShaderSource(fragment_shader, 1, &fragment, NULL);
	
	glCompileShader(vertex_shader);
	glCompileShader(fragment_shader);

	int compiled = 0;
	glGetShaderiv(vertex_shader, GL_COMPILE_STATUS, &compiled);
	if (compiled == GL_FALSE)
	{
		int length = 0;
		glGetShaderiv(vertex_shader, GL_INFO_LOG_LENGTH, &length);

		std::vector<char> log(length);
		glGetShaderInfoLog(vertex_shader, length, &length, &log[0]);

		std::cout << "Shader Not Compiled (Vertex), With Reason:\n" << log.data() << std::endl;

		glDeleteShader(vertex_shader); 
		return 0;
	}

	compiled = 0;

	glGetShaderiv(fragment_shader, GL_COMPILE_STATUS, &compiled);
	if (compiled == GL_FALSE)
	{
		int length = 0;
		glGetShaderiv(fragment_shader, GL_INFO_LOG_LENGTH, &length);

		std::vector<char> log(length);
		glGetShaderInfoLog(fragment_shader, length, &length, &log[0]);

		std::cout << "Shader Not Compiled (Fragment), With Reason:\n" << log.data() << std::endl;

		glDeleteShader(fragment_shader);
		return 0;
	}

	glAttachShader(program, vertex_shader);
	glAttachShader(program, fragment_shader);

	glLinkProgram(program);

	glValidateProgram(program);

	glDetachShader(program, vertex_shader);
	glDetachShader(program, fragment_shader);

	glDeleteShader(vertex_shader);
	glDeleteShader(fragment_shader);
	
	return program;
}

void ShaderManager::useProgram(unsigned int id) {

	glUseProgram(id);

}

ShaderManager::~ShaderManager()
{
}
