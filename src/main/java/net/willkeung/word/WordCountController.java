/**
 * 
 */
package net.willkeung.word;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.willkeung.word.count.FileStatistic;
import net.willkeung.word.count.WordCounter;
import net.willkeung.word.filter.WordCountFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author willkeung
 *
 */
@RestController
@RequestMapping(value = "/words")
public class WordCountController {
	
	private static final Logger LOG = LoggerFactory.getLogger(WordCountController.class);

	private Map<String, FileStatistic> receivedFiles; 
	
	@Autowired
	private WordCounter wordCountSaveService;
	
	@Autowired
	private WordCountFilter wordCountFilter;
	
	public WordCountController() {
		this.receivedFiles = new ConcurrentHashMap<String, FileStatistic>();
	}
	
	@RequestMapping(value = "/count", method=RequestMethod.POST)
	public WordCount count(@RequestParam("file") MultipartFile file) {
		if (file == null) {
			LOG.info("Received file is null");
			return WordCount.EMPTY_WORD_COUNT;
		}
		String fileName = file.getOriginalFilename();
		LOG.debug("filename: [{}]", fileName);
		if (this.receivedFiles.containsKey(fileName)) {
			throw new ResourceConflictException("Resource conflict - resource: " + 
					fileName + ", already exists.");
		}
		FileStatistic fileStats = null;
		WordCount wordCount = WordCount.EMPTY_WORD_COUNT;
		try {
			
			fileStats = this.wordCountSaveService.saveAndCount(
					fileName, file.getInputStream());
			this.receivedFiles.put(fileName, fileStats);
			wordCount = new WordCount(fileStats.getFileName(), 
					fileStats.getTotalWordCount(), fileStats.getWordCount());
		} catch (IOException ex) {
			throw new WordCountControllerException("Unable to obtain " + 
					"inputStream from upload", ex);
		}
		return wordCount;
	}

	@RequestMapping(value = "/files", method=RequestMethod.GET) 
	public WordCount[] getAllFileStatistics() {
		List<WordCount> wordCountResults = new ArrayList<WordCount>();
		this.receivedFiles.values().forEach(fileStats -> {
			WordCount wordCount = new WordCount(fileStats.getFileName(), 
					fileStats.getTotalWordCount(), fileStats.getWordCount());
			wordCountResults.add(wordCount);
		});
		WordCount[] allFileStats = wordCountResults.toArray(new WordCount[0]);
		return allFileStats;
	}
	
	@RequestMapping(value = "{filename}/filter", method=RequestMethod.GET) 
	public WordCount filter(@PathVariable String filename, 
							@RequestParam("kw") String filterWord) {
		if (filterWord == null || filterWord.isEmpty()) {
			return WordCount.EMPTY_WORD_COUNT;
		}
		
		FileStatistic fileStats = this.receivedFiles.get(filename);
		if (fileStats == null) {
			throw new ResourceNotFoundException(filename + 
					" does not exist.");
		}
		WordCount wordCount = this.wordCountFilter.filterKeyword(filterWord, fileStats);
		if (wordCount == null) {
			return new WordCount(filename, 0, new HashMap<String, Integer>());
		}
		return wordCount;
	}
}
