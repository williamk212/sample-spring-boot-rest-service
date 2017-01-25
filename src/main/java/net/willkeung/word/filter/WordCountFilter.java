/**
 * 
 */
package net.willkeung.word.filter;

import net.willkeung.word.WordCount;
import net.willkeung.word.count.FileStatistic;

/**
 * @author willkeung
 *
 */
public interface WordCountFilter {

	WordCount filterKeyword(String word, FileStatistic fileStats);
}
