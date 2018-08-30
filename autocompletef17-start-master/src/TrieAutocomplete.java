import static org.junit.Assert.assertArrayEquals;

import java.util.*;

/**
 * General trie/priority queue algorithm for implementing Autocompletor
 * 
 * @author Austin Lu
 * @author Jeff Forbes
 */
public class TrieAutocomplete implements Autocompletor {

	/**
	 * Root of entire trie
	 */
	protected Node myRoot;

	/**
	 * Constructor method for TrieAutocomplete. Should initialize the trie
	 * rooted at myRoot, as well as add all nodes necessary to represent the
	 * words in terms.
	 * 
	 * @param terms
	 *            - The words we will autocomplete from
	 * @param weights
	 *            - Their weights, such that terms[i] has weight weights[i].
	 * @throws NullPointerException
	 *             if either argument is null
	 * @throws IllegalArgumentException
	 *             if terms and weights are different weight
	 */
	public TrieAutocomplete(String[] terms, double[] weights) {
		if (terms == null || weights == null) {
			throw new NullPointerException("One or more arguments null");
		}
		
		// Represent the root as a dummy/placeholder node
		myRoot = new Node('-', null, 0);

		for (int i = 0; i < terms.length; i++) {
			add(terms[i], weights[i]);
		}
	}
	
	

	/**
	 * Add the word with given weight to the trie. If word already exists in the
	 * trie, no new nodes should be created, but the weight of word should be
	 * updated.
	 * 
	 * In adding a word, this method should do the following: Create any
	 * necessary intermediate nodes if they do not exist. Update the
	 * subtreeMaxWeight of all nodes in the path from root to the node
	 * representing word. Set the value of myWord, myWeight, isWord, and
	 * mySubtreeMaxWeight of the node corresponding to the added word to the
	 * correct values
	 * 
	 * @throws a
	 *             NullPointerException if word is null
	 * @throws an
	 *             IllegalArgumentException if weight is negative.
	 */
	private void add(String word, double weight) {
		if (word == null) {
			throw new NullPointerException();
		}
		if (weight < 0) {
			throw new IllegalArgumentException();
		}
				
		Node current = myRoot;
		for(int k=0; k < word.length(); k++){
		  char ch = word.charAt(k);
		  if (current.children.get(ch) == null) {
		    current.children.put(ch, new Node(ch, current, weight));
		  }
		  if (weight > current.mySubtreeMaxWeight && current.children.get(ch) != null)
			  current.mySubtreeMaxWeight = weight;
		  current = current.children.get(ch);
		}	
		current.isWord = true;
		current.myWeight = weight;
		current.mySubtreeMaxWeight = weight;
		current.myWord = word;
		
	}

	/**
	 * Required by the Autocompletor interface. Returns an array containing the
	 * k words in the trie with the largest weight which match the given prefix,
	 * in descending weight order. If less than k words exist matching the given
	 * prefix (including if no words exist), then the array instead contains all
	 * those words. e.g. If terms is {air:3, bat:2, bell:4, boy:1}, then
	 * topKMatches("b", 2) should return {"bell", "bat"}, but topKMatches("a",
	 * 2) should return {"air"}
	 * 
	 * @param prefix
	 *            - A prefix which all returned words must start with
	 * @param k
	 *            - The (maximum) number of words to be returned
	 * @return An Iterable of the k words with the largest weights among all
	 *         words starting with prefix, in descending weight order. If less
	 *         than k such words exist, return all those words. If no such words
	 *         exist, return an empty Iterable
	 * @throws a
	 *             NullPointerException if prefix is null
	 */
	public Iterable<String> topMatches(String prefix, int k) {
		if (k == 0) {
			return new LinkedList<String>();
		}
		if(prefix == null) {
			throw new NullPointerException();
		}
		
		if(myRoot.children == null) {
			return new LinkedList<String>();
		}
		
        Comparator<Node> comparator = new Node.ReverseSubtreeMaxWeightComparator();
        Comparator<Term> comp = new Term.ReverseWeightOrder();

		PriorityQueue<Node> npq = new PriorityQueue<Node>(k, comparator);
		PriorityQueue<Term> tpq = new PriorityQueue<Term>(k, comp);
		
		if (npq.size() > k) 
			npq.remove();
		Node current = myRoot;
		for(int i=0; i < prefix.length(); i++){
			if(!current.children.containsKey(prefix.charAt(i))) {
				return new LinkedList<String>();
			}
		    current = current.children.get(prefix.charAt(i));
		}		
		npq.add(current);
		while (npq.size() > 0) {
			
			if(tpq.size()!=0 && npq.size()!=0 && tpq.size() == k) {
				if(tpq.peek().getWeight() > npq.peek().mySubtreeMaxWeight) {
					break;
				}
			}
			current = npq.remove();

			if (current.isWord) {
				if(tpq.size() < k) {
				tpq.add(new Term(current.getWord(), current.getWeight()));
				}
			}
			

			for(Node n : current.children.values()) {
				npq.add(n);
			}
		}
		
		if(tpq.isEmpty()) {
			return new LinkedList<String>();
		}
		LinkedList<String> l = new LinkedList<String>();

		if(tpq.size() < k) {
			while(tpq.size() > 0) {
				l.add(tpq.poll().getWord());
			}
		}
		else {
			while(l.size() < k) {
				l.add(tpq.poll().getWord());
			}
		}
		return l;
		
	}
	

	/**
	 * Given a prefix, returns the largest-weight word in the trie starting with
	 * that prefix.
	 * 
	 * @param prefix
	 *            - the prefix the returned word should start with
	 * @return The word from with the largest weight starting with prefix, or an
	 *         empty string if none exists
	 * @throws a
	 *             NullPointerException if the prefix is null
	 */
	
	public String topMatch(String prefix) {
		String w = "";
		if(prefix == null) {
			throw new NullPointerException();
		}
		
		Node current = myRoot;
		for(int i=0; i < prefix.length(); i++){
			if(!current.children.containsKey(prefix.charAt(i))) {
				return "";
			}
		    current = current.children.get(prefix.charAt(i));
		}		
		if(current.isWord == true && current.myWeight == current.mySubtreeMaxWeight) {
			return current.getWord();
		}
		String ch = "";
		while (current.isWord == false) {
			for (Node n : current.children.values()) {
				if (n.mySubtreeMaxWeight == current.mySubtreeMaxWeight) {
					ch = n.myInfo;
				}
			}
			current = current.children.get(ch.charAt(0));
		}
		if (current.isWord == true) {
			w = current.myWord;
		}
		return w; 

	}

	
	/**
	 * Return the weight of a given term. If term is not in the dictionary,
	 * return 0.0
	 */
	public double weightOf(String term) {
		if (term == null) {
			throw new NullPointerException();
		}
		Node current = myRoot;
		for(int i=0; i < term.length(); i++){
			if(!current.children.containsKey(term.charAt(i))) {
				return 0.0;
			}
		    current = current.children.get(term.charAt(i));
		}	
		return current.getWeight();
	}
}
