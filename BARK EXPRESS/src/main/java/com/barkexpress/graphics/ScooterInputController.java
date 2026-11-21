package com.barkexpress.input;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;

/**
 * Maps WASD keys to scooter movement states.
 */
public class ScooterInputController implements ActionListener {

    private static final String MOVE_FORWARD = "SCOOTER_FORWARD";
    private static final String MOVE_BACKWARD = "SCOOTER_BACKWARD";
    private static final String TURN_LEFT = "SCOOTER_LEFT";
    private static final String TURN_RIGHT = "SCOOTER_RIGHT";

    private boolean forwardPressed;
    private boolean backwardPressed;
    private boolean leftPressed;
    private boolean rightPressed;

    public void registerInputs(InputManager inputManager) {
        inputManager.addMapping(MOVE_FORWARD, new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping(MOVE_BACKWARD, new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping(TURN_LEFT, new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping(TURN_RIGHT, new KeyTrigger(KeyInput.KEY_D));

        inputManager.addListener(this, MOVE_FORWARD, MOVE_BACKWARD, TURN_LEFT, TURN_RIGHT);
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        switch (name) {
            case MOVE_FORWARD -> forwardPressed = isPressed;
            case MOVE_BACKWARD -> backwardPressed = isPressed;
            case TURN_LEFT -> leftPressed = isPressed;
            case TURN_RIGHT -> rightPressed = isPressed;
            default -> {
            }
        }
    }

    public boolean isForwardPressed() {
        return forwardPressed;
    }

    public boolean isBackwardPressed() {
        return backwardPressed;
    }

    public boolean isLeftPressed() {
        return leftPressed;
    }

    public boolean isRightPressed() {
        return rightPressed;
    }
}