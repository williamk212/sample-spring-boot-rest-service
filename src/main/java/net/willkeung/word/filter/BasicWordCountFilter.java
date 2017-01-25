/**
 * 
 */
package net.willkeung.word.filter;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.willkeung.word.WordCount;
import net.willkeung.word.count.FileStatistic;

/**
 * @author willkeung
 *
 */
public class BasicWordCountFilter implements WordCountFilter {
	
	private static final Logger LOG = LoggerFactory.getLogger(BasicWordCountFilter.class);

	/* (non-Javadoc)
	 * @see net.willkeung.word.filter.WordCountFilter#filterKeyword(java.lang.String, net.willkeung.word.count.FileStatistic)
	 */
	@Override
	public WordCount filterKeyword(String word, FileStatistic fileStats) {
		if (word == null || word.isEmpty() || fileStats == null) {
			LOG.debug("No word to filter on. Returning the whole set.");
			return new WordCount(fileStats.getFileName(), 
					fileStats.getTotalWordCount(), fileStats.getWordCount());
		}
		final String lowerCaseFilter = word.toLowerCase();		
		// can't use stream/filter. need to calculate total word
		int updatedTotalWordCount = fileStats.getTotalWordCount();
		Map<String, Integer> updatedWordCount = new HashMap<String, Integer>();
		for (Entry<String, Integer> keyValue: fileStats.getWordCount().entrySet()) {
			if (keyValue.getKey().toLowerCase().contains(lowerCaseFilter)) {
				updatedTotalWordCount -= keyValue.getValue().intValue();
			} else {
				updatedWordCount.put(keyValue.getKey(), keyValue.getValue());
			}
		}
		return new WordCount(fileStats.getFileName(), updatedTotalWordCount, 
				updatedWordCount);
	}

}
