/**
 * 
 */
package net.willkeung.word.count;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author willkeung
 *
 */
public class BasicWordCountFileSaver extends AbstractWordCount {
	
	private static final Logger LOG = LoggerFactory.getLogger(BasicWordCountFileSaver.class);
	
	private final String dataDirectory;
	
	public BasicWordCountFileSaver() {
		this("");
	}
	
	public BasicWordCountFileSaver(String dataDirectory) {
		this.dataDirectory = dataDirectory;
	}

	/* (non-Javadoc)
	 * @see net.willkeung.word.count.WordCountere#saveAndCount(java.lang.String, java.io.InputStream)
	 */
	@Override
	public FileStatistic saveAndCount(String fileName, InputStream inStream) {
		if (fileName == null || fileName.length() == 0) {
			throw new IllegalArgumentException("filename cannot be null: " + 
					fileName);
		}
		verifyBaseDirAndCreate(this.dataDirectory);
		String fileUri = this.dataDirectory + "/" + fileName;
		FileOutputStream fileOutStream = null;
		FileStatistic fileStats = new FileStatistic(fileName);
		try {
			fileOutStream = new FileOutputStream(fileUri);
			fileStats = super.saveAndCount(fileName, inStream, 
					fileOutStream);
		} catch (FileNotFoundException e) {
			throw new WordCounterException("Could not write file to: " +
					fileUri, e);
		} finally {
			if (fileOutStream != null) {
				try {
					fileOutStream.flush();
					fileOutStream.close();
				} catch (IOException e) {
					// log exception and move on
					LOG.warn("Could not flush and close file at: " + fileUri, e);
				}
			}
		}
		return fileStats;
	}
	
	private void verifyBaseDirAndCreate(String dirPath) {
		File dirFile = new File(dirPath);
		if (!dirFile.exists()) {
			boolean result = dirFile.mkdirs();
			if (!result) {
				LOG.info("Could not create non-existing dir, {}", dirPath);
			}
		}
	}
	
}
