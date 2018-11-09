package F28DA_CW1;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Test;

public class ModificationsTest {

	@Test
	public void test1() throws WException {
		IWords dict = new HTableWords();
		
		try {
			dict.addWord("quick");
			dict.addWord("brown");
			dict.addWord("fox");
			dict.addWord("jumps");
		} catch (WException e) {
			fail("Error with linked list implementation");
		}

		IWords sugg = SpellChecker.suggestions("qiuck", dict);
		IWords sugg2 = SpellChecker.suggestions("bown", dict);
		IWords sugg3= SpellChecker.suggestions("fo", dict);
		IWords sugg4 = SpellChecker.suggestions("juumps", dict);
		
		assertTrue(sugg.wordExists("quick"));
		assertTrue(sugg2.wordExists("brown"));
		assertTrue(sugg3.wordExists("fox"));
		assertTrue(sugg4.wordExists("jumps"));
	}

	@Test
	public void test2() throws WException {
		IWords dict = new HTableWords();
		try {
			dict.addWord("cat");
			dict.addWord("on");
			dict.addWord("too");
		} catch(WException e) {
			fail("Error with Hash Table implementation");
		}
		
		
		
		IWords sugg = SpellChecker.suggestions("lat", dict);
		IWords sugg2 = SpellChecker.suggestions("to", dict);
		IWords sugg3= SpellChecker.suggestions("o", dict);
		IWords sugg4 = SpellChecker.suggestions("mat", dict);
		
		assertTrue(sugg.wordExists("cat"));
		assertTrue(sugg2.wordExists("too"));
		assertTrue(sugg3.wordExists("on"));
		assertTrue(sugg4.wordExists("cat"));
		
		Iterator<String> it = dict.allWords();
		assertTrue(it.hasNext());
	}
}
