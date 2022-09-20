#version 150

in vec3 Position;
in vec4 Color;

uniform mat4 u_ProjMat;

out vec4 vertexColor;

void main() {
    gl_Position = u_ProjMat * vec4(Position, 1.0);
    vertexColor = vec4(0.5, 0, 0.5, 1);
}