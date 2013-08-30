varying vec2 texCoord;

uniform sampler2D m_ColorMap;
uniform sampler2D m_ColorMap2;
uniform vec4 m_Color;
uniform bool m_MixGlow;

uniform float g_Time;

void main() {
    vec2 uv = texCoord;
    vec4 texColor;
    vec4 color;

    if (uv.x > 0.5){
        uv.x = (uv.x - 0.5) *2;
        texColor = texture2D(m_ColorMap, uv);
    } else {
        uv.x = uv.x *2;
        texColor = texture2D(m_ColorMap2, uv);
    }
    vec2 uvc = uv;
    uvc.x = uv.x - 0.5;
    uvc.y = uv.y - 0.5;
    vec2 borderSize = vec2(0.05,0.05);
    #ifdef GLOW
        if (m_MixGlow == true){
                float glowV=sqrt((uvc.x)*(uvc.x)+(uvc.y)*(uvc.y));
                vec4 glowC = mix(vec4(0.9,0.8,0.1,1),vec4(0.4,0.7,1,1),sin(g_Time));
                color = mix(texColor,glowC,glowV);

                if ((uv.x <borderSize.x || 1-uv.x<borderSize.x)||(uv.y <borderSize.y || 1-uv.y<borderSize.y)){
                    color+= vec4(0.8,0.8,0.1,0.5);
                }
        } else {
            color = texColor;
        }
    #else 
        color = texColor;
    #endif

    gl_FragColor = color;
}

