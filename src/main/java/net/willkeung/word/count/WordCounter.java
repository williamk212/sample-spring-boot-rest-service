/**
 * 
 */
package net.willkeung.word.count;

import java.io.InputStream;

/**
 * @author willkeung
 *
 */
public interface WordCounter {

	FileStatistic saveAndCount(String fileName, InputStream inStream);
}
