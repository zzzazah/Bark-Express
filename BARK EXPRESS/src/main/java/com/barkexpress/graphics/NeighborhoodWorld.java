package com.barkexpress.world;

import com.barkexpress.graphics.PlaceholderSpriteFactory;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture2D;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages the pixel-art suburban street, grass lawn, and roadside buildings.
 * Recycles road segments and buildings ahead of the player for endless Day 1 driving.
 */
public class NeighborhoodWorld {

    private final Node worldNode;
    private final List<Geometry> roadSegments = new ArrayList<>();
    private final List<Node> buildingProps = new ArrayList<>();

    private static final float ROAD_WIDTH = 12f;
    private static final float SEGMENT_LENGTH = 12f;
    private static final int SEGMENT_COUNT = 14;

    public NeighborhoodWorld(AssetManager assetManager) {
        worldNode = new Node("NeighborhoodWorld");

        buildGrassGround(assetManager);
        buildRoadSegments(assetManager);
        buildRoadsideBuildings(assetManager);
    }

    private void buildGrassGround(AssetManager assetManager) {
        Quad grassQuad = new Quad(300f, 400f);
        Material grassMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        grassMat.setColor("Color", new ColorRGBA(0.36f, 0.62f, 0.30f, 1.0f));

        Geometry grassGeo = new Geometry("SuburbanGrass", grassQuad);
        grassGeo.setMaterial(grassMat);
        // Rotate flat onto XZ plane
        grassGeo.setLocalRotation(new Quaternion().fromAngleAxis(-FastMath.HALF_PI, Vector3f.UNIT_X));
        grassGeo.setLocalTranslation(-150f, -0.02f, 100f);
        worldNode.attachChild(grassGeo);
    }

    private void buildRoadSegments(AssetManager assetManager) {
        Texture2D roadTex = PlaceholderSpriteFactory.createRoadTileTexture();
        Material roadMat = PlaceholderSpriteFactory.createPixelSpriteMaterial(assetManager, roadTex);

        for (int i = 0; i < SEGMENT_COUNT; i++) {
            Quad roadQuad = new Quad(ROAD_WIDTH, SEGMENT_LENGTH);
            roadQuad.scaleTextureCoordinates(new Vector2f(1f, 1f));

            Geometry segment = new Geometry("RoadSegment_" + i, roadQuad);
            segment.setMaterial(roadMat);
            segment.setLocalRotation(new Quaternion().fromAngleAxis(-FastMath.HALF_PI, Vector3f.UNIT_X));

            float zPos = 12f - (i * SEGMENT_LENGTH);
            segment.setLocalTranslation(-ROAD_WIDTH / 2f, 0f, zPos);

            roadSegments.add(segment);
            worldNode.attachChild(segment);
        }
    }

    private void buildRoadsideBuildings(AssetManager assetManager) {
        Texture2D houseBlueTex = PlaceholderSpriteFactory.createBuildingTexture(
                new Color(195, 212, 228), new Color(135, 65, 55), false);
        Texture2D houseCreamTex = PlaceholderSpriteFactory.createBuildingTexture(
                new Color(238, 226, 198), new Color(95, 80, 70), false);
        Texture2D groceryTex = PlaceholderSpriteFactory.createBuildingTexture(
                new Color(242, 242, 236), new Color(55, 130, 80), true);

        Material matBlue = PlaceholderSpriteFactory.createPixelSpriteMaterial(assetManager, houseBlueTex);
        Material matCream = PlaceholderSpriteFactory.createPixelSpriteMaterial(assetManager, houseCreamTex);
        Material matGrocery = PlaceholderSpriteFactory.createPixelSpriteMaterial(assetManager, groceryTex);

        for (int i = 0; i < 16; i++) {
            boolean isLeftSide = (i % 2 == 0);
            float xOffset = isLeftSide ? -9.5f : 9.5f;
            float zOffset = 6f - (i * 10f);

            Material chosenMat;
            if (i == 5) {
                chosenMat = matGrocery; // Placeholder Grocery Store facade on the route
            } else if (i % 3 == 0) {
                chosenMat = matBlue;
            } else {
                chosenMat = matCream;
            }

            float buildingSize = (i == 5) ? 6.5f : 5.2f;
            Quad bQuad = new Quad(buildingSize, buildingSize);
            Geometry bGeo = new Geometry("Building_" + i, bQuad);
            bGeo.setMaterial(chosenMat);
            bGeo.setLocalTranslation(-buildingSize / 2f, 0f, 0f);

            Node propNode = new Node("BuildingProp_" + i);
            propNode.attachChild(bGeo);
            propNode.setLocalTranslation(xOffset, 0f, zOffset);

            buildingProps.add(propNode);
            worldNode.attachChild(propNode);
        }
    }

    /**
     * Scrolls/recycles road tiles and roadside buildings seamlessly as the player drives.
     */
    public void update(float playerZ) {
        float totalSpan = SEGMENT_COUNT * SEGMENT_LENGTH;
        for (Geometry segment : roadSegments) {
            float segZ = segment.getLocalTranslation().z;
            if (segZ - playerZ > SEGMENT_LENGTH * 2f) {
                segment.move(0f, 0f, -totalSpan);
            } else if (playerZ - segZ > totalSpan - SEGMENT_LENGTH * 2f) {
                segment.move(0f, 0f, totalSpan);
            }
        }

        float buildingSpan = 160f;
        for (Node building : buildingProps) {
            float bZ = building.getLocalTranslation().z;
            if (bZ - playerZ > 20f) {
                building.move(0f, 0f, -buildingSpan);
            } else if (playerZ - bZ > buildingSpan - 20f) {
                building.move(0f, 0f, buildingSpan);
            }
        }
    }

    public Node getNode() {
        return worldNode;
    }
}