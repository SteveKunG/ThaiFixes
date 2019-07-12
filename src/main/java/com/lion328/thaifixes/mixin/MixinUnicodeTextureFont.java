package com.lion328.thaifixes.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.lion328.thaifixes.IThaiFixesUnicodeGlyph;
import com.lion328.thaifixes.ThaiFixes;

import net.minecraft.client.font.RenderableGlyph;
import net.minecraft.client.font.UnicodeTextureFont;

@Mixin(UnicodeTextureFont.class)
public abstract class MixinUnicodeTextureFont
{
    @Inject(method = "getGlyph", at = @At("RETURN"), cancellable = true)
    private void getGlyph(char ch, CallbackInfoReturnable<RenderableGlyph> info)
    {
        if (ThaiFixes.texturedGlyphOffsetMap.get(ch) == null)
        {
            return;
        }

        IThaiFixesUnicodeGlyph glyph = (IThaiFixesUnicodeGlyph)info.getReturnValue();

        if (glyph == null)
        {
            return;
        }
        glyph.setRemoveAdvance(true);
        glyph.setFlag(true);
        glyph.setCharacters(ch);
    }
}