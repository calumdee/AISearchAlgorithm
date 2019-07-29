Question 1

package search;

public interface frontier {

	public abstract void add(Node nodeToAdd);
	public abstract void clear();
	public abstract boolean isEmpty();
	public abstract Node remove();
	public abstract int maxStored();
}


package search;

import java.util.LinkedList;

public abstract class QueueFrontier implements frontier {
	public LinkedList<Node> collection = new LinkedList<Node>();
	public int stored = 0;
	public void add(Node nodeToAdd) {
		collection.addFirst(nodeToAdd);
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
	public abstract Node remove();
	
	
}

package search;

public class DepthFirstFrontier extends QueueFrontier {
	
	public Node remove() {
		if (!isEmpty()) {
			return collection.removeFirst();
		}
		return null;
	}
	
}

package search;

public class BreadthFirstFrontier extends QueueFrontier {
	
	public Node remove() {
		if (!isEmpty()) {
			return collection.removeLast();
		}
		return null;
	}
	
}

Question 2
package search;

import java.util.Set;

public interface State {
	Set<? extends Action> getApplicableActions();
	State getActionResult(Action action);
	boolean equals(Object that);
	int hashCode();
}
















package npuzzle;
import java.util.LinkedHashSet;
import java.util.Set;
import search.Action;
import search.State;

public class Tiles implements State {
	public static final int EMPTY_TILE = 0;
	protected final int width;
	protected final int[] tiles;
	protected final int emptyTileRow;
	protected final int emptyTileColumn;
	
	public Tiles(int[][] tiles) {
		width = tiles.length;
		this.tiles = new int[width * width];
		int emptyRow = -1;
		int emptyColumn = -1;
		int index=0;
		for (int row = 0; row < width; row++)
			for (int column = 0; column < width; column++) {
				int tile = tiles[row][column];
				if (tile == EMPTY_TILE) {
					emptyRow = row;
					emptyColumn = column;
				}
				this.tiles[index++] = tile;
			}
		emptyTileRow = emptyRow;
		emptyTileColumn = emptyColumn;
	}
	protected Tiles(int width, int[] tiles, int emptyTileRow, int emptyTileColumn) {
		this.width = width;
		this.tiles = tiles;
		this.emptyTileRow = emptyTileRow;
		this.emptyTileColumn = emptyTileColumn;
	}
	public int getWidth() {
		return width;
	}
	public int getEmptyTileRow() {
		return emptyTileRow;
	}
	public int getEmptyTileColumn() {
		return emptyTileColumn;
	}
	public int getTile(int row, int column) {
		return tiles[row * width + column];
	}
	public Set<Action> getApplicableActions() {
		Set<Action> actions = new LinkedHashSet<Action>();
		for (Movement movement : Movement.values()) {
			int newEmptyTileRow = emptyTileRow + movement.deltaRow;
			int newEmptyTileColumn = emptyTileColumn + movement.deltaColumn;
			if (0 <= newEmptyTileRow && newEmptyTileRow < width && 0 <= newEmptyTileColumn & newEmptyTileColumn < width)
				actions.add(movement);
		}
		return actions;
	}
	public State getActionResult(Action action) {
		Movement movement=(Movement)action;
		int newEmptyTileRow = emptyTileRow + movement.deltaRow;
		int newEmptyTileColumn = emptyTileColumn + movement.deltaColumn;
		int[] newTiles = tiles.clone();
		newTiles[emptyTileRow * width + emptyTileColumn] = getTile(newEmptyTileRow, newEmptyTileColumn);
		newTiles[newEmptyTileRow * width + newEmptyTileColumn] = EMPTY_TILE;
		return new Tiles(width, newTiles, newEmptyTileRow, newEmptyTileColumn);
	}
	public boolean equals(Object that) {
		if (this == that) {return true;};
		if (that == null || that.getClass()!=this.getClass()) {return false;};
		Tiles thats = (Tiles) that;
		if (width != thats.getWidth()) {return false;};
		boolean allsame = true;
		int i;
		int j;
		for (i=0; (i<(width))&&allsame; i++) {
			for (j=0; (j<width)&&allsame;j++ ) {
				allsame = tiles[i*width+j]==thats.getTile(i,j);
			}
		}
		return allsame;
	}
	public int hashcode() {
		int i;
		int t = 0;
		for(i=0; i<width*width;i++) {
			t += Math.pow(width*width, i)*tiles[i];
		}
		return t;
	}
}
package tour;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import search.Action;
import search.State;

public class TourState implements State {
	protected final Set<City> visitedCities;
	protected final City currentCity;
	
