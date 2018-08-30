import java.util.*;

public class EfficientWordMarkov extends WordMarkovModel {

	Map<WordGram, ArrayList<String>> myMap;

	public EfficientWordMarkov(int order){
		super(order);
		myMap = new HashMap<WordGram, ArrayList<String>>();
	}

	@Override
	public void setTraining(String text) {
		myWords = text.split("\\s+");
		myMap.clear();

		for (int i = 0; i <= myWords.length - myOrder; i++) {
			if (i == myWords.length-myOrder) {
				WordGram lastKey = new WordGram(myWords, i, myOrder);
				if (!myMap.containsKey(lastKey)) {
					myMap.put(lastKey, new ArrayList<String>());
				}
				myMap.get(lastKey).add(PSEUDO_EOS);
			} else {
				WordGram wg = new WordGram(myWords, i, myOrder);
				if(!myMap.containsKey(wg)) {
					myMap.put(wg, new ArrayList<String>());
				}
				myMap.get(wg).add(myWords[i+myOrder]);
			}
		}	
	}
	

	@Override
	public ArrayList<String> getFollows(WordGram key) throws NoSuchElementException {
		WordGram wg = new WordGram(key);
		if(myMap.get(wg) != null) {
			return myMap.get(wg);
		}
		ArrayList<String> list=new ArrayList<String>();
		list.add(PSEUDO_EOS);

		return list;  
	}
}


