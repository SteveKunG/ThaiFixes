/*
 * Copyright (c) 2018 Waritnan Sookbuntherng
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

package com.lion328.thaifixes;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

public class ThaiFixes implements ModInitializer
{
    private static final Logger LOGGER = LogManager.getLogger("ThaiFixes");
    public static final Map<Character, OffsetConfigContainer.TexturedGlyphOffsetConfig> texturedGlyphOffsetMap = new HashMap<>();

    @Override
    public void onInitialize()
    {
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new SimpleSynchronousResourceReloadListener()
        {
            @Override
            public Identifier getFabricId()
            {
                return new Identifier("thaifixes:offsets");
            }

            @Override
            public void apply(ResourceManager resourceManager)
            {
                ThaiFixes.LOGGER.info("Reloading ThaiFixes offset configurations.");
                Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
                resourceManager.findResources("offsets", s -> s.endsWith(".json")).forEach(e ->
                {
                    if (!e.getNamespace().equals("thaifixes"))
                    {
                        return;
                    }
                    try
                    {
                        resourceManager.getAllResources(e).forEach(res ->
                        {
                            try
                            {
                                ThaiFixes.LOGGER.info("Reading ThaiFixes offset configuration file " + e.getNamespace() + ":" + e.getPath());
                                InputStream is = res.getInputStream();
                                OffsetConfigContainer container = JsonHelper.deserialize(gson, IOUtils.toString(is, StandardCharsets.UTF_8), OffsetConfigContainer.class);
                                container.offsets.forEach(offset ->
                                {
                                    for (char c : offset.characters.toCharArray())
                                    {
                                        if (offset.textured != null)
                                        {
                                            texturedGlyphOffsetMap.put(c, offset.textured);
                                        }
                                    }
                                });
                            }
                            catch (IOException ex)
                            {
                                ex.printStackTrace();
                            }
                            catch (JsonParseException jsonEx)
                            {
                                jsonEx.printStackTrace();
                            }
                        });
                    }
                    catch (IOException e1)
                    {
                        e1.printStackTrace();
                    }
                });
                ThaiFixes.LOGGER.info("ThaiFixes is done reloading offset config.");
            }
        });
    }
}