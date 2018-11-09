package F28DA_CW1;

import java.util.ArrayList;

/**
 * @author Naeem Khan
 * 
 * This class is for doing word modifications for the SpellChecker class.
 * This will help avoid repetition and cluster.
 */

public class WordModifications {

	// Fields
	private String alphabets, word;
	private ArrayList<String> result;
	private IWords dict;
	private StringBuffer sb;

	/**
	 * 
	 * @param wordToCheck The word to be checked
	 * @param dictionary The hashtable/linkedlist set of words to be checked with
	 */
	
	public WordModifications(String wordToCheck, IWords dictionary) {
		alphabets = "abcdefghijklmnopqrstuvwxyz";
		word = wordToCheck;
		dict = dictionary;
		result = new ArrayList<String>();
	}
	
	/**
	 *
	 * @void
	 * 
	 * This method replaces letters in the word with every letter from the alphabet,
	 * one by one.
	 */
	
	private void letterSub() {
		
		for(int i = 0; i < word.length(); i++) {
			sb = new StringBuffer(word);
			
			for(int j = 0; j < alphabets.length(); j++) {
				// Delete a letter and replace it with another letter alphabetically
				sb.deleteCharAt(i);
				sb.insert(i, alphabets.charAt(j));
				
				// If the new word is in our dictionary, add it to the array list
				if(dict.wordExists(sb.toString()) && !isInList(sb.toString())) {
					result.add(sb.toString());
				}
			}
		}
	}

	/**
	 * 
	 * @void
	 * 
	 * This method swaps two letters in a word with each other
	 */
	
	private void letterReversal() {
		
		String[] sChar = new String[2];
		sb = new StringBuffer(word);
		String tempResult = "";
		
		for (int i = 0; i < word.length()-1; i++) {
			// Put the words to be swapped into each cell of this array
			sChar[0] = sb.substring(i+1, i+2);
			sChar[1] = sb.substring(i, i+1);
			// Combine the characters and each part of word to get a new word
			tempResult = sb.substring(0, i) + sChar[0] + sChar[1] + sb.substring(i+2, word.length());

			if(dict.wordExists(tempResult) && !isInList(sb.toString())) {
				result.add(tempResult);
			}
		}
	}
	
	/**
	 * 
	 * @void
	 * 
	 * Simply delete a letter from the word one by one and see if it is
	 * a possible word from our dictionary
	 */
	
	private void letterOmit() {
		
		for(int i = 0; i <= sb.length(); i++) {
			sb = new StringBuffer(word);	
			sb.deleteCharAt(i);
			
			if(dict.wordExists(sb.toString()) && !isInList(sb.toString())) { 
				result.add(sb.toString());
			}
		}
	}
	
	/**
	 * 
	 * @void
	 * 
	 * Insert letters alphabetically in each index of the word
	 */
	
	private void letterInsert() {
		
		for(int i = 0; i <= word.length(); i++) {
			for(int j = 0; j < alphabets.length(); j++) {
				sb = new StringBuffer(word);
				sb.insert(i, alphabets.charAt(j));
				
				if(dict.wordExists(sb.toString()) && !isInList(sb.toString())) {
					result.add(sb.toString());
				}
			}
		}
	}
	
	/**
	 * 
	 * @void
	 * 
	 * This method will do all the modifications. We use this here instead of making
	 * all the methods public to void cluster in main class as much as possible
	 */
	
	public void generateSuggestions() {
		letterSub();
		letterReversal();
		letterOmit();
		letterInsert();
	}

	/**
	 * 
	 * @return All the stored possible corrections of the words
	 * 
	 */
	
	public ArrayList<String> getResult() {
		return result;
	}
	
	/**
	 * 
	 * @param s: Check if this word is in the list
	 * @return Boolean.
	 */
	private boolean isInList(String s) {
		
		for(String entry : result) {
			if(entry.equals(s))
				return true;
		}
		
		return false;
	}
	
}
