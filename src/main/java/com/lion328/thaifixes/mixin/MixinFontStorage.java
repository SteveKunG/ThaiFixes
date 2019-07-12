package com.lion328.thaifixes.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.lion328.thaifixes.GlyphProcessor;

import net.minecraft.client.font.FontStorage;
import net.minecraft.client.font.GlyphRenderer;
import net.minecraft.client.font.RenderableGlyph;

@Mixin(FontStorage.class)
public abstract class MixinFontStorage
{
    @Inject(method = "getGlyphRenderer(Lnet/minecraft/client/font/RenderableGlyph;)Lnet/minecraft/client/font/GlyphRenderer;", at = @At("RETURN"), cancellable = true)
    private void onGetGlyphRendererUnicode(RenderableGlyph glyph, CallbackInfoReturnable<GlyphRenderer> info)
    {
        GlyphRenderer result = GlyphProcessor.processGlyph(glyph, info.getReturnValue());

        if (result != null)
        {
            info.setReturnValue(result);
        }
    }
}