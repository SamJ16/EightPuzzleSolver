package search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;

/**
 * An implementation of a Searcher that performs an iterative search,
 * storing the list of next states in a Queue. This results in a
 * breadth-first search.
 * 
 */
public class QueueBasedBreadthFirstSearcher<T> extends Searcher<T> {
	
	protected ArrayList<Vertex<T>> verts;
	protected boolean[][] adjMat;
	Graph<T> g=new Graph(verts, adjMat);

	public QueueBasedBreadthFirstSearcher(SearchProblem<T> searchProblem) {
		super(searchProblem);
	}

	@Override
	public List<T> findSolution() {
		T start=searchProblem.getInitialState();
		Queue<T> frontier = new LinkedList<>();
		  frontier.add(start);


		  Map<T, T> predecessor = new HashMap<>();
		  predecessor.put(start, null);

		  List<T> path = new ArrayList<>();

		  while (!frontier.isEmpty()) {
		    T current = frontier.remove();
		    for (T next : searchProblem.getSuccessors(current)) {
		      if (!predecessor.containsKey(next)) {
		        frontier.add(next);
		        predecessor.put(next, current);
		      }
		    }
		    if (searchProblem.isGoal(current)) {
		      path.add(current);
		      T previous = predecessor.get(current);
		      while (previous != null) {
		        path.add(0, previous);
		        previous = predecessor.get(previous);
		      }
		      break;
		    }
		  }
		  List<T> l= new ArrayList<> (path);
		  return l;
	}
}

class Vertex<T> {
	public T data;
	public boolean visited;
	public Vertex<T> parent;
	public Vertex() {
		data = null; visited = false; parent=null;
	}
	public Vertex(T _data) {
		data = _data; visited = false; parent=null;
	}
	public String toString() {
		return data.toString();
	}
	/* Declare any additional vertex attribute you may need */
}

class Graph<T> {
	// array of vertices
	protected ArrayList<Vertex<T>> verts;

	/* 2D array representing adjacency matrix of an unweighted graph.
	 * If adjMat[i][j] is true, there is an edge from vertex i to j;
	 * otherwise the element adjMat[i][j] is false.
	 */
	protected boolean[][] adjMat;
	
	public Graph(ArrayList<Vertex<T>> _verts, boolean[][] _mat) {
		/* check the validity of input parameters */
		int nverts = _verts.size();
		// adjacency matrix must be square matrix of NxN
		if(_mat.length != nverts) return;
		for(int i=0;i<nverts;i++) {
			if(_mat[i].length != nverts) return;
		}
		verts = _verts;
		adjMat = _mat;
	}
	
	public int numVerts() { return verts.size(); }

	// Return the next unvisited neighbor of a given vertex
	protected int getNextUnvisitedNeighbor (int vid) {
		for (int j=0; j<numVerts(); j++) {
			if(adjMat[vid][j] && verts.get(j).visited == false)  return j;
		}
	    return -1;    // return index -1 if none found
	}
	
	// Print out the graph, including vertex list and adjacency matrix
	public void print() {
		int nverts = numVerts();
		System.out.println("Vertex List:");
		for(int i=0;i<nverts;i++) {
			System.out.println(i+": "+verts.get(i)+" (valence: "+valence(i)+")");
		}
		System.out.println("Adjacency Matrix:");
		for(int i=0;i<nverts;i++) {
			for(int j=0;j<nverts;j++) {
				System.out.print(adjMat[i][j]?"1 ":"0 ");
			}
			System.out.println("");
		}
	}
	
	/* TODO: Make this a complete graph. All you need to do is to
	 * modify the adjMat according to the definition of a complete
	 * graph, that is, there is an edge between every two vertices,
	 * but there is no self-loop (no vertex is connected to itself).
	 */
	public void completeGraph() {
		for (int i=0; i<numVerts(); i++) {
			for (int j=0; j<numVerts(); j++) {
				if (j==i) {adjMat[i][j]=false;}
				else {adjMat[i][j]=true;}
			}
		}
	}
	
	/* TODO: Return the number of neighbors (i.e. valence) of a given vertex */
	public int valence(int vid) {
		int nC=0;
		for (int i=0; i<numVerts(); i++) {
			if (adjMat[vid][i]) {
				nC++;
			}
		}
		return nC;
	}
	
	/* TODO: Perform DFS from start vertex, and print out all vertices visited.
	 * When printing vertices, use System.out.print(verts.get(index).data+" ");
	 * to create a space between every two vertices.
	 */
	public void DFS(int start) {
		for(int i=0;i<numVerts();i++) verts.get(i).visited=false;	// clears flags
		Stack<Integer> stack = new Stack<Integer>();	// create stack
		stack.push(start);
		System.out.print(verts.get(start).data.toString()+" ");
		verts.get(start).visited=true;
		while (!stack.isEmpty()) {
			int index=getNextUnvisitedNeighbor(stack.peek());
			if (index==-1) {stack.pop();}
			else {
				verts.get(index).visited=true;
				System.out.print(verts.get(index).data.toString()+" ");
				stack.push(index);
			}
		}
	}
	
	/* TODO: find the path from start vertex to end vertex using BFS.
	 * If the path exists, return the path in an Arraylist, where the
	 * first element must be start, and the last element must be end,
	 * and the intermediate vertices must be the indices of vertices
	 * that are on the path from start to end.
	 * If the path doesn't exist, return null.
	 */
	public ArrayList<Integer> findPathBFS(int start, int end) {
		for(int i=0;i<numVerts();i++) verts.get(i).visited=false;	// clear flags
		Queue<Integer> queue = new LinkedList<Integer>();	// create queue
		ArrayList<Integer> pathIndices=new ArrayList<Integer> ();
		queue.add(start);
		verts.get(start).visited=true;
		int neighbor=0;
		while (!queue.isEmpty()) {
			int curr=queue.remove();
			while ((neighbor=getNextUnvisitedNeighbor(curr))!=-1) {
				queue.add(neighbor);
				verts.get(neighbor).visited=true;
				verts.get(neighbor).parent=verts.get(curr);
			}
			if (curr==end) {
				pathIndices.add(end);
				Vertex<T> parent=verts.get(curr).parent;
				while (parent!=null) {
					pathIndices.add(verts.indexOf(parent));
					parent=parent.parent;
				}
				for (int i=0; i<pathIndices.size()/2; i++) {
					int i0=pathIndices.get(i);
					int i1=pathIndices.get(pathIndices.size()-(i+1));
					pathIndices.set(i, i1);
					pathIndices.set(pathIndices.size()-(i+1), i0);
				}
				return pathIndices;
			}
		}
		return null;
	}
}
