#version 150

in vec3 position;
in vec4 color;

out vec4 vertexColor;

uniform mat4 u_ProjMat;

void main(void)
{
    gl_Position = u_ProjMat * vec4(position, 1.0);
    vertexColor = color;
}
