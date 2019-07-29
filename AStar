Practical 2
package search;

public interface NodeFunction {
	
	public abstract int value(Node n);

}


package search;

public class Node {
	public final Node parent;
	public final Action action;
	public final State state;
	public int value = 0;
	public final int g;
	public Node(Node parent, Action action, State state) {
		this.parent = parent;
		this.action = action;
		this.state = state;
		if (parent == null) {
			g=0;
		}
		else {
			g=parent.g + action.cost();
		}
	}
}


package search;

public interface Action {
	public abstract int cost();
}
package search;


public class AStarFunction implements NodeFunction {
	public NodeFunction h;
	public AStarFunction(NodeFunction heuristic) {
		h = heuristic;
	}
	public int value(Node n) {
		return n.g + h.value(n);
	};

}






package search;
import java.util.PriorityQueue;
import java.util.Comparator;

public class BestFirstFrontier implements frontier {
	public static NodeFunction nfunc;
	
	public BestFirstFrontier(NodeFunction nf) {
		nfunc = nf;
	}
	
	public static Comparator<Node> nodeCompare = new Comparator<Node>() {
		@Override
		public int compare(Node n1, Node n2) {
			return (int) (n1.value-n2.value);
		}
	};
	
	public PriorityQueue<Node> collection = new PriorityQueue<Node>(nodeCompare);
	public int stored = 0;
	public void add(Node nodeToAdd) {
		nodeToAdd.value = nfunc.value(nodeToAdd);
		collection.add(nodeToAdd);
		if (collection.size()>stored) {stored = collection.size();};
	};
	public void clear() {
		collection.clear();
		stored = 0;
	}
	public boolean isEmpty() {
		return collection.isEmpty();
	}
	public int maxStored() {
		return stored;
	}	
	public Node remove() {
		return collection.remove();
	};

package npuzzle;
import search.Node;
import search.NodeFunction;
import npuzzle.Tiles;

public class MisplacedTilesHeuristicFunction implements NodeFunction {
	public int value(Node n) {
		int count = 0;
		Tiles t = (Tiles) n.state;
		for (int i = 0 ; i < t.width * t.width-1; i++) {
			if (t.tiles[i] != i + 1)
			{
				count +=1;
			}	
		}
		return count;	
	};
}
package npuzzle;

import search.TreeSearch;
import search.GraphSearch;
import search.GoalTest;
import search.Node;
import search.frontier;
import search.Search;
import search.BestFirstFrontier;
import search.NodeFunction;
import search.AStarFunction;
import npuzzle.MisplacedTilesHeuristicFunction;

public class lab2 {
	public static void main(String[] args) {
		System.out.println("This is a demonstration of A* tree search on 8-puzzle");
		System.out.println();
		
		Tiles initialConfiguration = new Tiles(new int[][] {
			{ 7, 4, 2 },
			{ 8, 1, 3 },
			{ 5, 0, 6 }
		});

		 
		GoalTest goalTest = new TilesGoalTest();
		NodeFunction h = new MisplacedTilesHeuristicFunction();
		NodeFunction fn = new AStarFunction(h);
		frontier f = new BestFirstFrontier(fn);
		Search s = new TreeSearch(f);		
		Node solution = s.findSolution(initialConfiguration, goalTest);
		
		new NPuzzlePrinting().printSolution(solution);
		System.out.println("A* Tree Search");
		System.out.println(s.numberGen());
		System.out.println(f.maxStored());
		
		f.clear();
		s = new GraphSearch(f);
		solution = s.findSolution(initialConfiguration, goalTest);
		System.out.println("A* Graph Search");
		System.out.println(s.numberGen());
		System.out.println(f.maxStored());
		
		// The values produced by the A* search are several orders of magnitude smaller for the A* search than either of the previous 2 frontiers. The A* search with a good heuristic is a much better way of searching for a solution
	}
}
Output – 
A* Tree Search
963
613
A* Graph Search
224
91

