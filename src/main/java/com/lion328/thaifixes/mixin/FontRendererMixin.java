package com.lion328.thaifixes.mixin;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.lion328.thaifixes.Core;
import com.lion328.thaifixes.ThaiFixesConfigManager;
import com.lion328.thaifixes.ThaiUtil;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

@Mixin(FontRenderer.class)
public abstract class FontRendererMixin
{
    private int[] thaiCharWidth;
    private char lastChar;
    private static final ResourceLocation MCPX = new ResourceLocation("thaifixes:textures/font/mcpx.png");

    @Shadow
    protected byte[] glyphWidth;

    @Shadow
    protected float posX;

    @Shadow
    protected float posY;

    @Shadow
    private void loadGlyphTexture(int page) {}

    @Shadow(remap = false)
    protected abstract void bindTexture(ResourceLocation location);

    @Shadow(remap = false)
    protected abstract InputStream getResourceInputStream(ResourceLocation location) throws IOException;

    @Inject(method = "onResourceManagerReload", at = @At("RETURN"))
    private void onResourceManagerReload(IResourceManager resourceManager, CallbackInfo info)
    {
        this.loadMCPXTexture();
    }

    @Inject(method = "getCharWidth", cancellable = true, at = @At("HEAD"))
    private void getCharWidth(char ch, CallbackInfoReturnable info)
    {
        if (this.isSupportedCharacter(ch))
        {
            if (ThaiFixesConfigManager.fontRendererType.equals("unicode"))
            {
                info.setReturnValue(0);
            }
            else
            {
                if (ThaiUtil.isSpecialThaiChar(ch))
                {
                    info.setReturnValue(0);
                }
                info.setReturnValue(this.thaiCharWidth[ch - ThaiUtil.THAI_CHAR_RANGE_MIN + 1]);
            }
        }
    }

