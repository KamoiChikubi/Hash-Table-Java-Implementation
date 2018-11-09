package F28DA_CW1;

import java.util.Iterator;
import java.util.LinkedList;

public class LListWords implements IWords {

	// Fields
	private LinkedList<String> lList;
	
	// Constructor
	public LListWords() {
		lList = new LinkedList<String>();
	}

	/**
	 * @void Adds the word to the list
	 * @throws WException: If word is already in the list
	 */
	
	@Override
	public void addWord(String word) throws WException {

		if(lList.contains(word))
			throw new WException("Word already exist!");
		
		lList.add(word);
		
	}

	/**
	 * 
	 *  @void Deletes a word from the list
	 *  @throws WException: If word is not in the list
	 */
	
	@Override
	public void delWord(String word) throws WException {

		if(lList.contains(word))
			lList.remove(word);
		else
			throw new WException("Word doesn't exist!");
		
	}

	/**
	 * 
	 * @void Method to check if word exist in our list
	 */
	@Override
	public boolean wordExists(String word) {
		
		if(lList.contains(word))
			return true;
		
		return false;
	}

	/**
	 * 
	 * @return Size of the list
	 */
	
	@Override
	public int nbWords() {
		return lList.size();
	}

	/**
	 * 
	 * @return Iterator of the list
	 */
	
	@Override
	public Iterator<String> allWords() {
		return lList.iterator();
	}

}
