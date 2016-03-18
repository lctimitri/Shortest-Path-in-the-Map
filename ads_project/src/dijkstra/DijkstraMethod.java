package dijkstra;

import java.util.*;

public class DijkstraMethod {

	public static int[] dijkstraLH(Graph g) {
	    int x=g.getSource();
	    //System.out.println("source="+x);
	    int n=g.getNumber();
	    //System.out.println("nodes#= "+n);
	    
	    ArrayList<ArrayList<Integer>> graph=g.getGraph();
	    
	    LeftistHeap lh = new LeftistHeap(); //establish a new Leftist Heap
		LeftistHeapNode[] distance = new LeftistHeapNode[n]; //distance is stored in the array of entry
		
		int[] result = new int[n];
		boolean[] determined = new boolean[n];
		
		for(int i=0;i<n;i++){
			distance[i]= new LeftistHeapNode(i, Integer.MAX_VALUE);
		}
		distance[x] = new LeftistHeapNode(x, 0);
		// use queue to build LeftistHeap time cost O(n)
		Queue<LeftistHeapNode> q = new LinkedList<LeftistHeapNode>();
		for(LeftistHeapNode y:distance){
			q.offer(y);
		}
		LeftistHeapNode w=null;
    	while(q.size()>1){
    		LeftistHeapNode u = q.poll();
    		LeftistHeapNode v = q.poll();
    		w = lh.merge(u,v);
    		q.offer(w);
    	}
    	lh.root = w;
		// do extract min and relaxation
		while (!lh.isEmpty()) {
			LeftistHeapNode curr = lh.deleteMin();
			int min = curr.getKey();	//get value return the number of the node
			int cost = curr.getValue();	//get priority return the distance to that node
			
			determined[min] = true;	
			result[min] = cost;

			//for the node that adjacent to the [min] node, if it shortest path was not determined
			//try to update it distance if need
			for (int i = 0; i < graph.get(min).size()/2; i++) {
				int adj = graph.get(min).get(2*i);
				int arc = graph.get(min).get(2*i+1);

				int dist = distance[adj].getValue(); //dist is the current shortest path to the node adjacent to [min]
				if (!determined[adj] && dist > cost + arc) {
					distance[adj] = lh.decreaseKey(distance[adj], cost + arc);
				}
			}		
		}

		return result;
	} 

	public static int[] dijkstraFib(Graph g) {
	    int x=g.getSource();
	    int n=g.getNumber();
	    ArrayList<ArrayList<Integer>> graph=g.getGraph();
	    
		FibonacciHeap fh = new FibonacciHeap(); //establish a new Fibonacci Heap
		FibonacciHeap.Node[] distance = new FibonacciHeap.Node[n]; //distance is stored in the array of entry
		int[] result = new int[n];
		boolean[] determined = new boolean[n];

		//initialize it for the first time and make all the distance to max_value
		for(int i = 0; i < n; i++){
			distance[i] = fh.insert(i, Integer.MAX_VALUE); 
			//notice that enqueue will return a reference to the entry we inserted
		}
		fh.decreaseKey(distance[x], 0); //decrease the distance from source to source node to 0

		while (!fh.isEmpty()) {

			FibonacciHeap.Node curr = fh.removeMin();
			int min = curr.getKey();	//get value return the number of the node
			int cost = curr.getValue();	//get priority return the distance to that node
			determined[min] = true;	
			result[min] = cost;

			//for the node that adjacent to the [min] node, if it shortest path was not determined
			//try to update it distance if need
			for (int i = 0; i < graph.get(min).size()/2; i++) {
				int adj = graph.get(min).get(2*i);
				int arc = graph.get(min).get(2*i+1);

				int dist = distance[adj].getValue(); //dist is the current shortest path to the node adjacent to [min]
				if (!determined[adj] && dist > cost + arc) {
					fh.decreaseKey(distance[adj], cost + arc);
				}
			}		
		}

		return result;
	}
}

