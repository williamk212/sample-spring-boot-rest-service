/**
 * 
 */
package net.willkeung.word.filter;

import static org.junit.Assert.*;
import net.willkeung.word.WordCount;
import net.willkeung.word.count.FileStatistic;
import net.willkeung.word.filter.BasicWordCountFilter;

import org.junit.Test;

/**
 * @author willkeung
 *
 */
public class BasicWordCountFilterTest {

	private static FileStatistic buildSampleFileStatistic() {
		FileStatistic fileStats = new FileStatistic("dummy_file.txt");
		fileStats.addWord("Hello");
		fileStats.addWord("World");
		fileStats.addWord("the");
		fileStats.addWord("the");
		fileStats.addWord("the");
		
		fileStats.addWord("the");
		fileStats.addWord("the");
		fileStats.addWord("World");
		fileStats.addWord("Hello");
		fileStats.addWord("Hello");
		
		fileStats.addWord("the");
		return fileStats;
	}
	
	@Test
	public void filterKeyword() {
		FileStatistic fileStats = buildSampleFileStatistic();
		fileStats.addWord("blue");
		fileStats.addWord("blue");
		fileStats.addWord("bluegrass");
		fileStats.addWord("blueberry");
		fileStats.addWord("blueberry");
		
		BasicWordCountFilter wordFilter = new BasicWordCountFilter();
		String filterWord = "blue";
		
		assertEquals(16, fileStats.getTotalWordCount());
		
		assertEquals(2, fileStats.getWordCount("blue"));
		assertEquals(2, fileStats.getWordCount("blueberry"));
		assertEquals(1, fileStats.getWordCount("bluegrass"));
		WordCount wordCount = wordFilter.filterKeyword(filterWord, fileStats);
		
		assertEquals(11, wordCount.getTotalWordCount());
		assertNull(wordCount.getWordCount().get("blue"));
		assertNull(wordCount.getWordCount().get("blueberry"));
		assertNull(wordCount.getWordCount().get("bluegrass"));
	}
}
