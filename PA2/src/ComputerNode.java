import java.util.ArrayList;
import java.util.List;

/**
 * The ComputerNode class represents the nodes of the graph G, which are pairs (Ci, t).
 *
 * @author Kyle Bowden
 * @author Cassandra Labath
 * @author Kyle Trom
 */
public class ComputerNode {

	private int id;
	private int time;
	private List<ComputerNode> neighbors;
	private int color;
	
	public ComputerNode(int ID, int timestamp){
		id = ID;
		time = timestamp;
		color = 0;
		neighbors = new ArrayList<ComputerNode>();
	}
	
    /**
     * Returns the ID of the associated computer.
     *
     * @return Associated Computer's ID
     */
    public int getID() {
        return id;
    }

    /**
     * Returns the timestamp associated with this node.
     *
     * @return Timestamp for the node
     */
    public int getTimestamp() {
        return time;
    }

    /**
     * Returns a list of ComputerNode objects to which there is outgoing edge from this ComputerNode object.
     *
     * @return a list of ComputerNode objects that have an edge from this to the nodes in the list.
     */
    public List<ComputerNode> getOutNeighbors() {
        return neighbors;
    }
    
    @Override
    public boolean equals(Object obj)
    {
    	if(obj == null)
    		return false;
    	if(!ComputerNode.class.isAssignableFrom(obj.getClass()))
    		return false;
    	if(this.id == ((ComputerNode)obj).id && this.time == ((ComputerNode)obj).time)
    	{
    		return true;
    	}
    	return false;
    }
    
    public boolean isSameComputerAndBefore(ComputerNode other)
    {
    	if(this.id == other.id && this.time <= other.time)
    		return true;
    	return false;
    }
    
    public void resetColor()
    {
    	this.color = 0;
    }
    
    public void visitNode()
    {
    	this.color = 1; 
    }
    
    public int getColor()
    {
    	return this.color;
    }

}
