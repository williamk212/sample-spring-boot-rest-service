/**
 * 
 */
package net.willkeung.word.count;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author willkeung
 *
 */
public class FileStatistic {

	private final String fileName;
	private final Map<String, AtomicInteger> wordCountDict;
	private AtomicInteger	totalWordCount;
	private AtomicInteger	sizeInBytes;
	
	public FileStatistic(String fileName) {
		this.fileName = fileName;
		this.sizeInBytes = new AtomicInteger(0);
		this.totalWordCount = new AtomicInteger(0);
		this.wordCountDict = new HashMap<String, AtomicInteger>();
	}

	public String getFileName() {
		return fileName;
	}
	
	public int getTotalWordCount() {
		return this.totalWordCount.intValue();
	}

	public Map<String, Integer> getWordCount() {
		Map<String, Integer> convertedMap = new HashMap<String, Integer>();
		this.wordCountDict.forEach( (key, value) -> {
			convertedMap.put(key, value.intValue());
		});
		return Collections.unmodifiableMap(convertedMap);
	}

	public int getSizeInBytes() {
		return this.sizeInBytes.intValue();
	}

	public void addWord(String word) {
		if (word == null || word.length() == 0) {
			return;
		}
		String trimmedWord = word.trim();
		if (trimmedWord.length() > 0) {
			// word present
			if (!this.wordCountDict.containsKey(trimmedWord)) {
				this.wordCountDict.put(trimmedWord, new AtomicInteger(1));
			} else {
				AtomicInteger count = this.wordCountDict.get(trimmedWord);
				count.incrementAndGet();
			}
			this.totalWordCount.incrementAndGet();
		}
		sizeInBytes.addAndGet(word.length());
	}
	
	public int getWordCount(String word) {
		if (word == null || word.trim().length() == 0) {
			return 0;
		}
		AtomicInteger count = this.wordCountDict.get(word.trim());
		return count == null ? 0 : count.intValue();
	}
}
