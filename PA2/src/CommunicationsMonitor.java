import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.ArrayList;
import java.util.Arrays;
/**
 * The CommunicationsMonitor class represents the graph G
 * built to answer infection queries.
 *
 * @author Kyle Bowden
 * @author Cassandra Labath
 * @author dirty thicc
 */
public class CommunicationsMonitor {

	HashMap<Integer, List<ComputerNode> > nodeMap = new HashMap<Integer, List<ComputerNode>>(); 
	ArrayList<triple> communications = new ArrayList<>();
	triple[] triples;
	boolean graphStarted; //variable indicating that creategraph has begun
    /**
     * Constructor with no parameters
     */
    public CommunicationsMonitor() {
    	graphStarted = false;
    	
    }

    /**
     * Takes as input two integers c1, c2, and a timestamp. This triple represents the fact that the computers with IDs
     * c1 and c2 have communicated at the given timestamp. This method should run in O(1) time. Any invocation of this
     * method after createGraph() is called will be ignored.
     *
     * @param c1        First ComputerNode in the communication pair.
     * @param c2        Second ComputerNode in the communication pair.
     * @param timestamp Time the communication took place.
     */
    public void addCommunication(int c1, int c2, int timestamp){
    	if(!graphStarted)
    	{
    		communications.add(new triple(c1, c2, timestamp));
    		
    	}
    }

    /**
     * Constructs the data structure as specified in the Section 2. This method should run in O(n + m log m) time.
     */
    public void createGraph() {
    	triples = new triple[communications.size()];
    	communications.toArray(triples);
    	graphStarted = true; 
    	triples = Mergesort(triples);
    	for(int i = 0; i < triples.length; i++)
    	{
    		 if(!nodeMap.containsKey(triples[i].c1))				
    		 {
    			nodeMap.put(triples[i].c1, new ArrayList<ComputerNode>()); // creates associated list for node for c1 if it doesn't exist
    		 }
    		 if(!nodeMap.containsKey(triples[i].c2))			
    		 {
    			nodeMap.put(triples[i].c2, new ArrayList<ComputerNode>());  //creates associated list for node for c2 if it doesn't exist
    		 }
    		 
    		 
    		 
    		 Iterator<ComputerNode> iter1 = nodeMap.get(triples[i].c1).iterator();
    		 Iterator<ComputerNode> iter2 = nodeMap.get(triples[i].c2).iterator();
    	    	
    		 ComputerNode Node1 = new ComputerNode(triples[i].c1, triples[i].timestamp);
    		 ComputerNode temp1 = null;
    		 ComputerNode Node1follow = null; //follows Node1 in the iterator, also determining if Node1 is the first in c1's list
    		 ComputerNode Node2 = new ComputerNode(triples[i].c2, triples[i].timestamp);
    		 ComputerNode Node2follow = null;  //follows Node2 in the iterator, also determining if Node2 is the first in c2's list
    		 ComputerNode temp2 = null;
       	  	
    		 
    		 while(iter1.hasNext()) //this while loop searches to see if Node1 exists and ensures Node1 is the object we want to modify
    		 {						//it also appends the reference to the node to the list
    			 Node1follow = temp1;
    			 temp1 = iter1.next();
    			 if(Node1.equals(temp1))
    			 {
    				 Node1 = temp1;
    				 break;
    			 }
    			
    		 } 
    		 
    
			 nodeMap.get(triples[i].c1).add(new ComputerNode(triples[i].c1, triples[i].timestamp));   //create them
		    	
    		   	
    		 while(iter2.hasNext())  //this while loop searches to see if Node2 exists and ensures Node2 is the object we want to modify
    		 {						 //it also appends the reference to the node to the list
    			 Node2follow = temp2;
    			 temp2 = iter2.next();
    			 if(Node2.equals(temp2))
    			 {
    				 Node2 = temp2;
    				 break;
    			 }
    		 } 
    		 
    	 	nodeMap.get(triples[i].c2).add(new ComputerNode(triples[i].c2, triples[i].timestamp));  //create them	    	
	 
    		 
    		 Node1.getOutNeighbors().add(Node2);  // adding directed edges
    		 Node2.getOutNeighbors().add(Node1); // adding directed edges
    		 
    		 if(Node1follow != null)
    			 Node1follow.getOutNeighbors().add(Node1); //adding directed edge from (Ci, t) to (Ci, tk) if (Ci,t) exists
    		 if(Node2follow != null)
    			 Node2follow.getOutNeighbors().add(Node2); //adding directed edge from (Cj, t) to (Cj, tk) if (Cj,t) exists
    		 
    	}
    }

    /**
     * Determines whether computer c2 could be infected by time y if computer c1 was infected at time x. If so, the
     * method returns an ordered list of ComputerNode objects that represents the transmission sequence. This sequence
     * is a path in graph G. The first ComputerNode object on the path will correspond to c1. Similarly, the last
     * ComputerNode object on the path will correspond to c2. If c2 cannot be infected, return null.
     * <p>
     * Example 3. In Example 1, an infection path would be (C1, 4), (C2, 4), (C2, 8), (C4, 8), (C3, 8)
     * <p>
     * This method can assume that it will be called only after createGraph() and that x <= y. This method must run in
     * O(m) time. This method can also be called multiple times with different inputs once the graph is constructed
     * (i.e., once createGraph() has been invoked).
     *
     * @param c1 ComputerNode object to represent the Computer that is hypothetically infected at time x.
     * @param c2 ComputerNode object to represent the Computer to be tested for possible infection if c1 was infected.
     * @param x  Time c1 was hypothetically infected.
     * @param y  Time c2 is being tested for being infected.
     * @return List of the path in the graph (infection path) if one exists, null otherwise.
     */
    public List<ComputerNode> queryInfection(int c1, int c2, int x, int y) {
        ComputerNode desiredStartNode = new ComputerNode(c1,x);
        Iterator<ComputerNode> it;
        ComputerNode tempy = null;
        List<ComputerNode> path = null;
        if(nodeMap.containsKey(c1))
        {
        	it = nodeMap.get(c1).listIterator();
        	while(it.hasNext())
        	{
        		tempy = it.next();
        		if(desiredStartNode.equals(tempy))
        		{
        			desiredStartNode = tempy;
        		}
        	}
        }
        else
        	throw new NoSuchElementException("That number of computer doesn't exist");
        resetGraph();
        DFS(desiredStartNode, new ComputerNode(c2,y));
        
        return path;
        
    }

