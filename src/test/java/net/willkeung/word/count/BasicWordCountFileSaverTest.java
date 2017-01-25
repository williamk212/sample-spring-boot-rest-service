/**
 * 
 */
package net.willkeung.word.count;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.Assert.*;
import net.willkeung.word.count.BasicWordCountFileSaver;
import net.willkeung.word.count.FileStatistic;

import org.junit.Test;

/**
 * @author willkeung
 *
 */
public class BasicWordCountFileSaverTest {

	private static final String dataBaseDir = "target/store";
	
	@Test
	public void saveAndCount() throws IOException {
		String phrase = "  The red fox jumped over the lazy dog the the.. ";
		ByteArrayInputStream byteInStream = new ByteArrayInputStream(phrase.getBytes());
		String fileName = "hello_fox.txt";
		
		BasicWordCountFileSaver wordCountSvc = new BasicWordCountFileSaver(dataBaseDir);
		FileStatistic fileStats = wordCountSvc.saveAndCount(fileName, byteInStream);
		
		assertNotNull(fileStats);
		assertEquals(10, fileStats.getTotalWordCount());
		
		File savedFile = new File(dataBaseDir + "/" + fileName);
		assertTrue(savedFile.exists());
		assertTrue(savedFile.isFile());
		
		// Read file to confirm contents are exact
		FileInputStream fis = new FileInputStream(savedFile);
		BufferedInputStream bis = new BufferedInputStream(fis);
		ByteArrayOutputStream resultBytes = new ByteArrayOutputStream();
		int byteInt;
		try {
			while ( (byteInt = bis.read()) != -1) {
				resultBytes.write(byteInt);
			}			
		} finally {
			bis.close();
			fis.close();
		}
		assertEquals(phrase, new String(resultBytes.toByteArray()));
	}

}
