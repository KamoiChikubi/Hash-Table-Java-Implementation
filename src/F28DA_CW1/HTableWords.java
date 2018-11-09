package F28DA_CW1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/** References: https://stackoverflow.com/questions/52860647/double-hashed-hashtable-rehashing-problem
 * 
 * My own question on stackoverflow when I was stuck at double hashing. I ended up figuring out the 
 * problem myself, but the link is included above just for reference
 */

public class HTableWords implements IWords, IHashing, IMonitor {

	// Fields
	private int totalEntries, numberOfProbes, numberOfOperations, worstCollision;
	private float maxLoadFactor;
	private String[] hTable;

	// Constructor with max load factor specified
	public HTableWords(float maxLF) {
		maxLoadFactor = maxLF;
		hTable = new String[7];
		numberOfProbes = 0;
		numberOfOperations = 0;
		totalEntries = 0;
		worstCollision = 0;
	}
	
	// Default overloaded Constructor without parameter
		public HTableWords() {
			this((float) 0.5);
		}

	/**
	 * @return float: maximum allowed load factor before the hash table is rebuilt.
	 */
	
	@Override
	public float maxLoadFactor() {
		return (float) maxLoadFactor;
	}

	/**
	 * @return float: Current load factor of our hash table
	 */
	
	@Override
	public float loadFactor() {
		return (float) totalEntries/hTable.length;
	}

	/**
	 * @return float: Average number of probes
	 */
	
	@Override
	public float averageProbes() {
		return (float) numberOfProbes/numberOfOperations;
	}

	/**
	 * @param s: The word we need index for
	 * @return int: Compressed hash value which gives us the index to put the value in
	 * 
	 * We are using 33^hash + ascii code for the current letter for polynomial accumulation
	 * which is giving this program the minimum amount of collisions
	 */
	
	@Override
	public int giveCode(String s) {

		int hash = 0;
		
		for(int i = 0; i <s.length(); i++) {
			hash = hash*33 + s.charAt(i);
		}

		return Math.abs(hash % hTable.length);
	}

	/**
	 * 
	 * @param s: The string we need to double hash.
	 * @return int: Double hashed value
	 */
	
	private int doubleHash(String s) {
		return 7 - (giveCode(s) % 7);
	}
	
	/**
	 * 
	 * @param word: The word we need to add
	 * @void
	 * 
	 * This method is used to add a word from our hash table. If the word exist,
	 * we throw a WException, otherwise we use giveCode() method to get the 
	 * index for that word. If that index's value is null, we put that word there.
	 * Otherwise we use double hash algorithm to get steps to jump so we can find 
	 * a null value, and put the word there.
	 */
	
	@Override
	public void addWord(String word) throws WException {

		if(!wordExists(word)) {

			// If we reach the max load factor, we rebuild the hash table
			if(loadFactor() >= maxLoadFactor()) {
				rehash();
			}

			int index = giveCode(word);

			// If the index contains a word already, we use double hash until we find a new index
			if(hTable[index] != null) {
				int dHash = doubleHash(word);
				int newIndex = 0;
				
				/*
				 * We keep track of current number of probes to see if this iteration was our worst case.
				 * If it was the worst collision with max amount of probes, we know how many times we 
				 * probed in the worst case, and use that when searching for a word in case of collision.
				 */
				
				for(int i = 2; i < hTable.length; i++) {
					newIndex = (index + (i * dHash)) % hTable.length;
					numberOfProbes++;
					
					if(hTable[newIndex] == null) {
						if(worstCollision < i) {
							worstCollision = i;
						}
						hTable[newIndex] = word;
						break;
					}
				}
			} else {
				hTable[index] = word;
			}

			totalEntries++;
			numberOfOperations++;
		} else {
			throw new WException(word + " already exist!");
		}
	}

	/**
	 * @void
	 * @param word: The word we need to delete
	 * 
	 * This method is used to delete a word from our hash table. If the word doesn't 
	 * exist, we throw a WException, otherwise we use giveCode() method to get the 
	 * index for that word. If it is there, we change the value to null. Otherwise
	 * we use double hash algorithm to get steps to jump so we can find the value.
	 */
	
