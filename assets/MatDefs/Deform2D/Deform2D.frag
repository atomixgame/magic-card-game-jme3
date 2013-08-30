varying vec2 texCoord;

uniform sampler2D m_ColorMap;
uniform vec4 m_Color;
uniform float g_Time;

#ifdef SET_SCALE_XY
    uniform vec2 m_ScaleXY;
#else
    vec2 m_ScaleXY = vec2(1,1);
#endif


#ifdef SET_SCALE_TIME
    uniform float m_ScaleTime;
#else
    float m_ScaleTime = 1;
#endif

#ifdef DEF_SCROLL_XY
    uniform vec2 m_ScrollXY;
#else
    vec2 m_ScrollXY = vec2(0,0);
#endif

#ifdef DEF_SCROLL_XY
    uniform vec2 m_SkewXY;
#else
    vec2 m_SkewXY = vec2(0,0);

    
#endif

void main(){
    vec2 position = texCoord;
    vec2 texCoord2 = texCoord;
/* =====================================================
* TEXTURE COORDINATE EFFECTS
* Effects that Transform the texCoord
*/

/*
* 1) Scroll X or Y
*/
#ifdef DEF_SCROLL_XY
    texCoord2 += m_ScrollXY * g_Time/m_ScaleTime;


#endif

#ifdef DEF_SKEW_XY
    texCoord2.x += texCoord.y * m_SkewXY.x/m_ScaleTime;
    texCoord2.y += texCoord.x * m_SkewXY.y/m_ScaleTime;
#endif

    texCoord2 = mod(texCoord2, vec2(1,1));
    vec4 mixedCol = vec4(0,0,0,1);
    vec4 texColor = texture2D(m_ColorMap, texCoord2);
/* =====================================================
* COLOR MIXING EFFECTS
* Effects that Transform the texCoord
*/

/*
* 1) TEXTURE MAP
* Mix the values Color and the ColorMap by alpha of the ColorMap
*/
#ifdef MIX_COL_ALPHA
    mixedCol = vec4(mix(m_Color.rgb, texColor.rgb, texColor.a), texColor.a);
#else
    mixedCol = texColor * m_Color;
#endif

/*
* Mix with a Blob
*/
#ifdef MIX_BLOB
    float color = 0.0;
    color += sin( position.x * cos( g_Time / (1.5 * m_ScaleTime)) * (8.0 * m_ScaleXY.x)) + cos( position.y * cos( g_Time / (1.5 * m_ScaleTime)) * (1.0 * m_ScaleXY.x));
    color += sin( position.y * sin( g_Time / (1.0 * m_ScaleTime)) * (4.0 * m_ScaleXY.x)) + cos( position.x * sin( g_Time / (2.5 * m_ScaleTime)) * (4.0 * m_ScaleXY.x));
    color += sin( position.x * sin( g_Time / (0.5 * m_ScaleTime)) * (1.0 * m_ScaleXY.x)) + sin( position.y * sin( g_Time / (3.5 * m_ScaleTime)) * (8.0 * m_ScaleXY.x));
    color *= sin( g_Time / (1.0* m_ScaleTime)) * 0.5;
    vec4 blobCol = vec4( vec3( color, color * 0.5, sin( color + g_Time / 3.0 ) * 0.75 ), 1.0 );
    mixedCol = mix(blobCol,mixedCol,0.8);
#endif

    gl_FragColor = mixedCol;

}