	public TourState(City startCity) {
		this.visitedCities = Collections.emptySet();
		this.currentCity = startCity;
	}
	public TourState(Set<City> visitedCities, City currentCity) {
		this.visitedCities = visitedCities;
		this.currentCity = currentCity;
	}
	public Set<Road> getApplicableActions() {
		return currentCity.outgoingRoads;
	}
	public State getActionResult(Action action) {
		Road road = (Road)action;
		Set<City> newVisitedCities = new LinkedHashSet<City>(visitedCities);
		newVisitedCities.add(road.targetCity);
		return new TourState(newVisitedCities, road.targetCity);
	}
	public boolean equals(Object that) {
		if (this == that) {return true;};
		if (that == null || that.getClass()!=this.getClass()) {return false;};
		TourState thats = (TourState) that;
		if (currentCity==thats.currentCity) {
			if (visitedCities.equals(thats.visitedCities)){
				return true;
			}
		};
		return false;
	}
}
Question 3
package search;

public interface Search {
	frontier Frontier = null;
	Node findSolution(State root, GoalTest goal);
	int numberGen();
	
}
package search;

public class TreeSearch implements Search {
	protected final frontier Frontier;
	int generated = 0;
	public TreeSearch(frontier f) {
		Frontier = f;
	};
	public Node findSolution(State root, GoalTest goal) {
		Frontier.add(new Node(null, null, root));
		while (!Frontier.isEmpty()) {
			Node node = Frontier.remove();
			if (goal.isGoal(node.state))
				return node;
			else {
				for (Action action : node.state.getApplicableActions()) {
					State newState = node.state.getActionResult(action);
					Frontier.add(new Node(node, action, newState));
					generated +=1;
				}
			}
		}
		return null;
	};
	public int numberGen() {
		return generated;
	}
	
}
package search;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.HashSet;

public class GraphSearch implements Search {
	protected final frontier Frontier;
	int generated = 1;
	protected Set<State> visited = new HashSet<State>();
	protected Set<State> onFrontier = new HashSet<State>();
	public GraphSearch(frontier f) {
		Frontier = f;
	};
	public Node findSolution(State root, GoalTest goal) {
		visited.clear();
		generated=1;
		Frontier.add(new Node(null, null, root));
		onFrontier.add(root);
		while (!Frontier.isEmpty()) {
			Node node = Frontier.remove();
			onFrontier.remove(node.state);
			boolean present = false;
			for (State s: visited) {
				present = present || s.equals(node.state);
			}
			if (!present) { visited.add(node.state);};	
			if (goal.isGoal(node.state))
				return node;
	
			for (Action action : node.state.getApplicableActions()) {
				State newState = node.state.getActionResult(action);
				boolean x = false;
				for (State s: visited) {
					x = x || s.equals(newState);
				}
				for (State s: onFrontier) {
					x = x || s.equals(newState);
				}
				if (!x) {
					Frontier.add(new Node(node, action, newState));
					onFrontier.add(newState);
					generated +=1;
				}
			}
		}
		return null;

	};
	public int numberGen() {
		return generated;
	}
	
}
Question 5
package npuzzle;

import search.TreeSearch;
import search.GraphSearch;
import search.BreadthFirstFrontier;
import search.DepthFirstFrontier;
import search.GoalTest;
import search.Node;
import search.frontier;
import search.Search;

public class lab1 {
	public static void main(String[] args) {
		System.out.println("This is a demonstration of breadth-first tree search on 8-puzzle");
		System.out.println();
		
		Tiles initialConfiguration = new Tiles(new int[][] {
			{ 7, 4, 2 },
			{ 8, 1, 3 },
			{ 5, 0, 6 }
		});
		GoalTest goalTest = new TilesGoalTest();
		frontier f = new BreadthFirstFrontier();
		Search s = new TreeSearch(f);		
		Node solution = s.findSolution(initialConfiguration, goalTest);
		
		new NPuzzlePrinting().printSolution(solution);
		System.out.println("Breadth First Tree Search");
		System.out.println(s.numberGen());
		System.out.println(f.maxStored());
		
		f.clear();
		s = new GraphSearch(f);
		solution = s.findSolution(initialConfiguration, goalTest);
		System.out.println("Breadth First Graph Search");
		System.out.println(s.numberGen());
		System.out.println(f.maxStored());
		
		f = new DepthFirstFrontier();
		s = new GraphSearch(f);
		solution = s.findSolution(initialConfiguration, goalTest);
		System.out.println("Depth First Graph Search");
		System.out.println(s.numberGen());
		System.out.println(f.maxStored());	
		
		f.clear();
		s = new TreeSearch(f);
		solution = s.findSolution(initialConfiguration, goalTest);
		System.out.println("Depth First Tree Search");
		System.out.println(s.numberGen());
		System.out.println(f.maxStored());	
	}
}
Output – 
Breadth First Tree Search
2298273
1473871
Breadth First Graph Search
4359
1592
Depth First Graph Search
34266
14480
Depth First Tree Search
Out of memory - infinite
