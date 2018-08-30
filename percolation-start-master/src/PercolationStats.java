import java.awt.*;
import java.util.*;

/**
 * Compute statistics on Percolation after performing T independent experiments on an N-by-N grid.
 * Compute 95% confidence interval for the percolation threshold, and  mean and std. deviation
 * Compute and print timings
 * 
 * @author Kevin Wayne
 * @author Jeff Forbes
 * @author Josh Hug
 * @author Samuel Chan
 */

public class PercolationStats {
	
	// Class variables
	public static int RANDOM_SEED = 1234;
	public static Random ourRandom = new Random(RANDOM_SEED);
	public static ArrayList<Point> list;
	public static int myT;
	public static int myN;
	public static double[] myTrials;

	// The constructor for the class that also runs the simulation T times, adding each probability to myTrials
	public PercolationStats (int N, int T){
		if (N <= 0 || T <= 0) {
			throw new IllegalArgumentException();
		}
		list = new ArrayList<Point>();
		for (int i = 0; i < N*N; i++) {
			list.add(new Point());
		}
		myT = T;
		myN = N;
		myTrials = new double[T];
		
		// Loop to run simulation T times
		for(int i = 0; i < T; i ++) {
			// Change the below to switch from QuickFind to QuickUWPC for PercolationUF
			//IUnionFind finder = new QuickFind(N);
			IUnionFind finder = new QuickUWPC(N);
			
			// Change the below to switch from PercolationDFS to Percolation DFSFast to PercolationUF
			//IPercolate perc = new PercolationDFS(N); 
			IPercolate perc = new PercolationUF(N, finder); 

			int counter = 0;
			// Assigns the correct column and row to each Point, which acts as the coordinates for the cell
			for (Point p : list) {
				int col = counter % myN;
				int row = (counter - col) / N; 
				p.setLocation(row, col);

				counter++;
			}
			// Shuffles the list to randomly open cells in the next loop
			Collections.shuffle(list, ourRandom);

			// Randomly opens cells and checks if system percolates
			// If it does, add that to myTrials
			for (Point p : list) {
				int row = (int) (p.getX());
				int col = (int) (p.getY());
				perc.open(row, col);
				if(perc.percolates()) {
					myTrials[i] = ((double) (perc.numberOfOpenSites()))/(N*N);
					break;
				}
			}
		}
	}

	// Calculates mean of the array of trial probabilities, using sum()
    public static double mean() {
        validateNotNull(myTrials);
        if (myTrials.length == 0) return Double.NaN;
        double sum = sum();
        return sum / myTrials.length;
    }
    
    // sums up all the probabilities from every trial
    private static double sum() {
        validateNotNull(myTrials);
        double sum = 0.0;
        for (int i = 0; i < myTrials.length; i++) {
            sum += myTrials[i];
        }
        return sum;
    }
    
    // Calculates stddev of the trials given variance
    public static double stddev() {
        validateNotNull(myTrials);
        return Math.sqrt(var());
    }
    // Calculates the variance of the trials given mean
    public static double var() {
        validateNotNull(myTrials);

        if (myTrials.length == 0) return Double.NaN;
        double avg = mean();
        double sum = 0.0;
        for (int i = 0; i < myTrials.length; i++) {
            sum += (myTrials[i] - avg) * (myTrials[i] - avg);
        }
        return sum / (myTrials.length - 1);
    }
    
    // Makes sure the object is not null
    private static void validateNotNull(Object x) {
        if (x == null)
            throw new IllegalArgumentException("argument is null");
    }
    
	// Calculates the lower bound of the 95% confidence interval
	public static double confidenceLow(){
		return (mean() - 1.96*stddev()/Math.sqrt((double) myT));
	}
	
	// Calculates the upper bound of the 95% confidence interval
	public static double confidenceHigh(){
		return (mean() + 1.96*stddev()/Math.sqrt((double) myT));
	}
	
	// Displays stats for the grid size, number of trials, mean, stddev, confidence interval,
	// and time it took the simulation to run
	public static void main(String[] args) {
		double start =  System.nanoTime();
		PercolationStats ps = new PercolationStats(150, 10);
		double end =  System.nanoTime();
		double time =  (end-start)/1e9;
		System.out.printf("Grid Size (N): %d, Number of Trials (T): %d", myN, myT);
		System.out.println();
		System.out.printf("Mean: %f, Stddev: %f, 95%% Confidence Interval: [%f, %f], Time: %f\n", 
				ps.mean(), stddev(), confidenceLow(), confidenceHigh(), time);

	}
}
