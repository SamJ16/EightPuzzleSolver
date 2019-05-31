package search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * An implementation of a Searcher that performs an iterative search,
 * storing the list of next states in a Stack. This results in a
 * depth-first search.
 * 
 */
public class StackBasedDepthFirstSearcher<T> extends Searcher<T> {
	
	public StackBasedDepthFirstSearcher(SearchProblem<T> searchProblem) {
		super(searchProblem);
	}

	@Override
	public List<T> findSolution() {
		T start=searchProblem.getInitialState();
		Stack<T> frontier = new Stack<>();
		  frontier.add(start);


		  Map<T, T> predecessor = new HashMap<>();
		  predecessor.put(start, null);

		  List<T> path = new ArrayList<>();

		  while (!frontier.isEmpty()) {
		    T current = frontier.pop();
		    for (T next : searchProblem.getSuccessors(current)) {
		      if (!predecessor.containsKey(next)) {
		        frontier.push(next);
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
