import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;
 
//First Person Camera Controller
public class Player
{
    //3d vector to store the camera's position in
    private Vector3f    position    = null;
    //the rotation around the Y axis of the camera
    private float       yaw         = 0.0f;
    //the rotation around the X axis of the camera
    private float       pitch       = 0.0f;
     int inAir=0;
     int falling = 0;
     //int voxX;
    //int voxY;
     //int voxZ;
     
   // voxX=((int) position.x/-2);
   // voxY=((int) (position.y/-2)-2);
   // voxZ=((int) position.z/-2);
  //Constructor that takes the starting x, y, z location of the camera
    public Player(float x, float y, float z)
    {
        //instantiate position Vector3f to the x y z params.
        position = new Vector3f(x, y, z);
    }
    	public int getX(){
    		int x =  ((int) position.x/-2);
    		
    		return x;
    	}
    	
    	public int getY(){
    		int y = ((int) (position.y/-2)-1);
    		return y;
    	}
    	
    	public int getZ(){
    		int z = ((int) position.z/-2);
    		return z;
    	}
  //increment the camera's current yaw rotation
    public void yaw(float amount)
    {
        //increment the yaw by the amount param
        yaw += amount;
    }
     
    //increment the camera's current yaw rotation
    public void pitch(float amount)
    {
        //increment the pitch by the amount param
        pitch += amount;
    }
    
  //moves the camera forward relative to its current rotation (yaw)
    public void walkForward(float distance)
    {
        position.x -= distance * (float)Math.sin(Math.toRadians(yaw));
        position.z += distance * (float)Math.cos(Math.toRadians(yaw));
    }
     
    //moves the camera backward relative to its current rotation (yaw)
    public void walkBackwards(float distance)
    {
        position.x += distance * (float)Math.sin(Math.toRadians(yaw));
        position.z -= distance * (float)Math.cos(Math.toRadians(yaw));
    }
     
    //strafes the camera left relative to its current rotation (yaw)
    public void strafeLeft(float distance)
    {
        position.x -= distance * (float)Math.sin(Math.toRadians(yaw-90));
        position.z += distance * (float)Math.cos(Math.toRadians(yaw-90));
    }
     
    //strafes the camera right relative to its current rotation (yaw)
    public void strafeRight(float distance)
    {
        position.x -= distance * (float)Math.sin(Math.toRadians(yaw+90));
        position.z += distance * (float)Math.cos(Math.toRadians(yaw+90));
    }
    
    public void Jump(float height)
    {
    	
		if((int)inAir != 1){
			System.out.print(position.y);
    		inAir=1;
    		int newy = getY()+2;
    		while(getY()>newy)
    				{
    					position.y -=0.5;
    				}
    		falling =1;	
    		fall();
		}else if((int)inAir == 1){
			fall();
		}
		
  }
    
    
    
    private void fall(){
    	
		while (falling==1){
			int newy = getY()+2;
    		while(getY()<newy)
    				{
    					position.y +=0.5;
    					Collision(position.x,position.y,position.z);
    				}
			
		}
	}

	public void Collision(float x, float y, float z){
    		int playerX = getX();
			int playerY = getY();
			int playerZ = getZ();
        	int a = Game.chunky.Blocks[playerX][playerY][playerZ].GetID();
            System.out.print(a);
			if(a >= 0)
			{
				falling=0;
				inAir=0;
			}
		}
    
    
    
    //translates and rotate the matrix so that it looks through the camera
    //this dose basic what gluLookAt() does
    public void lookThrough()
    {
        //roatate the pitch around the X axis
        GL11.glRotatef(pitch, -1.0f, 0.0f, 0.0f);
        //roatate the yaw around the Y axis
        GL11.glRotatef(yaw, 0.0f, 1.0f, 0.0f);
        //translate to the position vector's location
        GL11.glTranslatef(position.x, position.y, position.z);
        
    }
 
}
    

