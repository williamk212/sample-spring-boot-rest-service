/**
 * 
 */
package net.willkeung.word.count;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import static org.junit.Assert.*;
import net.willkeung.word.count.AbstractWordCount;
import net.willkeung.word.count.FileStatistic;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

/**
 * @author willkeung
 *
 */
public class AbstractWordCountTest {

	private static class ByteArrayWordCount extends AbstractWordCount {

		private ByteArrayOutputStream byteOutStream;
		
		ByteArrayWordCount() {
			byteOutStream = new ByteArrayOutputStream();
		}
		
		@Override
		public FileStatistic saveAndCount(String fileName, InputStream inStream) {
			FileStatistic fileStats = super.saveAndCount(fileName, inStream, 
					byteOutStream);
			try {
				byteOutStream.flush();
			} catch (IOException e) {
				throw new RuntimeException("Couldn't flush", e);
			}
			return fileStats;
		}
		
		public ByteArrayOutputStream getByteOutStream() {
			return this.byteOutStream;
		}
		
		public void reset() {
			this.byteOutStream.reset();
		}
		
	}
	
	@Test
	public void saveAndCount() {
		String phrase = "  The red fox jumped over the lazy dog the the.. ";
		ByteArrayInputStream byteInStream = new ByteArrayInputStream(phrase.getBytes());
		
		String fileName = "hello_world.txt";
		ByteArrayWordCount wordCountSvc = new ByteArrayWordCount();
		
		FileStatistic fileStats = wordCountSvc.saveAndCount(fileName, byteInStream);
		assertNotNull(fileStats);
		assertEquals(10, fileStats.getTotalWordCount());
		
		Map<String, Integer> wordCount = fileStats.getWordCount();
		assertEquals(3, wordCount.get("the").intValue());
		
		String savedResult = new String(wordCountSvc.getByteOutStream().toByteArray());
		assertEquals(phrase, savedResult);
	}
	
	@Test
	public void saveAndCount_negative() {
		String phrase = "           ";
		ByteArrayInputStream byteInStream = new ByteArrayInputStream(phrase.getBytes());
		
		String fileName = "hello_world.txt";
		ByteArrayWordCount wordCountSvc = new ByteArrayWordCount();
		
		FileStatistic fileStats = wordCountSvc.saveAndCount(fileName, byteInStream);
		assertNotNull(fileStats);
		
		assertEquals(0, fileStats.getTotalWordCount());
		
		Map<String, Integer> wordCount = fileStats.getWordCount();
		assertNull(wordCount.get("the"));
		
		String savedResult = new String(wordCountSvc.getByteOutStream().toByteArray());
		assertEquals(phrase, savedResult);
	}
	
	@Test
	public void saveAndCount_file() throws IOException {
		ClassPathResource resource = new ClassPathResource("sample-files/coding-exercise.txt");
		InputStream inStream = resource.getInputStream();
		
		String fileName = "hello_world.txt";
		ByteArrayWordCount wordCountSvc = new ByteArrayWordCount();
		
		FileStatistic fileStats = wordCountSvc.saveAndCount(fileName, inStream);
		assertNotNull(fileStats);
		
		assertEquals(176, fileStats.getTotalWordCount());
		
		Map<String, Integer> wordCount = fileStats.getWordCount();
		assertEquals(12, wordCount.get("the").intValue());
		
	}
	
}
