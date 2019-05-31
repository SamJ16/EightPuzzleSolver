/*
 * Copyright 2017 Marc Liberatore.
 */

package puzzle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import search.SearchProblem;
import search.Searcher;

/**
 * A class to represent an instance of the eight-puzzle.
 * 
 * The spaces in an 8-puzzle are indexed as follows:
 * 
 * 0 | 1 | 2
 * --+---+---
 * 3 | 4 | 5
 * --+---+---
 * 6 | 7 | 8
 * 
 * The puzzle contains the eight numbers 1-8, and an empty space.
 * If we represent the empty space as 0, then the puzzle is solved
 * when the values in the puzzle are as follows:
 * 
 * 1 | 2 | 3
 * --+---+---
 * 4 | 5 | 6
 * --+---+---
 * 7 | 8 | 0
 * 
 * That is, when the space at index 0 contains value 1, the space 
 * at index 1 contains value 2, and so on.
 * 
 * From any given state, you can swap the empty space with a space 
 * adjacent to it (that is, above, below, left, or right of it,
 * without wrapping around).
 * 
 * For example, if the empty space is at index 2, you may swap
 * it with the value at index 1 or 5, but not any other index.
 * 
 * Only half of all possible puzzle states are solvable! See:
 * https://en.wikipedia.org/wiki/15_puzzle
 * for details.
 * 

 * @author liberato
 *
 */
public class EightPuzzle implements SearchProblem<List<Integer>> {
	final List<Integer> solution= Arrays.asList(new Integer[] {1, 2, 3, 4, 5, 6, 7, 8, 0});//state that is the goal
//	state of the eightpuzzle board is represented by an arraylist of all elements of the board
	private final List<Integer> start;
//	List<List<Integer>> states=new ArrayList<> ();list of all states, with each state being an arraylist view of the board
	
	/**
	 * Creates a new instance of the 8 puzzle with the given starting values.
	 * 
	 * The values are indexed as described above, and should contain exactly the
	 * nine integers from 0 to 8.
	 * 
	 * @param startingValues
	 *            the starting values, 0 -- 8
	 * @throws IllegalArgumentException
	 *             if startingValues is invalid
	 */
	public EightPuzzle(List<Integer> startingValues) throws IllegalArgumentException {
		if (startingValues.size()==9 && startingValues.contains(1) && startingValues.contains(2) && startingValues.contains(3) &&
		startingValues.contains(4) && startingValues.contains(5) && startingValues.contains(6) && startingValues.contains(7) &&
		startingValues.contains(8) && startingValues.contains(0)) {
			start=startingValues;
//			states.add(startingValues);
		}
		else {throw new IllegalArgumentException();}
	}

	@Override
	public List<Integer> getInitialState() {
		return start;
	}

