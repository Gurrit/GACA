package hills.Anton.engine.system.camera;

import org.lwjgl.glfw.GLFW;

import hills.Anton.engine.display.Display;
import hills.Anton.engine.input.Keyboard;
import hills.Anton.engine.input.Mouse;
import hills.Anton.engine.math.Mat4;
import hills.Anton.engine.math.Quaternion;
import hills.Anton.engine.math.Vec3;
import hills.Anton.engine.renderer.shader.ShaderProgram;
import hills.Anton.engine.system.camera.CameraSystem;
import hills.Anton.engine.system.EngineSystem;

public class CameraSystem extends EngineSystem {

	private static CameraSystem instance = null;
	
	private enum Direction {
		FORWARD(1.0f), BACKWARD(-1.0f), LEFT(-1.0f), RIGHT(1.0f), NONE(0.0f);
		
		private final float multiplier;
		private Direction(float multiplier){
			this.multiplier = multiplier;
		}
	}
	
	/**
	 * Going forwards/backwards.
	 */
	private Direction medial = Direction.NONE;
	
	/**
	 * Going left/right.
	 */
	private Direction lateral = Direction.NONE;
	
	/**
	 * Forwards/Backwards speed.
	 */
	private float medialSpeed = 10.0f;
	
	/**
	 * Left/Right speed.
	 */
	private float lateralSpeed = 10.0f;
	
	/**
	 * Camera world position.
	 */
	private Vec3 position;
	
	/**
	 * Camera forward direction.
	 */
	private Vec3 forward;
	
	/**
	 * Camera up direction.
	 */
	private Vec3 up;
	
	/**
	 * Camera right direction.
	 */
	private Vec3 right;
	
	/**
	 * To update camera or not (No need to update when nothing has changed).
	 */
	private boolean toUpdate;
	
	private CameraSystem() {
		super(1.0f, false, 0.0f);
		
		// Initialize camera at 0, 0, 0. Right is +X, Up is +Y, Depth is -Z.
		this.position = new Vec3(0.0f, 0.0f, 0.0f);
		this.forward = new Vec3(0.0f, 0.0f, -1.0f);
		this.up = new Vec3(0.0f, 1.0f, 0.0f);
		this.right = new Vec3(1.0f, 0.0f, 0.0f);
		
		Mat4 c = new Mat4();
		ShaderProgram.map("VIEW", "CAMERA", c.get140Data());
		ShaderProgram.map("VIEW", "CAMPOSWORLD", position.getData());
		
		toUpdate = false;
	}
	
	protected void update(double delta) {
		// Check input
		input();
		
		// If nothing has changed don't update!
		if(!toUpdate)
			return;
		
		// Move camera
		position = position.add(forward.mul(medialSpeed * medial.multiplier * (float) delta));
		position = position.add(right.mul(lateralSpeed * lateral.multiplier * (float) delta));
		medial = Direction.NONE;
		lateral = Direction.NONE;
		
		// Construct camera matrix according to position, and direction vectors.
		Mat4 cameraMatrix = Mat4.look(position, forward, up, right, true);
		
		// Map camera matrix to uniform buffer VIEW
		ShaderProgram.map("VIEW", "CAMERA", cameraMatrix.get140Data());
		
		// Map camera position to uniform buffer VIEW
		ShaderProgram.map("VIEW", "CAMPOSWORLD", position.getData());
		
		// Set toUpdate false
		toUpdate = false;
	}
	
	/**
	 * Move camera according to input.
	 */
	private void input(){
		if(Keyboard.isDown(GLFW.GLFW_KEY_W))
			setDirection(Direction.FORWARD);
		
		if(Keyboard.isDown(GLFW.GLFW_KEY_S))
			setDirection(Direction.BACKWARD);
		
		if(Keyboard.isDown(GLFW.GLFW_KEY_A))
			setDirection(Direction.LEFT);
		
		if(Keyboard.isDown(GLFW.GLFW_KEY_D))
			setDirection(Direction.RIGHT);
		
		if(Keyboard.isPressed(GLFW.GLFW_KEY_LEFT_SHIFT)){
			medialSpeed *= 2.0f;
			lateralSpeed *= 2.0f;
		}
		
		if(Keyboard.isPressed(GLFW.GLFW_KEY_LEFT_CONTROL)){
			medialSpeed *= 0.5f;
			lateralSpeed *= 0.5f;
		}
		
		if(Keyboard.isPressed(GLFW.GLFW_KEY_SPACE))
			if(Display.isMouseCaptured())
				Display.captureMouse(false);
			else
				Display.captureMouse(true);
		
		if(Display.isMouseCaptured()){
			float yaw = -Mouse.getXVelocity() * 0.3f;
			float pitch = -Mouse.getYVelocity() * 0.3f;
			
			if(yaw != 0.0f)
				rotate(yaw, 0.0f, 1.0f, 0.0f); // ROTATE AROUND WORLD UP-AXIS
			
			if(pitch != 0.0f)
				pitch(pitch);
		}
	}
	
	/**
	 * Rotate camera along cameras right axis.
	 * @param degrees - Degrees to rotate.
	 */
	public void pitch(float degrees){
		rotate(degrees, right);
	}
	
	/**
	 * Rotate camera along cameras up axis.
	 * @param degrees - Degrees to rotate.
	 */
	public void yaw(float degrees){
		rotate(degrees, up);
	}
	
	/**
	 * Rotate camera along cameras forward axis.
	 * @param degrees - Degrees to rotate.
	 */
	public void roll(float degrees){
		rotate(degrees, forward);
	}
	
	/**
	 * Rotate camera along axis x, y, z.
	 * @param angle - degrees to rotate.
	 * @param x - X component of axis to rotate around.
	 * @param y - Y component of axis to rotate around.
	 * @param z - Z component of axis to rotate around.
	 */
	public void rotate(float angle, float x, float y, float z){
		rotate(angle, new Vec3(x, y, z));
	}
	
	private void rotate(float angle, Vec3 axis){
		Quaternion rotQuat = new Quaternion(axis, angle);
		
		forward = rotQuat.mul(forward).normalize();
		up = rotQuat.mul(up).normalize();
		right = forward.cross(up);
		
		toUpdate = true;
	}
	
	/**
	 * Set direction for camera to move in.
	 * @param dir - Direction to move in.
	 * If set to NONE camera will stop.
	 */
	public void setDirection(Direction dir){
		switch(dir){
		case FORWARD:
			medial = Direction.FORWARD;
			break;
		case BACKWARD:
			medial = Direction.BACKWARD;
			break;
		case LEFT:
			lateral = Direction.LEFT;
			break;
		case RIGHT:
			lateral = Direction.RIGHT;
			break;
		case NONE:
			medial = Direction.NONE;
			lateral = Direction.NONE;
			break;
		}
		
		toUpdate = true;
	}
	
	public void render() {}
	
	public Vec3 getPosition(){
		return position;
	}
	
	public void cleanUp() {
		System.out.println("Camera system cleaned up!");
	}
	
	/**
	 * Creates the singleton instance of CameraSystem.
	 * @return False if an instance has already been created.
	 */
	public static boolean createInstance() {
		if(instance != null)
			return false;
		
		instance = new CameraSystem();
		return true;
	}
	
	/**
	 * 
	 * @return The singleton instance of CameraSystem.
	 * @throws NullPointerException If singleton instance has not been created.
	 */
	public static CameraSystem getInstance() throws NullPointerException {
		if(instance == null)
			throw new NullPointerException("Singleton instance not created!");
		
		return instance;
	}

}