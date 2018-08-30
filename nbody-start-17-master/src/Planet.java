public class Planet {

	double myXPos;            // current x position
	double myYPos;            // current y position
	double myXVel;            // current velocity in x direction 
	double myYVel;            // current velocity in y direction
	double myMass;            // mass of planet
	String myFileName;   
	public Planet(double xp, double yp, double xv,
			double yv, double mass, String filename) {
		myXPos = xp;            
		myYPos = yp;          
		myXVel = xv;           
		myYVel = yv;            
		myMass = mass;            
		myFileName = filename;
	}
	public Planet(Planet p) {
		myXPos = p.myXPos;
		myYPos = p.myYPos;
		myXVel = p.myXVel;
		myYVel = p.myYVel;
		myMass = p.myMass;
		myFileName = p.myFileName;		
	}
	
	public double calcDistance(Planet p) {
		double distance = Math.sqrt(Math.pow(this.myXPos-p.myXPos, 2) + Math.pow(this.myYPos-p.myYPos, 2)); 
		return distance;
	}
	
	public double calcForceExertedBy(Planet p) {
		double force = ((6.67e-11)*this.myMass*p.myMass)/(Math.pow(calcDistance(p),2));
		return force;
	}
	
	public double calcForceExertedByX(Planet p) {
		double force = (calcForceExertedBy(p)*(p.myXPos-this.myXPos)/calcDistance(p));
		return force;
	}
	
	public double calcForceExertedByY(Planet p) {
		double force = (calcForceExertedBy(p)*(p.myYPos-this.myYPos)/calcDistance(p));
		return force;
	}

	public double calcNetForceExertedByX(Planet[] planets) {
		double sum = 0;
		for (Planet p : planets) {
			if (! p.equals(this)) {
				sum += calcForceExertedByX(p);
			}
		}
		return sum;
	}
	
	public double calcNetForceExertedByY(Planet[] planets) {
		double sum = 0;
		for (Planet p : planets) {
			if (! p.equals(this)) {
				sum += calcForceExertedByY(p);
			}
		}
		return sum;
	}
	
	public void update(double seconds, double xforce, double yforce) {
		double accelX = xforce / this.myMass;
		double accelY= yforce / this.myMass;
		this.myXVel = this.myXVel + seconds * accelX;
		this.myYVel = this.myYVel + seconds * accelY;
		this.myXPos = this.myXPos + seconds * this.myXVel;
		this.myYPos = this.myYPos + seconds * this.myYVel;
	}
	
	public void draw() {
		StdDraw.picture(myXPos, myYPos, "images/"+myFileName);
	}
}