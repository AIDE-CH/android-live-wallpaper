#version 300 es
in vec3 a_Position;
in vec3 a_Color;
in vec2 a_Texture;
in vec3 a_Normal;

out vec3 outColor;
out vec2 outTexture;
flat out int outHasColor;
flat out int outHasTexture;
flat out int outHasNormal;
out vec3 outFragPos;
out vec3 outNormal;


uniform int hasColor;
uniform int hasTexture;
uniform int hasNormal;
uniform int hasUniformColor;
uniform vec3 u_Color;

uniform mat4 a_Model;
uniform mat4 a_View;
uniform mat4 a_Projection;

void main()
{
    gl_Position = a_Projection*a_View*a_Model*vec4(a_Position, 1.0f);


    outHasColor = hasColor;
    outHasTexture = hasTexture;
    outHasNormal = hasNormal;

    outColor = vec3(1, 1, 1);
    if(hasUniformColor > 0){
        outColor *= u_Color;
    }
    if(outHasColor > 0) {
        outColor *= a_Color;
    }
    if(outHasTexture > 0) {
        outTexture = vec2(a_Texture.x, a_Texture.y);
    }
    if(hasNormal > 0){
        outFragPos = vec3(a_Model * vec4(a_Position, 1.0));
        //outNormal = mat3(transpose(inverse(a_Model))) * normalize(a_Normal);
        outNormal = mat3(transpose(inverse(a_Model))) * a_Normal;
    }
}