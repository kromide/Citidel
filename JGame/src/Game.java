import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.util.glu.GLU;

public class Game {

	private int VBOVertexHandle;
	private int VBOColorHandle;
	public static GameState CurrentState = GameState.State_Menu;
	public static Chunk chunky;

	Player camera = new Player(1,-6,1);
	 
    float dx        = 0.0f;
    float dy        = 0.0f;
    float dt        = 0.0f; //length of frame
    float lastTime  = 0.0f; // when the last frame was
    float time      = 0.0f;
 
    float mouseSensitivity = 0.05f;
    float movementSpeed = 10.0f; //move 10 units per second
    Float jumpHeight = 2.0f;//jump one block high
 
 
	
	
	
	public void Start() {
		try {
			CreateWindow();
			InitGL();
			   //hide the mouse
		   Mouse.setGrabbed(true);

			Run();
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	DisplayMode displayMode;

	private void CreateWindow() throws Exception {
		Display.setFullscreen(false);
		DisplayMode d[] = Display.getAvailableDisplayModes();
		for (int i = 0; i < d.length; i++) {
			if (d[i].getWidth() == 640 && d[i].getHeight() == 480
					&& d[i].getBitsPerPixel() == 32) {
				displayMode = d[i];
				break;
			}
		}
		Display.setDisplayMode(displayMode);
		Display.setTitle("citidel");
		Display.create();
	}

	private void InitGL() {
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		GL11.glClearDepth(1.0);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);

		GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);

		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();

		GLU.gluPerspective(90.0f, (float) displayMode.getWidth()
				/ (float) displayMode.getHeight(), 0.1f, 300.0f);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);

		GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
	}

	private void Run() {
		chunky = new Chunk(0, 0, 0);
		// CreateVBO();
		while (!Display.isCloseRequested()) {
			try {
				 if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))//move forward
			        {
			            break;
			        }
				ProcessInput();
				GL11.glClear(GL11.GL_COLOR_BUFFER_BIT
						| GL11.GL_DEPTH_BUFFER_BIT);
				GL11.glLoadIdentity();

				
				camera.lookThrough();
				
				chunky.Render();
				// Render();
				
				Display.update();
				Display.sync(60);
			} catch (Exception e) {

			}
		}
		Display.destroy();

	}

	private void ProcessInput() {
		time = Sys.getTime();
        dt = (time - lastTime)/1000.0f;
        lastTime = time;
 
        //distance in mouse movement from the last getDX() call.
        dx = Mouse.getDX();
        //distance in mouse movement from the last getDY() call.
        dy = Mouse.getDY();
 
        //controll camera yaw from x movement fromt the mouse
        camera.yaw(dx * mouseSensitivity);
        //controll camera pitch from y movement fromt the mouse
        camera.pitch(dy * mouseSensitivity);
 
        //when passing in the distance to move
        //we times the movementSpeed with dt this is a time scale
        //so if its a slow frame u move more then a fast frame
        //so on a slow computer you move just as fast as on a fast computer
        if (Keyboard.isKeyDown(Keyboard.KEY_W))//move forward
        {
            camera.walkForward(movementSpeed*dt);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_S))//move backwards
        {
            camera.walkBackwards(movementSpeed*dt);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_A))//strafe left
        {
            camera.strafeLeft(movementSpeed*dt);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_D))//strafe right
        {
            camera.strafeRight(movementSpeed*dt);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE))//Jump
        {
        	camera.Jump(jumpHeight*dt);
        }
       
	}

	private void DrawVBO() {
		GL11.glPushMatrix();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBOVertexHandle);
		GL11.glVertexPointer(3, GL11.GL_FLOAT, 0, 0L);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBOColorHandle);
		GL11.glColorPointer(3, GL11.GL_FLOAT, 0, 0L);
		GL11.glDrawArrays(GL11.GL_QUADS, 0, 24);
		GL11.glPopMatrix();
	}

	private void CreateVBO() {
		VBOColorHandle = GL15.glGenBuffers();
		VBOVertexHandle = GL15.glGenBuffers();
		FloatBuffer VertexPositionData = BufferUtils.createFloatBuffer(24 * 3);
		VertexPositionData.put(new float[] { 1.0f, 1.0f, -1.0f, -1.0f, 1.0f,
				-1.0f, -1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f,

				1.0f, -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, -1.0f, -1.0f, -1.0f,
				1.0f, -1.0f, -1.0f,

				1.0f, 1.0f, 1.0f, -1.0f, 1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f,
				-1.0f, 1.0f,

				1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f, 1.0f, -1.0f,
				1.0f, 1.0f, -1.0f,

				-1.0f, 1.0f, 1.0f, -1.0f, 1.0f, -1.0f, -1.0f, -1.0f, -1.0f,
				-1.0f, -1.0f, 1.0f,

				1.0f, 1.0f, -1.0f, 1.0f, 1.0f, 1.0f, 1.0f, -1.0f, 1.0f, 1.0f,
				-1.0f, -1.0f });
		VertexPositionData.flip();
		FloatBuffer VertexColorData = BufferUtils.createFloatBuffer(24 * 3);
		VertexColorData.put(new float[] { 1, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 1,
				1, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 1, 1, 1, 0, 1, 0, 1, 0, 0, 1,
				0, 1, 1, 1, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 1, 1, 1, 0, 1, 0, 1,
				0, 0, 1, 0, 1, 1, 1, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 1, });
		VertexColorData.flip();

		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBOVertexHandle);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, VertexPositionData,
				GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBOColorHandle);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, VertexColorData,
				GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}

	public static void main(String[] args) throws LWJGLException {
		Game r = new Game();
		r.Start();
	}

	public enum GameState {
		State_Menu(0), State_SinglePlayer(1), State_Multiplayer(2), State_Editor(
				3);
		private int StateID;

		GameState(int i) {
			StateID = i;
		}

		public int GetID() {
			return StateID;
		}
	}
}