    /**
     * Returns a HashMap that represents the mapping between an Integer and a list of ComputerNode objects. The Integer
     * represents the ID of some computer Ci, while the list consists of pairs (Ci, t1),(Ci, t2),..., (Ci, tk),
     * represented by ComputerNode objects, that specify that Ci has communicated with other computers at times
     * t1, t2,...,tk. The list for each computer must be ordered by time; i.e., t1\<t2\<...\<tk.
     *
     * @return HashMap representing the mapping of an Integer and ComputerNode objects.
     */
    public HashMap<Integer, List<ComputerNode>> getComputerMapping() {
        return nodeMap;
    }

    /**
     * Returns the list of ComputerNode objects associated with computer c by performing a lookup in the mapping.
     *
     * @param c ID of computer
     * @return ComputerNode objects associated with c.
     */
    public List<ComputerNode> getComputerMapping(int c) {
        return nodeMap.get(c);
    }
    
    public boolean DFS(ComputerNode Node1, ComputerNode desiredNodeAtTime)
    {
    	ComputerNode nextNode;
    	Node1.visitNode();
    	if(Node1.isSameComputerAndBefore(desiredNodeAtTime))
    		return true;
    	else
    	{
    		Iterator<ComputerNode> iterator1 = Node1.getOutNeighbors().iterator();
	    	while(iterator1.hasNext())
	    	{
	    		nextNode = iterator1.next();
	    		if(nextNode.getColor() == 0)
	    		{
	    			if(DFS(iterator1.next(), desiredNodeAtTime));
	    				return true;
	    		}
	    	}
	    	
	    	return false;
    	}
    	
    }
    public boolean BFS(ComputerNode Node1, ComputerNode desiredNodeAtTime)
    {
    	Iterator<ComputerNode> iterator1 = Node1.getOutNeighbors().iterator();
    	Iterator<ComputerNode> iterator2 = Node1.getOutNeighbors().iterator(); 
    	return false;
    }
    
    public triple[] Mergesort(triple[] triples) {
    	int n = triples.length;
    	if (n == 1) {
    		return triples;
    	}
    	triple[] left = new triple[n/2];
    	triple[] right;
    	if(n % 2 == 0) {
    		right = new triple[n/2];
    	} else {
    		right = new triple[n/2 + 1];
    	}
    	int place = 0;
    	for(int i = 0; i < n/2; i++) {
    		left[place] = triples[i];
    		place++;
    	}
    	place = 0;
    	for(int i = n/2; i < n; i++) {
    		right[place] = triples[i];
    		place++;
    	}
    	triples = Merge(Mergesort(left), Mergesort(right));
    	return triples;
    }
    
    public triple[] Merge(triple[] left, triple[] right) {
    	int p = left.length;
    	int q = right.length;
    	triple[] merged = new triple[p+q];
    	int place = 0;
    	int i = 0;
    	int j = 0;
    	while(i < p && j < q) {
    		if(left[i].timestamp <= right[j].timestamp) {
    			merged[place] = left[i];
    			i++;
    			place++;
    		} else {
    			merged[place] = right[j];
    			j++;
    			place++;
    		}
    	}
    	if(i>=p) {
    		for(int k=j; k < q;k++){
    			merged[place] = right[k];
    			place++;
    		}
    	} else {
    		for(int k=i; k < p; k++) {
    			merged[place] = left[k];
    			place++;
    		}
    	}
    	return merged;
    }
    
    public class triple {
    	public int c1;
    	public int c2;
    	public int timestamp;
    	
    	public triple(int c1, int c2, int timestamp) {
    		this.c1 = c1;
    		this.c2 = c2;
    		this.timestamp = timestamp;
    	}
    }

    private void resetGraph()
    {
    	for(int i = 1;  i < nodeMap.size() + 1; i ++)
    	{
    		Iterator<ComputerNode> itER = nodeMap.get(i).iterator();
    		while(itER.hasNext())
    		{
    			itER.next().resetColor();;
    		}
    	}
    }
    
    public static void main(String args[]) {
    	CommunicationsMonitor coms = new CommunicationsMonitor();
    	coms.addCommunication(1, 2, 5);
    	coms.addCommunication(1, 3, 1);
    	coms.addCommunication(2, 3, 5);
    	coms.addCommunication(3, 5, 7);
    	coms.addCommunication(5, 4, 2);
    	coms.createGraph();
    	
    	
    	for(int i = 1; i < 6; i++)
    			
    	{
    		Iterator<ComputerNode> it = coms.nodeMap.get(i).iterator();
	    	while(it.hasNext())
	    	{
	    		Iterator<ComputerNode> its = it.next().getOutNeighbors().iterator();
	    		while(its.hasNext())
	    		{
	    			ComputerNode tempi = its.next();
	    			System.out.println("node " + i + " has neighbor Node " + tempi.getID() + " at time " + tempi.getTimestamp());
	    		}
	    		
	    		System.out.println();
	    	}
    	}
    }
    
}
