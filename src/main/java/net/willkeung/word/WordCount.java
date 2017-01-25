/**
 * 
 */
package net.willkeung.word;

import java.util.HashMap;
import java.util.Map;

/**
 * @author willkeung
 *
 */
public class WordCount {
	
	public static final WordCount EMPTY_WORD_COUNT = new WordCount("", 0, 
			new HashMap<String, Integer>());
	
	private final String filename;
	private final int totalWordCount;
	private final Map<String, Integer> wordCount;
	
	public WordCount(String filename, int totalWordCount, 
			Map<String, Integer> wordCount) {
		this.filename = filename;
		this.totalWordCount = totalWordCount;
		this.wordCount = wordCount;
	}
	
	public int getTotalWordCount() {
		return totalWordCount;
	}

	public String getFilename() {
		return filename;
	}

	public Map<String, Integer> getWordCount() {
		return wordCount;
	}

}
