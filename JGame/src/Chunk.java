
import java.nio.FloatBuffer;
import java.util.Random;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;



public class Chunk {

	static final int CHUNK_SIZE = 100;
	static final int CUBE_LENGTH = 2;
	private Block[][][] Blocks;
	private int VBOVertexHandle;
	private int VBOColorHandle;
	private int StartX, StartY, StartZ;
	private Random r;
	
	public void Render() {
		GL11.glPushMatrix();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBOVertexHandle);
		GL11.glVertexPointer(3, GL11.GL_FLOAT, 0, 0L);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBOColorHandle);
		GL11.glColorPointer(3, GL11.GL_FLOAT, 0, 0L);
		GL11.glDrawArrays(GL11.GL_QUADS, 0, CHUNK_SIZE * CHUNK_SIZE
				* CHUNK_SIZE * 24);

		GL11.glPopMatrix();
	}

	public void Update() {

	}
	public static void EnableLighting(boolean enabled){
		GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glEnable(GL11.GL_LIGHTING);
	    GL11.glEnable(GL11.GL_LIGHT0);
	    
	}
	public Chunk(int startX, int startY, int startZ) {
		r= new Random();
		Blocks = new Block[CHUNK_SIZE][CHUNK_SIZE][CHUNK_SIZE];
		for (int x = 0; x < CHUNK_SIZE; x++) {
			for (int y = 0; y < CHUNK_SIZE; y++) {
				for (int z = 0; z < CHUNK_SIZE; z++) {
					if(r.nextFloat()>0.7f){
					Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Dirt);
					}else if(r.nextFloat()>0.4f){
						Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Dirt);
					}else if(r.nextFloat()>0.2f){
						Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Water);
					}else{
						Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Default);
					}
				}
			}
		}
		VBOColorHandle = GL15.glGenBuffers();
		VBOVertexHandle = GL15.glGenBuffers();
		StartX = startX;
		StartY = startY;
		StartZ = startZ;
		RebuildMesh(startX, startY, startZ);
	}
	//r.nextFloat()*10
	public void RebuildMesh(float startX, float startY, float startZ) {
		VBOColorHandle = GL15.glGenBuffers();
		VBOVertexHandle = GL15.glGenBuffers();
		long time = System.nanoTime();
		FloatBuffer VertexPositionData = BufferUtils
				.createFloatBuffer((CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE) * 6 * 12);
		FloatBuffer VertexColorData = BufferUtils.createFloatBuffer((CHUNK_SIZE
				* CHUNK_SIZE * CHUNK_SIZE) * 6 * 12);
		for (float x = 0; x < CHUNK_SIZE; x += 1) {
			for (float y = 0; y < CHUNK_SIZE; y += 1) {
				for (float z = 0; z < CHUNK_SIZE; z += 1) {
					VertexPositionData.put(CreateCube((float) startX + x
							* CUBE_LENGTH, (float) startY + y * CUBE_LENGTH,
							(float) startZ + z * CUBE_LENGTH));
					 VertexColorData.put(CreateCubeVertexCol(GetCubeColor(Blocks[(int)
					 (x-startX)][(int) (y-startY)][(int) (z-startZ)])));
					}
			}
			
		}
		
		// System.out.print(""+x+" "+y+" "+z);
		VertexColorData.flip();
		VertexPositionData.flip();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBOVertexHandle);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, VertexPositionData,
				GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBOColorHandle);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, VertexColorData,
				GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

	}

	private float[] CreateCubeVertexCol(float[] CubeColorArray) {
		float[] cubeColors = new float[CubeColorArray.length * 4 * 6];
		for (int i = 0; i < cubeColors.length; i++) {
			cubeColors[i] = CubeColorArray[i % CubeColorArray.length];
		}
		return cubeColors;
	}

	public static float[] CreateCube(float x, float y, float z) {
		int offset = CUBE_LENGTH / 2;
		return new float[] {
				// BOTTOM QUAD(DOWN=+Y)
				x + offset, y + offset,
				z,
				x - offset,
				y + offset,
				z,
				x - offset,
				y + offset,
				z - CUBE_LENGTH,
				x + offset,
				y + offset,
				z - CUBE_LENGTH,
				// TOP!
				x + offset, y - offset, z - CUBE_LENGTH, x - offset,
				y - offset,
				z - CUBE_LENGTH,
				x - offset,
				y - offset,
				z,
				x + offset,
				y - offset,
				z,
				// FRONT QUAD
				x + offset, y + offset, z - CUBE_LENGTH, x - offset,
				y + offset, z - CUBE_LENGTH, x - offset,
				y - offset,
				z - CUBE_LENGTH,
				x + offset,
				y - offset,
				z - CUBE_LENGTH,
				// BACK QUAD
				x + offset, y - offset, z, x - offset, y - offset, z,
				x - offset, y + offset, z,
				x + offset,
				y + offset,
				z,
				// LEFT QUAD
				x - offset, y + offset, z - CUBE_LENGTH, x - offset,
				y + offset, z, x - offset, y - offset, z, x - offset,
				y - offset,
				z - CUBE_LENGTH,
				// RIGHT QUAD
				x + offset, y + offset, z, x + offset, y + offset,
				z - CUBE_LENGTH, x + offset, y - offset, z - CUBE_LENGTH,
				x + offset, y - offset, z };

	}

	private float[] GetCubeColor(Block block) {
		switch (block.GetID()) {
		case 1:
			return new float[] { 0, 1, 0 };

		case 2:
			return new float[] { 1, 0.5f, 0 };
		case 3:
			return new float[] { 0, 0f, 1f };
		}
		return new float[] { 1, 1, 1 };
	}
	private float[] GetNormalVector(){
		return new float[]{
				//BOTTOM
				0, 1, 0,
				0, 1, 0,
				0, 1, 0,
				0, 1, 0,
				//TOP
				0, -1, 0,
				0, -1, 0,
				0, -1, 0,
				0, -1, 0,
				//FRONT
				0, 0, 1,
				0, 0, 1,
				0, 0, 1,
				0, 0, 1,
				//BOTTOM
				0, 0, 1,
				0, 0, 1,
				0, 0, 1,
				0, 0, 1,
				//LEFT QUAD
				1, 0, 0,
				1, 0, 0,
				1, 0, 0,
				1, 0, 0,
				//RIGHT QUAD
				-1, 0, 0,
				-1, 0, 0,
				-1, 0, 0,
				-1, 0, 0, };
	}
}
