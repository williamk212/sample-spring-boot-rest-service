/**
 * 
 */
package net.willkeung.word.count;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.lang3.CharUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author willkeung
 *
 */
public abstract class AbstractWordCount implements WordCounter {

	private static final Logger LOG = LoggerFactory.getLogger(AbstractWordCount.class);
	
	public static final int DEFAULT_WORD_BUFFER_SIZE = 7;
	
	protected FileStatistic saveAndCount(String fileName, InputStream inStream,
			OutputStream outStream) {
		if (fileName == null || fileName.length() == 0) {
			throw new IllegalArgumentException("filename cannot be null: " + 
					fileName);
		}
		FileStatistic fileStats = new FileStatistic(fileName);
		BufferedInputStream bis = new BufferedInputStream(inStream);
		int byteInt;
		try {
			boolean beginWord = false;
			ByteArrayOutputStream wordOutStream = 
					new ByteArrayOutputStream(DEFAULT_WORD_BUFFER_SIZE);
			while ( (byteInt = bis.read()) != -1) {
				if (CharUtils.isAsciiAlphanumeric((char)byteInt)) {
					if ( !beginWord ) {
						// found beginning of word
						beginWord = true;
					}
					wordOutStream.write(byteInt);
				}				
				if (beginWord && !CharUtils.isAsciiAlphanumeric((char)byteInt) ) {
					// found end of word
					wordOutStream.flush();
					beginWord = false;
					String foundWord = new String(wordOutStream.toByteArray());
					fileStats.addWord(foundWord);
					wordOutStream.reset();
					if (LOG.isTraceEnabled()) {
						LOG.trace("Found word [{}], current count: {}", foundWord,
								fileStats.getTotalWordCount());
					}
				}				
				outStream.write(byteInt);
			}
		} catch (IOException ex) {
			throw new WordCounterException("Could not read file: " +
					fileName, ex);
		}
		return fileStats;	
	}

}
