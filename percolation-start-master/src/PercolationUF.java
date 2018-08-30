import java.util.Arrays;

/**
 * Simulate percolation thresholds for a grid-base system using a
 * union-find algorithm for determining if the top of
 * a grid is connected to the bottom of a grid.
 * @author Samuel Chan
 */

public class PercolationUF implements IPercolate{
	
	// Instance variables for the class
	IUnionFind myFinder;
	int myOpenSites;
	protected boolean[][] myGrid;
	private final int VTOP;
	private final int VBOTTOM;
	public final boolean OPEN = true;
	public final boolean BLOCKED = false;
	
	// Constructor that constructs the grid & fills it with BLOCKED
	public PercolationUF(int size, IUnionFind finder){
		finder.initialize(size*size+2);
		myFinder = finder;
		VTOP = size*size;
		VBOTTOM = size*size+1;
		myOpenSites = 0;
		myGrid = new boolean[size][size];
		for (boolean[] row : myGrid)
			Arrays.fill(row, BLOCKED);
	}
	
	// Opens a site when it is called, then calls updateOnOpen
	@Override
	public void open(int row, int col) {
		if (!inBounds(row,col)) {
			throw new IndexOutOfBoundsException("Index out of bounds");
		}
		if(isOpen(row, col)) {
			return;
		}
		myOpenSites++;
		myGrid[row][col] = OPEN;
		updateOnOpen(row,col);
		
 	}
	
	//Returns index of grid given row and col
	public int getIndex(int row, int col) {
		if(!inBounds(row, col)) {
			throw new IndexOutOfBoundsException("Index out of bounds");
		}
		return row*myGrid.length + col;
	}

	// Checks whether cell is in bounds
	protected boolean inBounds(int row, int col) {
		if (row < 0 || row >= myGrid.length) return false;
		if (col < 0 || col >= myGrid[0].length) return false;
		return true;

	}
	
	// Checks whether a cell is open
	@Override
	public boolean isOpen(int row, int col) {
		if (!inBounds(row,col)) {
			throw new IndexOutOfBoundsException("Index out of bounds");
		}
		return myGrid[row][col] != BLOCKED;
	}

	// Checks to see whether a cell is full by calling connected(current, VTOP)
	@Override
	public boolean isFull(int row, int col) {
		if (!inBounds(row,col) && isOpen(row, col)) {
			throw new IndexOutOfBoundsException("Index out of bounds");
		}
		return myFinder.connected(getIndex(row, col), VTOP);

	}

	// Checks to see whether the system percolates by seeing if VBOTTOM connects to the VTOP
	@Override
	public boolean percolates() {
		return myFinder.connected(VBOTTOM, VTOP);
	}

	// Returns number of open sites
	@Override
	public int numberOfOpenSites() {
		return myOpenSites;
	}
	
	// Checks the four cells surrounding a given row and col, and unions it (if they are in first or last row, 
	// union it to VTOP and VBOTTOM respectively
	protected void updateOnOpen(int row, int col) {
		if (! inBounds(row,col) || ! isOpen(row,col)) {
			return;
		}
		//System.out.println(row + "col " + col);

		int[] rowEl = {0, 1, -1, 0};
		int[] colEl = {1, 0, 0, -1};
		
		for(int d = 0; d < rowEl.length; d++) {
			int nr = row + rowEl[d];
			int nc = col + colEl[d];
			if(nr == -1 && myGrid[row][col] != BLOCKED) {
				myFinder.union(getIndex(row, col), VTOP);
			}
			if (nr == myGrid.length && myGrid[row][col] != BLOCKED) {
				myFinder.union(VBOTTOM, getIndex(row, col));
			}
			if(inBounds(nr, nc) && isOpen(nr,nc)) { 
				myFinder.union(getIndex(row, col), getIndex(nr,nc));
			}

		}

	}
	
	
}
