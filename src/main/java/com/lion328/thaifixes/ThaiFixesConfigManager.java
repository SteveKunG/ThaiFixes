package com.lion328.thaifixes;

import java.io.File;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.config.IConfigElement;

public class ThaiFixesConfigManager
{
    private static Configuration config;

    public static String fontRendererType = "unicode";

    public static void init(File file)
    {
        ThaiFixesConfigManager.config = new Configuration(file);
        ThaiFixesConfigManager.syncConfig(true);
    }

    public static void syncConfig(boolean load)
    {
        if (!ThaiFixesConfigManager.config.isChild)
        {
            if (load)
            {
                ThaiFixesConfigManager.config.load();
            }
        }

        ThaiFixesConfigManager.config.setCategoryPropertyOrder("main", ThaiFixesConfigManager.addMainSetting());

        if (ThaiFixesConfigManager.config.hasChanged())
        {
            ThaiFixesConfigManager.config.save();
        }
    }

    private static List<String> addMainSetting()
    {
        List<String> propOrder = Lists.newArrayList();

        Property prop = ThaiFixesConfigManager.config.get("main", "Font Renderer Type", "unicode");
        prop.setValidValues(new String[] {"unicode", "mcpx"});
        ThaiFixesConfigManager.fontRendererType = prop.getString();
        propOrder.add(prop.getName());
        return propOrder;
    }

    public static List<IConfigElement> getConfigElements()
    {
        List<IConfigElement> list = Lists.newArrayList();
        list.add(new ConfigElement(ThaiFixesConfigManager.config.getCategory("main")));
        return list;
    }
}