    @Inject(method = "func_181559_a", cancellable = true, at = @At("HEAD"))
    private void renderChars(char ch, boolean italic, CallbackInfoReturnable info)
    {
        if (this.isSupportedCharacter(ch))
        {
            if (ThaiFixesConfigManager.fontRendererType.equals("unicode"))
            {
                this.loadGlyphTexture(0x0E);
                float posYShift = 0.0F;
                float height = 2.99F;

                if (ThaiUtil.isLowerThaiChar(ch))
                {
                    height = 1.99F;
                    posYShift = 6.0F;
                }

                float heightX2 = height * 2;
                byte rawWidth = this.glyphWidth[ch];
                float startTexcoordX = rawWidth >>> 4;
                float charWidth = (rawWidth & 15) + 1;
                float texcoordX = ch % 16 * 16 + startTexcoordX;
                float texcoordY = (ch & 255) / 16 * 16 + posYShift * 2;
                float texcoordXEnd = charWidth - startTexcoordX - 0.02F;
                float skew = italic ? 1.0F : 0.0F;
                float posX = this.posX - ((charWidth - startTexcoordX) / 2.0F + 0.5F);
                float posY = this.posY + posYShift;

                GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
                GL11.glTexCoord2f(texcoordX / 256.0F, texcoordY / 256.0F);
                GL11.glVertex3f(posX + skew, posY, 0.0F);
                GL11.glTexCoord2f(texcoordX / 256.0F, (texcoordY + heightX2) / 256.0F);
                GL11.glVertex3f(posX - skew, posY + height, 0.0F);
                GL11.glTexCoord2f((texcoordX + texcoordXEnd) / 256.0F, texcoordY / 256.0F);
                GL11.glVertex3f(posX + texcoordXEnd / 2.0F + skew, posY, 0.0F);
                GL11.glTexCoord2f((texcoordX + texcoordXEnd) / 256.0F, (texcoordY + heightX2) / 256.0F);
                GL11.glVertex3f(posX + texcoordXEnd / 2.0F - skew, posY + height, 0.0F);
                GL11.glEnd();
                this.lastChar = ch;
                info.setReturnValue(0.0F);
            }
            else
            {
                int offset = ch - ThaiUtil.THAI_CHAR_RANGE_MIN + 1;
                float cPosX = this.posX;
                float cPosY = this.posY;

                if (ThaiUtil.isSpecialThaiChar(ch))
                {
                    cPosX -= this.thaiCharWidth[offset];

                    if (ThaiUtil.isUpperThaiChar(ch))
                    {
                        cPosY -= 7.0F;
                    }
                    else
                    {
                        cPosY += 2.0F;
                    }
                    if (ThaiUtil.isVeryLongTailThaiChar(this.lastChar))
                    {
                        cPosY -= 1.0F;
                    }
                    if (ThaiUtil.isSpecialThaiChar(this.lastChar))
                    {
                        cPosY -= 2.25F;
                    }
                }

                float texcoordX = offset % 16 * 8;
                float texcoordY = offset / 16 * 8;
                float italicSize = italic ? 1.0F : 0.0F;
                float f3 = this.thaiCharWidth[offset] - 0.01F;

                this.bindTexture(MCPX);

                GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
                GL11.glTexCoord2f(texcoordX / 128.0F, texcoordY / 128.0F);
                GL11.glVertex2f(cPosX + italicSize, cPosY);
                GL11.glTexCoord2f(texcoordX / 128.0F, (texcoordY + 7.99F) / 128.0F);
                GL11.glVertex2f(cPosX - italicSize, cPosY + 7.99F);
                GL11.glTexCoord2f((texcoordX + f3 - 1.0F) / 128.0F, texcoordY / 128.0F);
                GL11.glVertex2f(cPosX + f3 - 1.0F + italicSize, cPosY);
                GL11.glTexCoord2f((texcoordX + f3 - 1.0F) / 128.0F, (texcoordY + 7.99F) / 128.0F);
                GL11.glVertex2f(cPosX + f3 - 1.0F - italicSize, cPosY + 7.99F);
                GL11.glEnd();
                this.lastChar = ch;
                info.setReturnValue(ThaiUtil.isSpecialThaiChar(ch) ? 0 : (float) this.thaiCharWidth[offset]);
            }
        }
    }

    private boolean isSupportedCharacter(char ch)
    {
        return ThaiFixesConfigManager.fontRendererType.equals("unicode") ? ThaiUtil.isSpecialThaiChar(ch) : ThaiUtil.isThaiChar(ch);
    }

    private void loadMCPXTexture()
    {
        this.thaiCharWidth = new int[256];
        BufferedImage bufferedimage = null;

        try
        {
            bufferedimage = TextureUtil.readBufferedImage(this.getResourceInputStream(MCPX));
        }
        catch (IOException e)
        {
            Core.LOGGER.catching(e);
        }

        int width = bufferedimage.getWidth();
        int height = bufferedimage.getHeight();
        int[] texture = new int[width * height];
        bufferedimage.getRGB(0, 0, width, height, texture, 0, width);
        int xSize = width / 16;
        int ySize = height / 16;
        byte space = 1;
        float f = 8.0F / xSize;
        int charPos = 0;

        while (charPos < 256)
        {
            int col = charPos % 16;
            int row = charPos / 16;
            int l1 = xSize - 1;

            while (true)
            {
                if (l1 >= 0)
                {
                    boolean end = true;

                    for (int j2 = 0; j2 < ySize && end; ++j2)
                    {
                        if ((texture[col * xSize + l1 + (row * xSize + j2) * width] >> 24 & 0xFF) != 0)
                        {
                            end = false;
                        }
                    }
                    if (end)
                    {
                        --l1;
                        continue;
                    }
                }
                ++l1;
                this.thaiCharWidth[charPos] = (int) (0.5D + l1 * f) + space;
                ++charPos;
                break;
            }
        }
    }
}