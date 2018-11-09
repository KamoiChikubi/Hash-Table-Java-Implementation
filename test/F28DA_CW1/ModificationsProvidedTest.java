package F28DA_CW1;

import static org.junit.Assert.*;

import org.junit.Test;

public class ModificationsProvidedTest {

	@Test
	public void testOmission() throws WException {

		IWords dict = new LListWords();
		try {
			dict.addWord("cats");
			dict.addWord("like");
			dict.addWord("on");
			dict.addWord("of");
			dict.addWord("to");
			dict.addWord("play");
		} catch (WException e) {
			fail("Error with linked list implementation");
		}
		IWords sugg = SpellChecker.suggestions("catts", dict);
		assertTrue(sugg.wordExists("cats"));
	}

}
