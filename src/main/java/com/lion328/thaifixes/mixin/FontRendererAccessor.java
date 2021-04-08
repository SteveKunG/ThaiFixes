package com.lion328.thaifixes.mixin;

import java.io.IOException;
import java.io.InputStream;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;

@Mixin(FontRenderer.class)
public interface FontRendererAccessor
{
    @Accessor
    byte[] getGlyphWidth();

    @Accessor
    float getPosX();

    @Accessor
    float getPosY();

    @Invoker
    void callLoadGlyphTexture(int page);

    @Invoker(remap = false)
    void callBindTexture(ResourceLocation location);

    @Invoker(remap = false)
    InputStream callGetResourceInputStream(ResourceLocation location) throws IOException;
}