package com.barkexpress.entities;

import com.barkexpress.graphics.PlaceholderSpriteFactory;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture2D;

/**
 * Represents the bulldog (with helmet, no uniform) riding the delivery scooter.
 */
public class ScooterPlayer {

    private final Node playerNode;
    private final Geometry spriteGeometry;

    private float forwardSpeed = 0f;
    private float lateralVelocity = 0f;

    private static final float MAX_FORWARD_SPEED = 18.0f;
    private static final float MAX_REVERSE_SPEED = -6.0f;
    private static final float ACCELERATION = 24.0f;
    private static final float BRAKE_DECELERATION = 32.0f;
    private static final float FRICTION = 12.0f;
    private static final float TURN_SPEED = 11.0f;
    private static final float ROAD_HALF_WIDTH = 4.2f;

    public ScooterPlayer(AssetManager assetManager) {
        playerNode = new Node("BulldogScooterNode");

        float spriteWidth = 1.6f;
        float spriteHeight = 1.6f;
        Quad quad = new Quad(spriteWidth, spriteHeight);

        Texture2D texture = PlaceholderSpriteFactory.createBulldogScooterTexture();
        Material material = PlaceholderSpriteFactory.createPixelSpriteMaterial(assetManager, texture);

        spriteGeometry = new Geometry("BulldogScooterSprite", quad);
        spriteGeometry.setMaterial(material);
        // Center horizontally on X, pivot at bottom wheels on Y = 0
        spriteGeometry.setLocalTranslation(-spriteWidth / 2f, 0.02f, 0f);

        playerNode.attachChild(spriteGeometry);
    }

    public void update(float tpf, boolean forward, boolean backward, boolean left, boolean right) {
        // 1. Forward / Backward acceleration (along -Z is forward into the scene)
        if (forward) {
            forwardSpeed += ACCELERATION * tpf;
        } else if (backward) {
            forwardSpeed -= BRAKE_DECELERATION * tpf;
        } else {
            // Natural rolling friction
            if (forwardSpeed > 0f) {
                forwardSpeed = Math.max(0f, forwardSpeed - FRICTION * tpf);
            } else if (forwardSpeed < 0f) {
                forwardSpeed = Math.min(0f, forwardSpeed + FRICTION * tpf);
            }
        }
        forwardSpeed = FastMath.clamp(forwardSpeed, MAX_REVERSE_SPEED, MAX_FORWARD_SPEED);

        // 2. Steering / Lateral movement along X
        float steerInput = 0f;
        if (left) {
            steerInput -= 1f;
        }
        if (right) {
            steerInput += 1f;
        }

        // Allow responsive lane positioning even at low speeds
        float speedFactor = FastMath.clamp(Math.abs(forwardSpeed) / 6.0f, 0.35f, 1.0f);
        lateralVelocity = steerInput * TURN_SPEED * speedFactor;

        // 3. Apply translation
        Vector3f pos = playerNode.getLocalTranslation();
        float newX = FastMath.clamp(pos.x + lateralVelocity * tpf, -ROAD_HALF_WIDTH, ROAD_HALF_WIDTH);
        float newZ = pos.z - forwardSpeed * tpf; // -Z moves forward down the street
        playerNode.setLocalTranslation(newX, 0f, newZ);

        // 4. Subtle pixel-sprite tilt when turning for tactile scooter feedback
        float targetRoll = -steerInput * 0.12f * speedFactor;
        Quaternion roll = new Quaternion().fromAngleAxis(targetRoll, Vector3f.UNIT_Z);
        spriteGeometry.setLocalRotation(roll);
    }

    public Node getNode() {
        return playerNode;
    }

    public Vector3f getWorldPosition() {
        return playerNode.getWorldTranslation();
    }

    public float getForwardSpeed() {
        return forwardSpeed;
    }
}