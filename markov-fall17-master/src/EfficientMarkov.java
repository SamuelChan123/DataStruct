import java.util.*;

public class EfficientMarkov extends MarkovModel {

	Map<String, ArrayList<String>> myMap;

	public EfficientMarkov(int order) {
		super(order);
		myMap = new HashMap<String, ArrayList<String>>();
	}
	
	@Override
	public void setTraining(String text) {
		myText = text;
		myMap.clear();	

		int index = 0;

		while (index < myText.length()-myOrder) {
			ArrayList<String> next = new ArrayList<String>();
			if(myMap.get(myText.substring(index, index+myOrder)) != null) {
				next = myMap.get(myText.substring(index, index+myOrder));
			}
			next.add(myText.substring(index+myOrder, index+myOrder+1));
			myMap.put(myText.substring(index, index+myOrder), next);

			index++;
		}
		ArrayList<String> next = new ArrayList<String>();
		String lastKey = new String(myText.substring(index, index+myOrder));
		if (!myMap.containsKey(lastKey)) {
			myMap.put(lastKey, next);
		}
		next = myMap.get(lastKey);
		next.add(PSEUDO_EOS);
		myMap.put(lastKey, next);
	}

	@Override
	public ArrayList<String> getFollows(String key) throws NoSuchElementException{
		if(myMap.get(key)!=null) {
			return myMap.get(key);
		}
		ArrayList<String> list=new ArrayList<String>();
		list.add(PSEUDO_EOS);
		return list;
	}
}
