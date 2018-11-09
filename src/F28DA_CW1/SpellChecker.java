package F28DA_CW1;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

/** Main class for the Spell-Checker program */
public class SpellChecker {

	/** Suggests word modifications for a given word and a given word dictionary. 
	 * @return A new dictionary with all the suggested corrections for the words
	 * @throws WException */
	
	static public IWords suggestions(String word, IWords dict) throws WException {
		
		String result = "";
		HTableWords dictionary = new HTableWords();
		WordModifications suggestions = new WordModifications(word, dict);
		
		suggestions.generateSuggestions();
		
		for(String entry : suggestions.getResult()) {
			if(result.equals("")) {
				result = entry;
			} else {
				result += ", " + entry;
			}
		}
		
			// Condition to filter out results which we couldn't find the suggestions for
			if(suggestions.getResult().size() > 0) {
				dictionary.addWord(result);
			}
		
		return dictionary;
	}

	/**
	 * Main method for the Spell-Checker program. The program takes two input
	 * filenames in the command line: the word dictionary file and the file
	 * containing the words to spell-check. .
	 */
	public static void main(String[] args) {
		if (args.length != 2) {
			System.err.println("Usage: SpellChecker dictionaryFile.txt inputFile.txt ");
			System.exit(1);
		}

		try {
			BufferedInputStream dict, file;
			HTableWords dictionary = new HTableWords();
			
			dict = new BufferedInputStream(new FileInputStream(args[0]));

			FileWordRead readDict = new FileWordRead(dict);
			
			long startTime = System.currentTimeMillis();
			
			while(readDict.hasNextWord()) {
				String word = readDict.nextWord().trim();
				if(!dictionary.wordExists(word)) {
					try {
						dictionary.addWord(word);
					} catch (WException e) {
						e.printStackTrace();
					}	
				}
			}
			
			dict.close();
			
			file = new BufferedInputStream(new FileInputStream(args[1]));

			FileWordRead readFile = new FileWordRead(file);
			
			// Read the words from misspelled words file
			while(readFile.hasNextWord()) {
				String word = readFile.nextWord();
				// If they are not in our dictionary, suggest corrections for them
				if(!dictionary.wordExists(word)) {
					Iterator<String> it;
					try {
						 it = suggestions(word, dictionary).allWords();
						while(it.hasNext()) {
							System.out.println(word + " => " + it.next());
						}
					} catch (WException e) {
						e.printStackTrace();
					}
					
					
				}
				
			}
			
			file.close();
			
			long endTime = System.currentTimeMillis();
			
			long finalTime = endTime - startTime;
			
			System.out.println("The program took " + finalTime + " milliseconds.");
			
		} catch (IOException e) { // catch exceptions caused by file input/output errors
			System.err.println("Missing input file, check your filenames");
			System.exit(1);
		} 
	}

}
