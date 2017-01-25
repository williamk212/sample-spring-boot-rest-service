/**
 * 
 */
package net.willkeung;

import net.willkeung.word.count.BasicWordCountFileSaver;
import net.willkeung.word.count.WordCounter;
import net.willkeung.word.filter.BasicWordCountFilter;
import net.willkeung.word.filter.WordCountFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author willkeung
 *
 */
@Configuration
public class RestTextServiceConfiguration {

	public static final String DEFAULT_FILE_STORE_PATH = "target/store";
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@Bean
	public WordCounter wordCountSaveService() {
		return new BasicWordCountFileSaver(DEFAULT_FILE_STORE_PATH);
	}
	
	@Bean
	public WordCountFilter wordCountFilter() {
		return new BasicWordCountFilter();
	}
}
