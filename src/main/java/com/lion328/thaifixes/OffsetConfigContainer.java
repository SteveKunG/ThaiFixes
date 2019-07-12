package com.lion328.thaifixes;

import java.util.List;

public class OffsetConfigContainer
{
    public List<OffsetConfig> offsets;

    public class OffsetConfig
    {
        public String characters;
        public TexturedGlyphOffsetConfig textured;
    }

    public class TexturedGlyphOffsetConfig
    {
        public float yOffset;
        public float heightOffset;
    }
}