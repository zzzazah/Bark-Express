package com.barkexpress.app;

import com.barkexpress.entities.ScooterPlayer;
import com.barkexpress.input.ScooterInputController;
import com.barkexpress.world.NeighborhoodWorld;
import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.system.AppSettings;

/**
 * BARK EXPRESS - Day 1 Outdoor Scooter Prototype
 * Engine: jMonkeyEngine 3.9.0-stable | Java 21
 */
public class BarkExpressApp extends SimpleApplication {

    private ScooterPlayer player;
    private ScooterInputController inputController;
    private NeighborhoodWorld world;
    private BitmapText hudStatusText;

    // Third-person camera offset behind and above the bulldog scooter
    private static final Vector3f CAMERA_OFFSET = new Vector3f(0f, 4.2f, 7.8f);
    private static final float CAMERA_LOOK_AHEAD_Z = -4.5f;
    private static final float CAMERA_LERP_SPEED = 8.0f;

    public static void main(String[] args) {
        BarkExpressApp app = new BarkExpressApp();
        AppSettings settings = new AppSettings(true);
        settings.setTitle("BARK EXPRESS - Day 1 Outdoor Prototype");
        settings.setResolution(1280, 720);
        settings.setVSync(true);
        app.setSettings(settings);
        app.setShowSettings(false);
        app.start();
    }

    @Override
    public void simpleInitApp() {
        // Disable default FlyByCamera so our third-person scooter camera has full control
        flyCam.setEnabled(false);

        // Crisp daytime sky backdrop
        viewPort.setBackgroundColor(new ColorRGBA(0.53f, 0.81f, 0.96f, 1.0f));

        // Initialize world, player, and WASD input
        world = new NeighborhoodWorld(assetManager);
        rootNode.attachChild(world.getNode());

        player = new ScooterPlayer(assetManager);
        rootNode.attachChild(player.getNode());

        inputController = new ScooterInputController();
        inputController.registerInputs(inputManager);

        // Snap camera to initial behind-scooter position
        Vector3f startPos = player.getWorldPosition();
        cam.setLocation(startPos.add(CAMERA_OFFSET));
        cam.lookAt(startPos.add(0f, 1.0f, CAMERA_LOOK_AHEAD_Z), Vector3f.UNIT_Y);

        initMinimalHud();
    }

    private void initMinimalHud() {
        setDisplayStatView(false);
        setDisplayFps(true);

        hudStatusText = new BitmapText(guiFont);
        hudStatusText.setSize(guiFont.getCharSet().getRenderedSize());
        hudStatusText.setColor(ColorRGBA.White);
        hudStatusText.setText("BARK EXPRESS [Day 1 Prototype] | Controls: W/S Accelerate & Brake, A/D Steer");
        hudStatusText.setLocalTranslation(16f, cam.getHeight() - 16f, 0f);
        guiNode.attachChild(hudStatusText);
    }

    @Override
    public void simpleUpdate(float tpf) {
        // 1. Update bulldog scooter physics & movement
        player.update(
                tpf,
                inputController.isForwardPressed(),
                inputController.isBackwardPressed(),
                inputController.isLeftPressed(),
                inputController.isRightPressed()
        );

        Vector3f playerPos = player.getWorldPosition();

        // 2. Scroll/recycle suburban street and buildings
        world.update(playerPos.z);

        // 3. Smooth third-person trailing camera
        Vector3f desiredCamPos = new Vector3f(
                playerPos.x * 0.65f, // Subtle horizontal tracking so lane changes feel dynamic
                CAMERA_OFFSET.y,
                playerPos.z + CAMERA_OFFSET.z
        );
        cam.getLocation().interpolateLocal(desiredCamPos, FastMath.clamp(CAMERA_LERP_SPEED * tpf, 0f, 1f));
        cam.lookAt(new Vector3f(playerPos.x * 0.5f, 1.0f, playerPos.z + CAMERA_LOOK_AHEAD_Z), Vector3f.UNIT_Y);
    }
}