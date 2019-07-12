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
    private boolean thaiFixesShouldRemoveAdvance = false;
    private boolean thaiFixesFlag = false;
    private char thaiFixesCharacter;

    @Shadow
    private int width;

    @Inject(method = "getAdvance()F", at = @At("HEAD"), cancellable = true, remap = false)
    public void getAdvance(CallbackInfoReturnable<Float> ci)
    {
        if (this.thaiFixesShouldRemoveAdvance)
        {
            ci.setReturnValue(0.0F);
        }
    }

    @Override
    public float getRealAdvance()
    {
        return this.width / 2 + 1;
    }

    @Override
    public void setRemoveAdvance(boolean flag)
    {
        this.thaiFixesShouldRemoveAdvance = flag;
    }

    @Override
    public boolean getFlag()
    {
        return this.thaiFixesFlag;
    }

    @Override
    public void setFlag(boolean flag)
    {
        this.thaiFixesFlag = flag;
    }

    @Override
    public char getCharacters()
    {
        return this.thaiFixesCharacter;
    }

    @Override
    public void setCharacters(char thaiFixesCharacter)
    {
        this.thaiFixesCharacter = thaiFixesCharacter;
    }
}