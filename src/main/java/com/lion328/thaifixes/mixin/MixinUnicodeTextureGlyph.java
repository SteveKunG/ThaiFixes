package com.lion328.thaifixes.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.lion328.thaifixes.IThaiFixesUnicodeGlyph;

@Mixin(targets = "net.minecraft.client.font.UnicodeTextureFont$UnicodeTextureGlyph", priority = 500)
public abstract class MixinUnicodeTextureGlyph implements IThaiFixesUnicodeGlyph
{
    private boolean shouldRemoveAdvance = false;
    private boolean flag = false;
    private char characters;

    @Shadow
    private int width;

    @Inject(method = "getAdvance()F", at = @At("HEAD"), cancellable = true, remap = false)
    public void getAdvance(CallbackInfoReturnable<Float> info)
    {
        if (this.shouldRemoveAdvance)
        {
            info.setReturnValue(0.0F);
        }
    }

    @Override
    public float getRealAdvance()
    {
        return this.width / 2 + 1;
    }

    @Override
    public void setRemoveAdvance(boolean shouldRemoveAdvance)
    {
        this.shouldRemoveAdvance = shouldRemoveAdvance;
    }

    @Override
    public boolean getFlag()
    {
        return this.flag;
    }

    @Override
    public void setFlag(boolean flag)
    {
        this.flag = flag;
    }

    @Override
    public char getCharacters()
    {
        return this.characters;
    }

    @Override
    public void setCharacters(char characters)
    {
        this.characters = characters;
    }
}