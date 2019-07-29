/*
 * Copyright (c) 2017 Waritnan Sookbuntherng
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.lion328.thaifixes.mixin;

import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.lion328.thaifixes.ThaiFixes;
import com.lion328.thaifixes.ThaiUtils;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;

@Mixin(FontRenderer.class)
public abstract class ThaiFontRendererMixin
{
    static
    {
        ThaiFixes.LOGGER.info("ThaiFontRendererMixin loaded, Thai Characters now render correctly!");
    }

    @Shadow
    @Final
    private byte[] glyphWidth;

    @Shadow
    private float posX;

    @Shadow
    private float posY;

    @Shadow
    protected abstract void loadGlyphTexture(int page);

    @Inject(method = "renderUnicodeChar(CZ)F", cancellable = true, at = @At("HEAD"))
    protected void renderThaiChar(char ch, boolean italic, CallbackInfoReturnable info)
    {
        int i = this.glyphWidth[ch] & 255;

        if (ThaiUtils.isSpecialThaiChar(ch))
        {
            float posYShift = 0.0F;
            float height = 2.99F;
            this.loadGlyphTexture(0x0E);

            if (ThaiUtils.isLowerThaiChar(ch))
            {
                height = 1.99F;
                posYShift = 6.0F;
            }

            float heightX2 = height * 2;
            float startTexcoordX = i >>> 4;
            float charWidth = (i & 15) + 1;
            float texcoordX = ch % 16 * 16 + startTexcoordX;
            float texcoordY = (ch & 255) / 16 * 16 + posYShift * 2;
            float texcoordXEnd = charWidth - startTexcoordX - 0.02F;
            float skew = italic ? 1.0F : 0.0F;
            float posX = this.posX - ((charWidth - startTexcoordX) / 2.0F + 0.5F);
            float posY = this.posY + posYShift;

            GlStateManager.glBegin(GL11.GL_TRIANGLE_STRIP);
            GlStateManager.glTexCoord2f(texcoordX / 256.0F, texcoordY / 256.0F);
            GlStateManager.glVertex3f(posX + skew, posY, 0.0F);
            GlStateManager.glTexCoord2f(texcoordX / 256.0F, (texcoordY + heightX2) / 256.0F);
            GlStateManager.glVertex3f(posX - skew, posY + height, 0.0F);
            GlStateManager.glTexCoord2f((texcoordX + texcoordXEnd) / 256.0F, texcoordY / 256.0F);
            GlStateManager.glVertex3f(posX + texcoordXEnd / 2.0F + skew, posY, 0.0F);
            GlStateManager.glTexCoord2f((texcoordX + texcoordXEnd) / 256.0F, (texcoordY + heightX2) / 256.0F);
            GlStateManager.glVertex3f(posX + texcoordXEnd / 2.0F - skew, posY + height, 0.0F);
            GlStateManager.glEnd();
            info.setReturnValue(0.0F);
        }
    }
}