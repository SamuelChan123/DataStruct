public class NLLH {
	public static void main(String[] args) {
		// This is the DNA strand with filler of * and enzyme of C
		String create = testString(10, "C", "*");
		
		// Create the IDnaStrand Objects for each of the three classes
		IDnaStrand stringbuildstrand = new StringBuilderStrand(create);
		IDnaStrand stringstrand = new StringStrand(create);
		IDnaStrand linkstrand = new LinkStrand("*");
				
		String splicee = "xxxxxxxxxxxxxxxxxxxxxxxxx"; // splicee of size 50
		//System.out.println(splicee.length());
		
		// Every iteration, double the splicee length and keep break distance constant
		 /*for (int k = 0; k < 15; k++) { 
			double start = System.nanoTime();
			//stringbuildstrand.cutAndSplice("C", splicee);
			//stringstrand.cutAndSplice("C", splicee);
			linkstrand.cutAndSplice("C", splicee);
			double end = System.nanoTime();
			splicee += splicee;
			//System.out.print("Splicee Length: " + splicee.length() + " Runtime: ");
			System.out.println((end-start)/1e9);
		} */
		 
		splicee = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"; // splicee of size 50
		// Every iteration, vary b, or the break distance
		
		for (int k = 1024; k >= 1; k /= 2) { 
			//String init = testString((int) k, "C", "*");
			System.out.println("num of breaks: " + (100000/k));
			//IDnaStrand build = new StringBuilderStrand(init);
			//IDnaStrand strand = new StringStrand(init);
			double sum = 0;
			for (int i = 0; i < 100; i ++) {
				IDnaStrand link = new LinkStrand("*");
				testStringLinked((int) k, "C", "*", link);
				double start = System.nanoTime();
				link.cutAndSplice("C", splicee);
				double end = System.nanoTime();
				sum += (end - start) /1e9;
			}
			double avg = sum / 100;
			System.out.println(avg);
		}

	}
	// Create a new dna strand
	public static String testString(int mod, String enzyme, String filler) {
		if (mod <= 0) {
			throw new IndexOutOfBoundsException();
		}
		String s = "";
		for(int i = 0; i < 100000; i++) { // Loop through to create a string of size 100000 characters with the enzyme every mod characters
			if( i % mod == 0) {
				s = s + enzyme; // This is the enzyme to be added every mod characters
			}
			else {
				s = s + filler; // This is the filler character
			}
		}
		return s;
	}
	
	// Create a new DNA strand for linkstrand class
	public static String testStringLinked(int mod, String enzyme, String filler, IDnaStrand link) {
		if (mod <= 0) {
			throw new IndexOutOfBoundsException();
		}
		String s = "";
		for(int i = 0; i < 100000; i++) { // Loop through to create a string of size 100000 characters with the enzyme every mod characters
			if( i % mod == 0) {
				link.append(enzyme); // This is the enzyme to be added every mod characters
			}
			else {
				link.append(filler); // This is the filler character
			}
		}
		return s;
	}
}
