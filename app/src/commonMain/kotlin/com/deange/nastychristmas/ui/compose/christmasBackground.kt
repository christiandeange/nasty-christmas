package com.deange.nastychristmas.ui.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun Modifier.christmasBackground(): Modifier

internal const val christmasSksl: String = """
uniform float uTime;
uniform vec3 uResolution;

vec4 main( vec2 fragCoord )
{
    float mr = min(uResolution.x, uResolution.y);
    vec2 uv = abs((fragCoord * 1.001 - uResolution.xy) / mr);

    // Slower movement
    float d = -uTime * 0.05; 
    float a = 0.0;

    for (float i = 0.0; i < 8.0; ++i) {
        a += cos(i - d - a * uv.x);
        d += sin(uv.y * i + a);
    }

    d += uTime * 0.05; 

    // Christmas colors
    vec3 green1 = vec3(0.2, 0.6, 0.2); // Dark green
    vec3 green2 = vec3(0.4, 0.8, 0.4); // Medium green
    vec3 green3 = vec3(0.6, 0.9, 0.6); // Light green

    // Smoother blending with smoothstep
    float blend1 = smoothstep(0.0, 1.0, sin(a + d) * 0.5 + 0.5);
    float blend2 = smoothstep(0.0, 1.0, cos(uv.x * d) * 0.3 + 0.2);

    vec3 col = mix(green1, green2, blend1); 
    col = mix(col, green3, blend2); 

    return vec4(col, 1.0);
}
"""
