public class WordGram implements Comparable<WordGram>{

	private String[] myWords;
	private int myHash;	

	public WordGram(String[] words, int index, int size) {
		myWords = new String[size];
		System.arraycopy(words, index, myWords, 0, size);
		myHash = this.toString().hashCode();

	}
	
	public WordGram(WordGram wg) {
		myWords = wg.myWords;
		myHash = wg.toString().hashCode();
	}

	@Override
	public int hashCode() {
		return myHash;
	}

	@Override
	public String toString() {
		return String.join(" ", myWords);
	}

	@Override
	public boolean equals(Object other) { 

		if (other == null|| ! (other instanceof WordGram)) {
			return false;
		}

		WordGram wg = (WordGram) other;
		
		if(this.myWords.length!=wg.myWords.length) {
			return false;

		}

		for(int i = 0; i < this.myWords.length; i++) {
			if(!(this.myWords[i]).equals(wg.myWords[i])) {
				return false;
			}
		}
		
		return true;
	}

	@Override
	public int compareTo(WordGram wg) {
		if (wg.toString().equals(this.toString())) {
			return 0;
		}
		return this.toString().compareTo(wg.toString());
	}
	
	
	public int length() {
		return myWords.length;
	}


	public WordGram shiftAdd(String last) { 
		
		WordGram wg = new WordGram(this.myWords, 0, this.myWords.length);
		for (int i = 0; i< wg.myWords.length-1; i++) {
				wg.myWords[i] = this.myWords[i+1];
		}
		
		wg.myWords[this.myWords.length-1] = last;

		return wg; 

	}
}