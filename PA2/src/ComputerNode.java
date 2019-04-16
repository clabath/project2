import java.util.List;

/**
 * The ComputerNode class represents the nodes of the graph G, which are pairs (Ci, t).
 *
 * @author
 * @author
 * @author
 */
public class ComputerNode {

    /**
     * Returns the ID of the associated computer.
     *
     * @return Associated Computer's ID
     */
    public int getID() {
        return 0;
    }

    /**
     * Returns the timestamp associated with this node.
     *
     * @return Timestamp for the node
     */
    public int getTimestamp() {
        return 0;
    }

    /**
     * Returns a list of ComputerNode objects to which there is outgoing edge from this ComputerNode object.
     *
     * @return a list of ComputerNode objects that have an edge from this to the nodes in the list.
     */
    public List<ComputerNode> getOutNeighbors() {
        return null;
    }

}
