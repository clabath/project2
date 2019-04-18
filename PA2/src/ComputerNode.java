import java.util.List;

/**
 * The ComputerNode class represents the nodes of the graph G, which are pairs (Ci, t).
 *
 * @author Kyle Bowden
 * @author Cassandra Labath
 * @author dirty thicc
 */
public class ComputerNode {

	private int id;
	private int time;
	private List<ComputerNode> neighbors;
	
	public ComputerNode(int ID, int timestamp){
		id = ID;
		time = timestamp;
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

}
