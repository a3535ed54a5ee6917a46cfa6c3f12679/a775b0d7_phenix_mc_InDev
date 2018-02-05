package fr.phenixmc.client.shield;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AntiXray
{
    static boolean lostingamefocus = true;
    static List<String> allow = new ArrayList();
    private static final Logger logger = LogManager.getLogger();

    static void init()
    {
        if (allow.size() == 0)
        {
            allow.add("textures/blocks/gravel.png");
            allow.add("textures/blocks/sand.png");
            allow.add("textures/blocks/leaves_jungle_opaque.png");
            allow.add("textures/blocks/leaves_oak_opaque.png");
            allow.add("textures/blocks/cobblestone.png");
            allow.add("textures/blocks/stone.png");
            allow.add("textures/blocks/obsidian.png");
            allow.add("textures/blocks/grass_top.png");
            allow.add("textures/blocks/lava.png");
        }
    }

    private static BufferedImage readTextureImage(InputStream par1InputStream) throws IOException
    {
        BufferedImage var2 = ImageIO.read(par1InputStream);
        par1InputStream.close();
        return var2;
    }

    public static boolean checkTexturePack()
    {
        IResourceManager par1ResourceManager = Minecraft.getMinecraft().getResourceManager();
        init();

        for (int i = 0; i < allow.size(); ++i)
        {
            String filename = (String)allow.get(i);

            try
            {
                IResource e = par1ResourceManager.getResource(new ResourceLocation(filename));
                InputStream var7 = e.getInputStream();
                BufferedImage var9 = ImageIO.read(var7);
                int pixnb = countTransparentPixels(var9);
                logger.info("Check texture " + i + " transparency : " + filename + " = " + pixnb);

                if (pixnb > 0)
                {
                    return false;
                }
            }
            catch (IOException var71)
            {
                logger.info("Check texture " + i + " transparency : " + filename + " not found");
            }
        }

        return true;
    }

    private static int countTransparentPixels(BufferedImage par1BufferedImage)
    {
        int pixnb = 0;
        int w = par1BufferedImage.getWidth();
        int h = par1BufferedImage.getHeight();

        for (int x = 0; x < w; ++x)
        {
            for (int y = 0; y < h; ++y)
            {
                int rgb = par1BufferedImage.getRGB(x, y);
                int alpha = rgb >> 24 & 255;

                if (alpha != 255)
                {
                    ++pixnb;
                }
            }
        }

        return pixnb;
    }
}
