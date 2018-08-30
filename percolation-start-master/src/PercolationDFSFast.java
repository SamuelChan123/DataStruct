import java.util.*;

/**
 * Simulate percolation thresholds for a grid-base system using a
 * quicker algorithm based on PercolationDFS for determining if the top of
 * a grid is connected to the bottom of a grid.
 * Instead of scanning the entire top row, PercolationDFSFast recursively checks 
 * whether one cell is connected to another open cell, and if it connects to the top,
 * the original cell is full 
 * 
 * @author Samuel Chan
 */

public class PercolationDFSFast extends PercolationDFS {
	// Constructor that is essentially the same as PercolationDFS
	public PercolationDFSFast(int n) {
		super(n);
	}

	@Override
	// Uses PercolationDFS's isOpen method, but checks whether the cell is in bounds
	public boolean isOpen(int row, int col) {
		if (!inBounds(row,col)) {
			throw new IndexOutOfBoundsException("Index out of bounds");
		}
		return super.isOpen(row,col);
	}
	
	// Gets the index in the grid for the row, col
	public int getIndex(int row, int col) {
		return row*col + col;
	}

	// Checks to see whether the cell is full, first making sure it is in bounds
	@Override 
	public boolean isFull(int row, int col) {
		if (! inBounds(row,col)) {
			throw new IndexOutOfBoundsException("Index out of bounds");
		}
		return super.isFull(row, col);
	}
	
	// Called upon open, checks if any of the four surrounding cells are full, if so, the cell itself is full
	// Recursively does this for any open cells surrounding it
	@Override 
	protected void updateOnOpen(int row, int col) {
		if (! inBounds(row,col) || ! isOpen(row,col)) {
			return;
		}
		
		if (row == 0) {
			myGrid[row][col] = FULL;
		}
		
		int[] rowEl = {0, 1, -1, 0};
		int[] colEl = {1, 0, 0, -1};
		
		for(int d = 0; d < rowEl.length; d++) {
			int nr = row + rowEl[d];
			int nc = col + colEl[d];

			if (inBounds(nr, nc)) {
				if (myGrid[nr][nc] == FULL) {
					myGrid[row][col] = FULL;	
				}
			}
		}
		
		if(myGrid[row][col] == FULL) {
			for(int d = 0; d < rowEl.length; d++) {
				int nr = row + rowEl[d];
				int nc = col + colEl[d];
				if (inBounds(nr, nc) && isOpen(nr,nc)) {
					if (myGrid[nr][nc] != FULL) {
						updateOnOpen(nr, nc);	
					}
				}
			}
		}
		

		
	
	}

	// Opens cell using PercolationDFS's open method, but checks whether it's in bound first
	@Override 
	public void open(int row, int col) { 
		if (! inBounds(row,col)) {
			throw new IndexOutOfBoundsException();
		}
		super.open(row, col);
	}
}
