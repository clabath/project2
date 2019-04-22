import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
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
     * Takes as input two integers c1, c2, and 
     * 
     * a timestamp. This triple represents the fact that the computers with IDs
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
    		 ComputerNode Node2 = new ComputerNode(triples[i].c2, triples[i].timestamp);
    		 ComputerNode temp2 = null;
       	  	
    		 
    		 while(iter1.hasNext()) //this while loop searches to see if Node1 exists and ensures Node1 is the object we want to modify
    		 {						//it also appends the reference to the node to the list
    			 temp1 = iter1.next();
    			 if(Node1.equals(temp1))
    			 {
    				 Node1 = temp1;
    				 break;
    			 }
    			
    		 } 
    		 
    		 if(temp1 != null && !iter1.hasNext())
    			 temp1.getOutNeighbors().add(Node1); //adding directed edge from (Ci, t) to (Ci, tk) if (Ci,t) exists
  
    		 
    
			 if(!iter1.hasNext() && Node1 != temp1)
			 {
				nodeMap.get(triples[i].c1).add(Node1);   //create them
			 }
    		   	
    		 while(iter2.hasNext())  //this while loop searches to see if Node2 exists and ensures Node2 is the object we want to modify
    		 {						 //it also appends the reference to the node to the list
    			 temp2 = iter2.next();
    			 if(Node2.equals(temp2))
    			 {
    				 Node2 = temp2;
    				 break;
    			 }
    		 } 
    		 
    		 if(temp2 != null && !iter2.hasNext())	
    			 temp2.getOutNeighbors().add(Node2); //adding directed edge from (Cj, t) to (Cj, tk) if (Cj,t) exists
    		 
    	
    		 if(!iter2.hasNext() && Node2 != temp2)
			 {
				nodeMap.get(triples[i].c2).add(Node2);   //create them
			 }
    		 

    	  	 Node1.getOutNeighbors().add(Node2);  // adding directed edges
    		 Node2.getOutNeighbors().add(Node1); // adding directed edges
    	
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
        ArrayList<ComputerNode> path = null;
        if(nodeMap.containsKey(c1))
        {
        	it = nodeMap.get(c1).listIterator();
        	while(it.hasNext())
        	{
        		tempy = it.next();
        		if(desiredStartNode.isSameComputerAndBefore(tempy))
        		{
        			desiredStartNode = tempy;
        			break;
        		}
        	}
        }
        else
        	throw new NoSuchElementException("That number of computer doesn't exist");
        resetGraph();
        path = new ArrayList<ComputerNode>();
        DFS(desiredStartNode, new ComputerNode(c2,y), path);
        if(!path.isEmpty() || c1 == c2)
        		path.add(desiredStartNode);
        path = tempPathSwap(path);            //temporary fix for backwards infection path
        return path;
        
    }
    
    public ArrayList<ComputerNode> tempPathSwap( ArrayList<ComputerNode> path){
    	 ArrayList<ComputerNode> swapped = new ArrayList<ComputerNode>();
    	 for(int i = path.size() - 1; i >= 0; i--) {
    		 swapped.add(path.get(i));
    	 }
    	 return swapped;
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
     * 
     * Returns the list of ComputerNode objects associated with computer c by performing a lookup in the mapping.
     *
     * @param c ID of computer
     * @return ComputerNode objects associated with c.
     */
    public List<ComputerNode> getComputerMapping(int c) {
        return nodeMap.get(c);
    }
    
    public boolean DFS(ComputerNode Node1, ComputerNode desiredNodeAtTime, ArrayList<ComputerNode> nodePath)
    {

    	ComputerNode nextNode = null;
    	Node1.visitNode();
    	if(Node1.isSameComputerAndBefore(desiredNodeAtTime))
    		{
    			return true;
    		}
    	else
    	{
    		Iterator<ComputerNode> iterator1 = Node1.getOutNeighbors().iterator();
	    	while(iterator1.hasNext())
	    	{
	    		nextNode = iterator1.next();
	    		if(nextNode.getColor() == 0)
	    		{
	    			
	    			if(DFS(nextNode, desiredNodeAtTime, nodePath))
	    				{
	    					nodePath.add(nextNode);
	    					return true;
	    				}
	    		}
	    	}
    	}
    	
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
    	
    	nodeMap.forEach((computer, list) -> {
    		Iterator<ComputerNode> it = list.iterator();
    		while(it.hasNext())
    		{
    			it.next().resetColor();
    		}
    		
    		
    	});
    }
    
    public static void main(String args[]) {
    	CommunicationsMonitor coms = new CommunicationsMonitor();
 
    	
    	for(int i = 1; i < 500; i++) {
    		coms.addCommunication(i, i+1, i);
    	}
    	coms.createGraph();
    	
    	
    	coms.nodeMap.forEach((computer, list) -> {
    		Iterator<ComputerNode> it = list.iterator();
    		Iterator<ComputerNode> its;
    		while(it.hasNext())
    		{
    			ComputerNode nodes = it.next();
    			its = nodes.getOutNeighbors().iterator();
    			while(its.hasNext())
    			{
    				ComputerNode tempi = its.next();

    				System.out.println("Computer " + computer + " has a communication with computer " + tempi.getID() + " at time " + tempi.getTimestamp());
    			}
    					
    		}
    		
    		
    	});
    	
    	
    	
    	Scanner scan = new Scanner(System.in);
    	int c1, t1, c2, t2;
    	System.out.println("gimme the infected computer's number");
    	c1 = scan.nextInt();
    	System.out.println("gimme the infected computer's time of infection");
    	t1 = scan.nextInt();
    	System.out.println("gimme the computer's number that you want to check if infected");
    	c2 = scan.nextInt();
    	System.out.println("gimme the time you want to check if it's infected by");
    	t2 = scan.nextInt();
    	System.out.println();
    	System.out.println("lit fam give me a minute while i find if there's a path");
    	System.out.println("...");
      	System.out.println("...");
      	System.out.println("...");
    	System.out.println();
    	
    	List<ComputerNode> list = coms.queryInfection(c1,c2,t1,t2);
    	Iterator<ComputerNode> it1 = list.iterator();
    	ComputerNode temp;
    	{
    		System.out.println("One path is: ");
    		while (it1.hasNext())
    		{
    			temp = it1.next();
    			
    			System.out.print("Computer " + temp.getID() + " at time " +temp.getTimestamp());
    			if(it1.hasNext())
    			{
    				System.out.println("-->");
    			}
    		}
    	}
		System.out.println();
		scan.close();
    }
    
}
