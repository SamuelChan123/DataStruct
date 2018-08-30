import java.io.*;
import java.util.*;

public class NBody {
	
	public static void main(String[] args){
		StdAudio.loop("data/music.wav");
		double totalTime = 100000000000.0;
		double dt = 25000000.0;
		String pfile = "data/planets.txt";
		if (args.length > 2) {
			totalTime = Double.parseDouble(args[0]);
			dt = Double.parseDouble(args[1]);
			pfile = args[2];
		}
		
		String fname= "./data/planets.txt";
		Planet[] planets = readPlanets(pfile);
		double radius = readRadius(pfile);

		
		StdDraw.setScale(-radius, radius);
		StdDraw.picture(0,0,"images/starfield.jpg");
		for (Planet planet : planets) {
			planet.draw();
		}
		
		for(double t = 0.0; t < totalTime; t += dt) {
			int counter = 0;
			for (Planet planet : planets) {
				double[] xForces = new double[planets.length];
				double[] yForces = new double[planets.length];
				xForces[counter] = planet.calcNetForceExertedByX(planets);
				yForces[counter] = planet.calcNetForceExertedByY(planets);
				
				planet.update(t, xForces[counter], yForces[counter]);
		
				counter++;
			}
			
			
			StdDraw.picture(0,0,"images/starfield.jpg");
			for (Planet planet : planets) {
				planet.draw();
			}
			StdDraw.show(50);
		}
		
		 
		System.out.printf("%d\n", planets.length);
		System.out.printf("%.2e\n", radius);
		for (int i = 0; i < planets.length; i++) {
		    System.out.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
		                      planets[i].myXPos, planets[i].myYPos, 
		                      planets[i].myXVel, planets[i].myYVel, 
		                      planets[i].myMass, planets[i].myFileName);	
		}
	}
	
	public static double readRadius(String fname) {
		double value = 0;
		try {
			Scanner scan = new Scanner(new File(fname));
			double numofPlanets = scan.nextInt();
			value = scan.nextDouble();
			scan.close();
		} catch (FileNotFoundException e) {
			System.out.println("No file found!");
			System.exit(0);
		}
		return value;

	}
	
	public static Planet[] readPlanets(String fname) {

		try {
			Scanner scan = new Scanner(new File(fname));
			int numofPlanets = scan.nextInt();
			double scale = scan.nextDouble();
			
			int current = 0;
			Planet[] planets = new Planet[numofPlanets];
			while(current < numofPlanets) {
				double x1 = scan.nextDouble();
				double x2 = scan.nextDouble();
				double x3 = scan.nextDouble();
				double x4 = scan.nextDouble();
				double x5 = scan.nextDouble();
				String x6 = scan.next();
				Planet pl = new Planet(x1,x2,x3,x4,x5,x6);
				planets[current] = pl;
				current++;
			}
			scan.close(); 
			return planets;
		} catch (FileNotFoundException e) {
		      System.exit(0);
		}
		return null;
	}

}


