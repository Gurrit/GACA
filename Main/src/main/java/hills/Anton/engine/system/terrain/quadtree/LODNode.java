package hills.Anton.engine.system.terrain.quadtree;

import java.util.ArrayList;
import java.util.List;

import hills.Anton.engine.math.Mat4;
import hills.Anton.engine.math.STD140Formatable;
import hills.Anton.engine.math.Vec3;

public class LODNode implements STD140Formatable {
	
	private static final float[] LOD_SCALES = {1.0f, 2.0f, 4.0f, 8.0f, 16.0f, 32.0f, 64.0f, 128.0f, 256.0f};
	
	private int lodLevel = -1;					// LOD Level
	private final float x, z;					// Position
	private final float width, depth, height;	// Size		
	private boolean[] subSectionsToHandle;		// Partial selection info
	
	private LODNode[] childNodes = null;
	
	public LODNode(float x, float z, float width, float depth, float height){
		this.x = x;
		this.z = z;
		this.width = width;
		this.depth = depth;
		this.height = height;
		
		subSectionsToHandle = new boolean[4];
	}
	
	public boolean genLODNodeTree(Vec3 pos, float[] ranges, int lodLevel) {
		this.lodLevel = -1; // Assume not a leaf node.
		
		// Check if node is within it's LOD range from position.
		// If not then return false and let parent node handle this subsection.
		if(!withinLODRange(pos, ranges[lodLevel])){
			//return false;
			this.lodLevel = lodLevel;
		}
		
		// TODO Add frustrum cull check!
		
		// If node is in the last LOD level (most detailed),
		// set this node as leaf node.
		if(lodLevel == 0)
			this.lodLevel = 0; // Add node as leaf node
		else
			// If node not in lowest LOD range,
			// but entire node within its own LOD range
			// add node to be drawn as is.
			// Else check the four children nodes to see
			// which ones are in a lower LOD range.
			if(!withinLODRange(pos, ranges[lodLevel - 1]))
				this.lodLevel = lodLevel; // Add node as leaf node.
			else {
				childNodes = getChildNodes();
				
				for(int i = 0; i < childNodes.length; i++)
					if(!childNodes[i].genLODNodeTree(pos, ranges, lodLevel - 1)){
						// If child node is outside of its LOD range,
						// let this node handle that subsection.
						subSectionsToHandle[i] = true; 
					}
			}
		
		return true;
	}
	
	/**
	 * GO through this nodes tree and fetch all leaf nodes.
	 * @return A list of all the leaf nodes.
	 */
	public List<LODNode> getLeafNodes(){
		if(lodLevel >= 0){
			List<LODNode> node = new ArrayList<LODNode>();
			node.add(this);
			return node;
		}
		
		List<LODNode> nodes = new ArrayList<LODNode>();
		for(LODNode node: childNodes)
			nodes.addAll(node.getLeafNodes());
		
		return nodes;
	}
	
	private boolean withinLODRange(Vec3 pos, float range){
		float cubeXPlane1 = x;
		float cubeXPlane2 = x + width;
		
		float cubeYPlane1 = 0.0f;
		float cubeYPlane2 = height;
		
		float cubeZPlane1 = z;
		float cubeZPlane2 = z + depth;
		
		float posX = pos.getX();
		float posY = pos.getY();
		float posZ = pos.getZ();
		
		float rangeSquared = range * range;
		
		if(posX < cubeXPlane1)
			rangeSquared -= (posX - cubeXPlane1) * (posX - cubeXPlane1);
		else if(posX > cubeXPlane2)
			rangeSquared -= (posX - cubeXPlane2) * (posX - cubeXPlane2);
		
		if(posY < cubeYPlane1)
			rangeSquared -= (posY - cubeYPlane1) * (posY - cubeYPlane1);
		else if(posY > cubeYPlane2)
			rangeSquared -= (posY - cubeYPlane2) * (posY - cubeYPlane2);
		
		if(posZ < cubeZPlane1)
			rangeSquared -= (posZ - cubeZPlane1) * (posZ - cubeZPlane1);
		else if(posZ > cubeZPlane2)
			rangeSquared -= (posZ - cubeZPlane2) * (posZ - cubeZPlane2);
		
		return rangeSquared > 0;
	}
	
	/**
	 * Create and get this nodes 4 child nodes.
	 * @return This nodes 4 child nodes.
	 */
	private LODNode[] getChildNodes(){
		LODNode[] nodes = new LODNode[4];
		
		float width = this.width / 2.0f;
		float depth = this.depth / 2.0f;
		
		nodes[0] = new LODNode(x + width, z + depth, width, depth, height);
		nodes[1] = new LODNode(x, z + depth, width, depth, height);
		nodes[2] = new LODNode(x, z, width, depth, height);
		nodes[3] = new LODNode(x + width, z, width, depth, height);
		
		return nodes;
	}
	
	@Override
	public byte[] get140Data() {
		float scale = LOD_SCALES[lodLevel];
		Mat4 matrix = Mat4.identity().setScale(scale, 0.0f, scale).setTranslation(x, 0.0f, z);
		
		return matrix.get140Data();
	}
}