	@Override
	public List<List<Integer>> getSuccessors(List<Integer> currentState) {
		List<List<Integer>> nextStates=new ArrayList<> ();//list of every possible future state
		
		int index=currentState.indexOf(0);//location of empty space
		
		int size=currentState.size();//size is square of a number because board is a square shape of dimensions number X number
		
		int h=(int)Math.sqrt(size);
		
		boolean U=index>=Math.sqrt(size);//means that you can swap up at least one level without IndexOutofBoundsException
		
		boolean D=index<size-Math.sqrt(size);//means that you can swap down at least one level without IndexOutofBoundsException
		
		boolean L=(index%Math.sqrt(size))!=0;//means that you can swap left at least one index without IndexOutofBoundsException
		
		boolean R=((index+1)%Math.sqrt(size))!=0;//means that you can swap right at least one index without IndexOutofBoundsException
		
		if (D&R&!U&!L) {//can swap down or right but not left or up (empty space is top left corner)
			List<Integer> a=new ArrayList<> (currentState);
			Collections.swap(a, index, (int) (index+h));//swap down
			nextStates.add(a);
			List<Integer> b=new ArrayList<> (currentState);
			Collections.swap(b, index, (int) (index+1));//swap right
			nextStates.add(b);
		}
		
		if (D&L&!U&!R) {//can swap down or left but not up or right (empty space is top right corner)
			List<Integer> a=new ArrayList<> (currentState);
			Collections.swap(a, index, (int) (index+h));//swap down
			nextStates.add(a);
			List<Integer> b=new ArrayList<> (currentState);
			Collections.swap(b, index, (int) (index-1));//swap left
			nextStates.add(b);
		}
		
		if (U&R&!D&!L) {//can swap up or right but not down or left (empty space is bottom left corner)
			List<Integer> a=new ArrayList<> (currentState);
			Collections.swap(a, index, (int) (index-h));//swap up
			nextStates.add(a);
			List<Integer> b=new ArrayList<> (currentState);
			Collections.swap(b, index, (int) (index+1));//swap right
			nextStates.add(b);
		}
		
		if (U&L&!D&!R) {//can swap up or left but not down or right (empty space is bottom right corner)
			List<Integer> a=new ArrayList<> (currentState);
			Collections.swap(a, index, (int) (index-h));//swap up
			nextStates.add(a);
			List<Integer> b=new ArrayList<> (currentState);
			Collections.swap(b, index, (int) (index-1));//swap left
			nextStates.add(b);
		}
		
		if (D&L&R&!U) {//can swap down or left or right but not up (empty space is in top row)
			List<Integer> a=new ArrayList<> (currentState);
			Collections.swap(a, index, (int) (index+h));//swap down
			nextStates.add(a);
			List<Integer> b=new ArrayList<> (currentState);
			Collections.swap(b, index, (int) (index-1));//swap left
			nextStates.add(b);
			List<Integer> c=new ArrayList<> (currentState);
			Collections.swap(c, index, (int) (index+1));//swap right
			nextStates.add(c);
		}
		
		if (U&D&R&!L) {//can swap up or down or right but not left (empty space is in leftmost column)
			List<Integer> a=new ArrayList<> (currentState);
			Collections.swap(a, index, (int) (index+h));//swap down
			nextStates.add(a);
			List<Integer> b=new ArrayList<> (currentState);
			Collections.swap(b, index, (int) (index-h));//swap up
			nextStates.add(b);
			List<Integer> c=new ArrayList<> (currentState);
			Collections.swap(c, index, (int) (index+1));//swap right
			nextStates.add(c);
		}
		
		if (U&D&L&!R) {//can swap up or down or left but not right (empty space is in rightmost column)
			List<Integer> a=new ArrayList<> (currentState);
			Collections.swap(a, index, (int) (index+h));//swap down
			nextStates.add(a);
			List<Integer> b=new ArrayList<> (currentState);
			Collections.swap(b, index, (int) (index-h));//swap up
			nextStates.add(b);
			List<Integer> c=new ArrayList<> (currentState);
			Collections.swap(c, index, (int) (index-1));//swap left
			nextStates.add(c);
		}
		
		if (U&L&R&!D) {//can swap up or left or right but not down (empty space is bottom row)
			List<Integer> a=new ArrayList<> (currentState);
			Collections.swap(a, index, (int) (index-1));//swap left
			nextStates.add(a);
			List<Integer> b=new ArrayList<> (currentState);
			Collections.swap(b, index, (int) (index-h));//swap up
			nextStates.add(b);
			List<Integer> c=new ArrayList<> (currentState);
			Collections.swap(c, index, (int) (index+1));//swap right
			nextStates.add(c);
		}
		
		if (U&D&R&L) {//anywhere not along the edges of the board
			List<Integer> a=new ArrayList<> (currentState);
			Collections.swap(a, index, index+1);//swap right
			nextStates.add(a);
			List<Integer> b=new ArrayList<> (currentState);
			Collections.swap(b, index, index-1);//swap left
			nextStates.add(b);
			List<Integer> c=new ArrayList<> (currentState);
			Collections.swap(c, index, (int) (index+h));//swap down
			nextStates.add(c);
			List<Integer> d=new ArrayList<> (currentState);
			Collections.swap(d, index, (int) (index-h));//swap up
			nextStates.add(d);
		}
		return nextStates;
	}


	@Override
	public boolean isGoal(List<Integer> state) {
		return state.equals(solution);
	}

	public static void main(String[] args) {
//		EightPuzzle eightPuzzle = new EightPuzzle(Arrays.asList(new Integer[] {1, 2, 3, 4, 0, 6, 7, 5, 8 }));
//		Searcher<List<Integer>> eP=new Searcher<List<Integer>>(eightPuzzle);
//		List<List<Integer>> solution = new List<List<Integer>> (eP.findSolution());
//		for (List<Integer> state : solution) {
//			System.out.println(state);
//		}
//		System.out.println(solution.size() + " states in solution");
	}
}