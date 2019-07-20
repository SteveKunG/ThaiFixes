package com.lion328.thaifixes;

import com.lion328.thaifixes.mixin.MixinGlyphRenderer;

import net.minecraft.client.font.GlyphRenderer;
import net.minecraft.client.font.RenderableGlyph;

public class GlyphProcessor
{
    public static GlyphRenderer processGlyph(RenderableGlyph glyph, GlyphRenderer glyphRenderer)
    {
        if (glyph instanceof IThaiFixesUnicodeGlyph)
        {
            IThaiFixesUnicodeGlyph thaiFixesGlyph = (IThaiFixesUnicodeGlyph) glyph;

            if (!thaiFixesGlyph.getFlag())
            {
                return null;
            }
            MixinGlyphRenderer converted = (MixinGlyphRenderer)glyphRenderer;
            float posYShift = ThaiFixes.texturedGlyphOffsetMap.get(thaiFixesGlyph.getCharacters()).yOffset;
            float height = ThaiFixes.texturedGlyphOffsetMap.get(thaiFixesGlyph.getCharacters()).heightOffset;
            float v0 = converted.thaiFixesVMin() + posYShift / 128.0F;
            float y0 = converted.thaiFixesYMin() + posYShift;
            return new GlyphRenderer(glyphRenderer.getId(), converted.thaiFixesUMin(), converted.thaiFixesUMax(), v0, v0 + height / 128.0F, converted.thaiFixesXMin() - thaiFixesGlyph.getRealAdvance(), converted.thaiFixesXMax() - thaiFixesGlyph.getRealAdvance(), y0, y0 + height);
        }
        else
        {
            return null;
        }
    }
}