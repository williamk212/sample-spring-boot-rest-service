/**
 * 
 */
package net.willkeung.word.count;

import static org.junit.Assert.*;
import net.willkeung.word.count.FileStatistic;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author willkeung
 *
 */
public class FileStatisticTest {
	
	private static final Logger LOG = LoggerFactory.getLogger(FileStatisticTest.class);

	@Test
	public void getWordCount() {
		String name = "hello.txt";
		
		FileStatistic fs = new FileStatistic(name);
		String word = "hello";
		fs.addWord(word);
		word = "hello";
		fs.addWord(word);
		assertEquals(2, fs.getWordCount(word));
		
		word = " hello   ";
		fs.addWord(word);
		assertEquals(3, fs.getWordCount(word));
		
		word = null;
		fs.addWord(word);
		assertEquals(3, fs.getWordCount("hello"));
		assertEquals(3, fs.getTotalWordCount());
	}
	
	@Test
	public void getSizeInBytes() {
		String name = "hello.txt";
		
		FileStatistic fs = new FileStatistic(name);
		String word = "hello";
		fs.addWord(word);
		assertEquals(word.getBytes().length, fs.getSizeInBytes());
		assertEquals(1, fs.getWordCount(word));
	}
	
	@Test
	public void getSizeInBytes_withSpace() {
		String name = "hello.txt";
		
		FileStatistic fs = new FileStatistic(name);
		String word = " hello ";
		fs.addWord(word);
		assertEquals(word.getBytes().length, fs.getSizeInBytes());
		assertEquals(1, fs.getWordCount(word));
	}
	
	@Test
	public void getSizeInBytes_onlySpace() {
		String name = "hello.txt";
		
		FileStatistic fs = new FileStatistic(name);
		String word = "     ";
		fs.addWord(word);
		int byteSize = word.getBytes().length;
		LOG.debug("String byte size: {}", byteSize);	
		assertEquals(byteSize, fs.getSizeInBytes());
		assertEquals(0, fs.getWordCount(word));
	}
}
