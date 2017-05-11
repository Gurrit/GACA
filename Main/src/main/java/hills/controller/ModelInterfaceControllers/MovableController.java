package hills.controller.ModelInterfaceControllers;

import hills.controller.InputControllers.InputLocator;
import hills.controller.InputControllers.KeyboardListener;
import hills.controller.InputControllers.MouseListener;
import hills.controller.ServiceMediator;
import hills.model.IMovable;
import hills.model.PlayerMovable;
import hills.services.ServiceLocator;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gustav on 2017-05-06.
 */
public class MovableController implements KeyboardListener, MouseListener{
    private PlayerMovable player;
    private List<IMovable> movableList = new ArrayList<>();

    public MovableController(){
        InputLocator.INSTANCE.subscribeToKeyboard(this);
        InputLocator.INSTANCE.subscribeToMouse(this);
    }
    public void addAIMovable(IMovable movable){
        movableList.add(movable);
    }
    public void setPlayer(PlayerMovable movable){
        player = movable;
    }
    public void updateMovables(float delta, double runtime){
        player.updateMovable(delta);
        float h = ServiceMediator.INSTANCE.getHeight(player.get3DPos().getX(), player.get3DPos().getZ());
        player.setHeight(h);
        for(IMovable movable : movableList){
            movable.updateMovable(delta);
            if(runtime % 1000 == 0){

            }
        }
        ServiceLocator.INSTANCE.getCameraDataService().setPosition(player.get3DPos());
        ServiceLocator.INSTANCE.getCameraDataService().setOrientation
                (player.getRightVector(), player.getUpVector(), player.getForwardVector(), false);
        // Send updates to all saved objects.
    }

    @Override
    public void mouseMoved(float xVelocity, float yVelocity) {
        player.updateYaw(xVelocity*-0.3f);
        player.updatePitch(yVelocity*-0.3f);
    }

    @Override
    public void KeyPressed(int key, int mods) {
        setDirection(key, mods, true);
    }

    @Override
    public void keyReleased(int key, int mods) {
        setDirection(key, mods, false);
    }
    private void setDirection(int key, int mods, boolean pressed){
        switch (key){
            case GLFW.GLFW_KEY_W:
                if(mods == GLFW.GLFW_MOD_SHIFT){
                    player.addVelocity(PlayerMovable.Direction.FORWARD_SPRINT, pressed);
                    break;
                }
                player.addVelocity(PlayerMovable.Direction.FORWARD, pressed);       // Forward Velocity
                break;
            case GLFW.GLFW_KEY_A:
                player.addVelocity(PlayerMovable.Direction.LEFT, pressed);       // RightVelocity
                break;
            case GLFW.GLFW_KEY_S:
                player.addVelocity(PlayerMovable.Direction.BACK, pressed);       //Backward Velocity
                break;
            case GLFW.GLFW_KEY_D:
                player.addVelocity(PlayerMovable.Direction.RIGHT, pressed);       // Left Velocity
                break;
        }
    }
}
