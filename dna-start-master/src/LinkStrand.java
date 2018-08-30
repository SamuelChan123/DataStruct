public class LinkStrand implements IDnaStrand {
	// Private node class
	private class Node {
		String info;
		Node next;
		public Node(String s) {
			info = s;
			next = null;
		}
	}
	// Instance variables
	private Node myFirst,myLast;
	private long mySize;
	private int myAppends;
	private int myIndex;
	private int myLocalIndex;
	private Node myCurrent;
	// default constructor with empty string
	public LinkStrand() {
		this("");
	}
	// When constructor called, initializes a new linkstrand
	public LinkStrand(String s) {
		initialize(s);
	}
	// Returns size
	@Override
	public long size() {
		return mySize;
	}

	// Initialize a new linkstrand with string source
	@Override
	public void initialize(String source) {
		myFirst = new Node(source);
		myLast = myFirst;
		mySize = source.length();
		
		myAppends = 0;		
		myLocalIndex = 0;
		myIndex = 0;
		myCurrent = myFirst;
	}

	// Get a new linkstrand with string source
	@Override
	public IDnaStrand getInstance(String source) {
		return new LinkStrand(source);
	}

	// Append string dna in a new node to the end of the current node
	@Override
	public IDnaStrand append(String dna) {
		myLast = myLast.next = new Node(dna);
		mySize += dna.length();
		myAppends++;
		return this;
	}

// reverse the order of the strand
	@Override
	public IDnaStrand reverse() {
		
		Node curS = myFirst;
		Node nextS = null;
		Node prevS = null;

		while(curS!=null){
			StringBuilder build = new StringBuilder("");
			build.append(curS.info);
			curS.info = build.reverse().toString();
			nextS = curS.next;
			curS.next = prevS;
			prevS = curS;
			curS = nextS;
		}
		
		curS = prevS;
		LinkStrand rev = new LinkStrand(curS.info);
		curS = curS.next;
		while (curS != null) {
			rev.append(curS.info);
			curS = curS.next;
		}
		return rev;
	}

	
	// returns number of times append has been called
	@Override
	public int getAppendCount() {
		return myAppends;
	}
	// String representation of the link strand object
	public String toString() {
		StringBuilder string = new StringBuilder("");
		Node first = myFirst;
		if(first == null) return null;
		while(first != null) {
			string.append(first.info);
			first = first.next;
		}
		return string.toString();
		
	}
	
	
	// Find the character at index specified, with O(1) runtime when O(n+1) is called after O(n)
	@Override
	public char charAt(int index) {
		if (index<0||index>size()-1) {
			throw new IndexOutOfBoundsException();
		}
		if (index > myIndex) {
			while (myIndex != index) {
				myIndex++;
				myLocalIndex++;
				if (myLocalIndex >= myCurrent.info.length()) {
					myLocalIndex = 0;
					myCurrent = myCurrent.next;
				}	
			}
			myIndex = index;
			return myCurrent.info.charAt(myLocalIndex);
		}
		else {
			int count = 0;
			int dex = 0;
			Node list = myFirst;
			while (count != index) {
				count++;
				dex++;
				if (dex >= list.info.length()) {
					dex = 0;
					list = list.next;
				}
			}					
			myCurrent = list;
			myLocalIndex = dex;
			myIndex = index;
			return list.info.charAt(dex);
		}

	}
	

	}




