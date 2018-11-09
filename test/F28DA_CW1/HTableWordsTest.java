package F28DA_CW1;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Test;

public class HTableWordsTest {

	@Test
	public void individualWordsTest() throws WException {
		HTableWords2_PEER3 h = new HTableWords2_PEER3();
		
		h.addWord("hi");
		h.addWord("my");
		h.addWord("name");
		h.addWord("is");
		h.addWord("xxx");
		
		assertEquals(5, h.nbWords());
		
		assertTrue(h.wordExists("xxx"));
		
		h.delWord("name");
		
		assertEquals(4, h.nbWords());
		assertFalse(h.wordExists("name"));
		
	}	
	
	@Test
	public void massOperationTest() throws WException {
		
		HTableWords2_PEER3 h = new HTableWords2_PEER3();
		String word = "";
		
		for (int i = 0; i < 200; i++) {
			word = "word" + i;
			h.addWord(word);
		}
		
		assertEquals(200, h.nbWords());
		
		for (int i = 0; i < 200; i++) {
			word = "word" + i;
			h.delWord(word);
		}
		
		assertEquals(0, h.nbWords());
	}

}
