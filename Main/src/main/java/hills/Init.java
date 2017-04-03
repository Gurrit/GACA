package hills;

import hills.Gurra.Terrain;
import hills.engine.GameLoop;
import hills.engine.display.AspectRatios;
import hills.engine.display.Display;
import hills.engine.display.FrameBuffer;
import hills.engine.system.camera.CameraSystem;
import hills.engine.system.debug.DebugSystem;
import hills.engine.system.domainModel.World;
import hills.engine.system.game.GameSystem;
import hills.engine.system.terrain.TerrainSystem;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWWindowCloseCallback;
import org.lwjgl.opengl.GL11;

import java.util.Random;

/**
 * Created by gustav on 2017-03-21.
 */
public class Init {
	
	/**
	 * Window title.
	 */
	private static final String TITLE = "Hills";
	
	/**
	 * Window initial width.
	 */
	private static final int WIDTH = 720;
	
	/**
	 * Window initial height.
	 */
	private static final int HEIGHT = (int) (WIDTH * AspectRatios.SIXTEEN_TO_NINE);
	
	// Absolute first thing that gets done. (Hacky?)
	// Initialize GLFW, OpenGL and create a new window.
	// This allows OpenGL calls when initializing static final variables such as shader programs.
	static {
		System.setProperty("org.lwjgl.util.Debug", "true");
		System.setProperty("org.lwjgl.util.DebugAllocator", "true");
		
		Display.setErrorCallback(GLFWErrorCallback.createPrint(System.err));
		Display.create(WIDTH, HEIGHT, TITLE);
	}
	
	public void init(){
		//TerrainNormalMapCreator.createSmoothNormals("height_map_test_3.png");
		Random rand = new Random();
		Terrain noise = new Terrain(rand.nextLong());
		World w = new World(noise.createfinalIsland());

		//TerrainNormalMapCreator.createFlatNormals("height_map_test_3.png");

		FrameBuffer.setClearColor(0.55f, 0.55f, 1.0f, 1.0f);	// Set clear color
		FrameBuffer.enableDepthTesting(0.0f, 1.0f);				// Enable depth testing
		FrameBuffer.setClearDepth(1.0f);						// Clear depth buffer to 1.0
		
		FrameBuffer.setDepthFunction(GL11.GL_LEQUAL);		// Set OpenGL depth function.
		
		DebugSystem.createInstance();						// Create DebugSystem instance
		DebugSystem.getInstance().setFPSDebugMode(true);	// Activate FPS debug mode
		
		CameraSystem.createInstance(1.0f, false, 0.0f);																	// Create CameraSystem instance
		CameraSystem cameraSystem = CameraSystem.getInstance(); 														// Get the CameraSystem instance
		cameraSystem.updatePerspective(0.1f, 3000.0f, (float) Display.getWidth() / (float) Display.getHeight(), 70.0f);	// Update the perspective matrix
		initDisplayCallbacks();
		
		TerrainSystem.createInstance();						// Create TerrainSystem instance
		
		GameSystem.createInstance(1.0f, false, 0.0f);		// Create GameSystem instance
		
		GameLoop.start();                            		// Start engine game loop
		
		Display.destroy();                           		// Terminate GLFW window and GLFW when program ends
	}
	
	/**
	 * Initialize GLFW callback methods.
	 */
	public void initDisplayCallbacks(){
		CameraSystem cameraSystem = CameraSystem.getInstance();
		
		// Window close callback
		Display.setWindowCloseCallback(new GLFWWindowCloseCallback(){
			public void invoke(long window) {
				GameLoop.stop();
			}
		});
		
		// Frame buffer resize callback
		Display.setFramebufferSizeCallback(new GLFWFramebufferSizeCallback(){
			public void invoke(long window, int width, int height) {
				GL11.glViewport(0, 0, width, height);
				
				// Set perspective matrix according to new width and height
				cameraSystem.updatePerspective(0.1f, 3000.0f, (float) Display.getWidth() / (float) Display.getHeight(), 70.0f);
			}
		});
	}
	
}