	@Override
	public void delWord(String word) throws WException {

		if(wordExists(word)) {
			int index = giveCode(word);

			if(!word.equals(hTable[index])) {
				int dHash = doubleHash(word);
				int newIndex = 0;

				for(int i = 2; i < worstCollision + 2; i++) {
					newIndex = (index + (i * dHash)) % hTable.length;
					numberOfProbes++;
					
					if(word.equals(hTable[newIndex])) {
						hTable[newIndex] = null;
						break;
					}
				}
			} else {
				hTable[index] = null;
			}

			totalEntries--;
			numberOfOperations++;
		} else {
			throw new WException(word + " doesn't exist!");
		}
	} 


	/*
	 * @param word: String
	 * @return Boolean
	 * 
	 * This method is used to search the hash table to locate the mentioned word in 
	 * our hash table.
	 * 
	 * It uses giveCode() method to get the index to search the word at. If the word is
	 * not there it return false.
	 * If there is another word at the index, we use double hash algorithm to get the step
	 * value. We jump indexes using that value to skip indexes and look for the word.
	 */

	@Override
	public boolean wordExists(String word) {

		int index = giveCode(word);

		if(word.equals(hTable[index])) {
			numberOfOperations++;
			return true;
		} else {
			
			int dHash = doubleHash(word);
			
			for(int i = 2; i < worstCollision + 2; i++) {
				int newIndex = (index + (i*dHash)) % hTable.length;
				numberOfProbes++;
				
				if(word.equals(hTable[newIndex])) {
					numberOfOperations++;
					return true;
				}
			}
		}

		numberOfOperations++;
		return false;
	}

	/*
	 * @return int: Total number of entries
	 * 
	 */

	@Override
	public int nbWords() {	
		return totalEntries;
	}

	/*
	 * @return All the entries of the hash table as an iterator object
	 * 
	 */

	@Override
	public Iterator<String> allWords() {

		ArrayList<String> l = new ArrayList<String>();

		for(int i = 0; i < hTable.length; i++) {
			if(hTable[i] != null)
				l.add(hTable[i]);
		}

		return l.iterator();
	}

	/**
	 * @void
	 * @throws WException
	 * 
	 * This method is used to resize the array whenever our load factor has reached the maximum
	 * allowed load factor. We add all the entries to a temporary array, increase the 
	 * size of our original array (we double the array size, and get the next closest prime
	 * number to that). Once our array is resized, we add all the elements back into it from
	 * the temporary array.
	 */

	private void rehash() throws WException {

		String[] tempTable = new String[nbWords()];

		// Add all the values to tempTable. We are keeping the j counter to avoid putting values into
		// random spots
		for(int i=0, j=0; i<hTable.length; i++) {
			if(hTable[i] != null) {
				tempTable[j] = hTable[i];
				j++;
			}
		}

		hTable = new String[getNextPrime((hTable.length)*2)];
		// Reset the original array values
		Arrays.fill(hTable, null);

		/*
		 *  Reset all the global field variables, since we are basically RE-making the table
		 *  so they will be back to their previous values when they are being added to the table.
		 */

		totalEntries = 0;
		numberOfProbes = 0;
		numberOfOperations = 0;

		// Add all the values back to original hTable array
		for (int i = 0; i < tempTable.length; i++) {
			if(tempTable[i] != null) {
				addWord(tempTable[i]);
			}
		}

	}


	// Reference: https://www.baeldung.com/java-generate-prime-numbers

	/**
	 * @param num: Whatever number is there, it will check it using the loop if it's prime or not
	 * @return Boolean
	 * 
	 * Helper method to determine whether or not our @param num is prime.
	 */

	private boolean isPrime(int num) {

	
		for(int i = 2; i*i <= num; i++) {
			if(num % i == 0) 
				return false;
		}

		return true;
	}

	/**
	 * 
	 * @param num: Which will be the tablesize*2
	 * @return int: The next prime number closest to new table size.
	 * 
	 * Method to get the prime number
	 * 
	 */

	private int getNextPrime(int num) {

		int prime = 0;

		for(int i = num; i <= num*2; i++) {
			if(isPrime(i))
				prime = i;
		}

		return prime;
	}

}