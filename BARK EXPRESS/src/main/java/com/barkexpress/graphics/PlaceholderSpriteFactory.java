package com.barkexpress.graphics;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.texture.Image;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;
import com.jme3.texture.plugins.AWTLoader;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * Generates procedural pixel-art textures with Nearest-Neighbor filtering.
 * Enforces character design rules: Bulldog wears a helmet, NO orange jacket or uniform.
 */
public final class PlaceholderSpriteFactory {

    private static final AWTLoader AWT_LOADER = new AWTLoader();

    private PlaceholderSpriteFactory() {
    }

    public static Material createPixelSpriteMaterial(AssetManager assetManager, Texture2D texture) {
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setTexture("ColorMap", texture);
        mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        mat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
        mat.setFloat("AlphaDiscardThreshold", 0.1f);
        return mat;
    }

    /**
     * Creates a 32x32 pixel-art rear/3-quarter sprite of the bulldog on a scooter.
     * Includes protective helmet; strictly excludes delivery uniform or orange jacket.
     */
    public static Texture2D createBulldogScooterTexture() {
        int size = 32;
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();

        // Transparent background
        g.setColor(new Color(0, 0, 0, 0));
        g.fillRect(0, 0, size, size);

        // Scooter rear wheel (dark rubber)
        g.setColor(new Color(35, 35, 40));
        g.fillRect(14, 24, 4, 7);

        // Scooter chassis / rear fender (teal/mint retro scooter)
        g.setColor(new Color(64, 180, 166));
        g.fillRect(11, 19, 10, 6);
        g.setColor(new Color(45, 135, 125));
        g.fillRect(12, 22, 8, 3);

        // Tail light (red)
        g.setColor(new Color(230, 55, 55));
        g.fillRect(15, 20, 2, 2);

        // Bulldog torso (natural tan/fawn fur - NO uniform, NO orange jacket)
        g.setColor(new Color(198, 148, 98));
        g.fillRect(11, 12, 10, 8);
        // Lighter fur patch on back/shoulders
        g.setColor(new Color(224, 182, 134));
        g.fillRect(13, 13, 6, 5);

        // Bulldog paws holding handlebars
        g.setColor(new Color(180, 130, 82));
        g.fillRect(9, 13, 2, 3);
        g.fillRect(21, 13, 2, 3);

        // Scooter handlebars & mirrors
        g.setColor(new Color(90, 95, 105));
        g.fillRect(7, 12, 18, 2);
        g.fillRect(7, 10, 2, 2);
        g.fillRect(23, 10, 2, 2);

        // Bulldog head & jowls
        g.setColor(new Color(198, 148, 98));
        g.fillRect(11, 6, 10, 6);

        // Protective Helmet (cream/white with dark visor strap & blue racing stripe)
        g.setColor(new Color(242, 242, 245));
        g.fillRect(10, 2, 12, 6);
        g.fillRect(11, 1, 10, 1);
        // Helmet center stripe (navy blue)
        g.setColor(new Color(40, 75, 130));
        g.fillRect(15, 1, 2, 7);
        // Helmet rim
        g.setColor(new Color(70, 75, 85));
        g.fillRect(10, 7, 12, 1);

        g.dispose();
        return toPixelTexture(img, false);
    }

    /**
     * Creates a 32x32 repeating asphalt road tile with dashed center line and sidewalks.
     */
    public static Texture2D createRoadTileTexture() {
        int size = 32;
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();

        // Sidewalks on left and right edges
        g.setColor(new Color(185, 182, 175));
        g.fillRect(0, 0, 4, size);
        g.fillRect(28, 0, 4, size);

        // Curb shadow
        g.setColor(new Color(130, 128, 122));
        g.fillRect(4, 0, 1, size);
        g.fillRect(27, 0, 1, size);

        // Asphalt surface
        g.setColor(new Color(62, 66, 74));
        g.fillRect(5, 0, 22, size);

        // Subtle pixel asphalt grain
        g.setColor(new Color(52, 56, 63));
        g.fillRect(8, 5, 2, 2);
        g.fillRect(21, 14, 2, 2);
        g.fillRect(10, 24, 2, 2);
        g.fillRect(22, 27, 2, 2);

        // Dashed yellow center divider
        g.setColor(new Color(240, 195, 65));
        g.fillRect(15, 2, 2, 10);
        g.fillRect(15, 18, 2, 10);

        g.dispose();
        return toPixelTexture(img, true);
    }

    /**
     * Creates a 32x32 pixel-art suburban house or storefront facade.
     */
    public static Texture2D createBuildingTexture(Color wallColor, Color roofColor, boolean isGroceryStore) {
        int size = 32;
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();

        // Transparent sky around roof peak
        g.setColor(new Color(0, 0, 0, 0));
        g.fillRect(0, 0, size, size);

        // Roof
        g.setColor(roofColor);
        g.fillRect(2, 4, 28, 8);
        g.fillRect(5, 2, 22, 2);

        // Main wall
        g.setColor(wallColor);
        g.fillRect(4, 12, 24, 20);

        // Door
        g.setColor(new Color(110, 68, 42));
        g.fillRect(14, 21, 5, 11);

        // Windows
        g.setColor(new Color(165, 220, 245));
        g.fillRect(7, 15, 5, 5);
        g.fillRect(20, 15, 5, 5);

        // Window frames
        g.setColor(new Color(245, 245, 245));
        g.drawRect(7, 15, 4, 4);
        g.drawRect(20, 15, 4, 4);

        // Simple green awning sign if it's the grocery store placeholder
        if (isGroceryStore) {
            g.setColor(new Color(55, 155, 85));
            g.fillRect(5, 11, 22, 3);
        }

        g.dispose();
        return toPixelTexture(img, false);
    }

    private static Texture2D toPixelTexture(BufferedImage bufferedImage, boolean repeat) {
        Image jmeImage = AWT_LOADER.load(bufferedImage, true);
        Texture2D texture = new Texture2D(jmeImage);
        // CRITICAL: Nearest-neighbor filtering keeps pixel edges razor-sharp
        texture.setMagFilter(Texture.MagFilter.Nearest);
        texture.setMinFilter(Texture.MinFilter.NearestNoMipMaps);
        if (repeat) {
            texture.setWrap(Texture.WrapMode.Repeat);
        } else {
            texture.setWrap(Texture.WrapMode.Clamp);
        }
        return texture;
    }
}