
public class Block {

 private boolean IsActive;
 private BlockType Type;

 public enum BlockType {
 BlockType_Default(0),
 BlockType_Grass(1),
 BlockType_Dirt(2),
 BlockType_Water(3), 
 BlockType_Stone(4),
 BlockType_Wood(5),
 BlockType_Sand(6),
 BlockType_NumTypes(7);
 private int BlockID;
 BlockType(int i) {
 BlockID=i;
 }
 public int GetID(){
 return BlockID;
 }
 }
    public Block(BlockType type){
 Type= type;
    }

 public boolean IsActive() {
 return IsActive;
 }
        
 public void SetActive(boolean active) {
 IsActive=active;
 }
    public int GetID(){
 return Type.GetID();
    }
}
