/**
 * 
 */
package net.willkeung.word.count;

import java.io.IOException;

import net.willkeung.RestTextApplication;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author willkeung
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RestTextApplication.class)
@WebAppConfiguration
@IntegrationTest({"server.port=8080"})
public class RestTextApplicationIT {
	
	private static final Logger LOG = LoggerFactory
			.getLogger(RestTextApplicationIT.class);
	
	private ObjectMapper objMapper = new ObjectMapper();
	private String baseUrl;
	private RestTemplate restTemplate;
	
	@Before
	public void setUp() {
		this.baseUrl = "http://localhost:8080/words";
		restTemplate = new TestRestTemplate();
	}

	@Test
	public void invokeCount() throws JsonParseException, JsonMappingException, IOException {
		MultiValueMap<String, Object> multiValueParts = new LinkedMultiValueMap<String, Object>();
		multiValueParts.add("file", 
				new ClassPathResource("sample-files/coding-exercise.txt"));
		
		ResponseEntity<String> response = restTemplate.postForEntity(this.baseUrl + 
				"/count", multiValueParts, String.class);
		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		String responseBody = response.getBody();
		LOG.debug("Response body: {}", responseBody);
		
		JsonNode rootNode = objMapper.readTree(responseBody);
		String filename = rootNode.path("filename").asText();
		assertEquals("coding-exercise.txt", filename);
		
		// totalWordCount
		int totalWordCount = rootNode.path("totalWordCount").asInt();
		assertEquals(176, totalWordCount);
	}
	
	@Test
	public void invokeCount_conflict() {
		MultiValueMap<String, Object> multiValueParts = new LinkedMultiValueMap<String, Object>();
		multiValueParts.add("file", 
				new ClassPathResource("sample-files/coding-exercise-con.txt"));
		
		ResponseEntity<String> response = restTemplate.postForEntity(this.baseUrl + 
				"/count", multiValueParts, String.class);
		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
		response = restTemplate.postForEntity(this.baseUrl + 
				"/count", multiValueParts, String.class);
		assertNotNull(response);
		assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
	}
	
	@Test
	public void invokeFilter() throws JsonProcessingException, IOException {
		String filename = "coding-exercise-blue.txt";
		MultiValueMap<String, Object> multiValueParts = new LinkedMultiValueMap<String, Object>();
		multiValueParts.add("file", 
				new ClassPathResource("sample-files/" + filename));
		
		ResponseEntity<String> response = restTemplate.postForEntity(this.baseUrl + 
				"/count", multiValueParts, String.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		String responseBody = response.getBody();
		
		JsonNode rootNode = objMapper.readTree(responseBody);
		String filenameResponse = rootNode.path("filename").asText();
		assertEquals(filename, filenameResponse);
		
		int totalWordCount = rootNode.path("totalWordCount").asInt();
		assertEquals(179, totalWordCount);
		
		response = restTemplate.getForEntity(this.baseUrl + "/" + filename + 
				"/filter?kw=blue", String.class);
		rootNode = objMapper.readTree(response.getBody());
		filenameResponse = rootNode.path("filename").asText();
		assertEquals(filename, filenameResponse);
		totalWordCount = rootNode.path("totalWordCount").asInt();
		assertEquals(172, totalWordCount);
		
		JsonNode wordCountNode = rootNode.path("wordCount");
		assertTrue(wordCountNode.has("should"));

		assertFalse(wordCountNode.has("blue"));
		assertFalse(wordCountNode.has("bluegrass"));
		assertFalse(wordCountNode.has("blueberry"));
		LOG.debug("blue node: [{}]", wordCountNode.path("blue"));
	
	}
	
	@Test
	public void invokeFiles() throws JsonProcessingException, IOException {
		String filename = "coding-exercise-A.txt";
		MultiValueMap<String, Object> multiValueParts = new LinkedMultiValueMap<String, Object>();
		multiValueParts.add("file", 
				new ClassPathResource("sample-files/" + filename));
		
		ResponseEntity<String> response = restTemplate.postForEntity(this.baseUrl + 
				"/count", multiValueParts, String.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());

		filename = "coding-exercise-B.txt";
		multiValueParts = new LinkedMultiValueMap<String, Object>();
		multiValueParts.add("file", new ClassPathResource("sample-files/" + filename));
		response = restTemplate.postForEntity(this.baseUrl + 
				"/count", multiValueParts, String.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
		response = restTemplate.getForEntity(this.baseUrl + "/files", String.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		LOG.debug("list of files: {}", response.getBody());
		JsonNode rootNode = objMapper.readTree(response.getBody());
		assertTrue(rootNode.isArray());
		
		assertTrue(rootNode.size() >= 2);
		boolean foundFileA = false;
		boolean foundFileB = false;
		for (JsonNode node: rootNode) {
			if (node.path("filename").asText().equals("coding-exercise-A.txt")) {
				foundFileA = true;
			} else if (node.path("filename").asText().equals("coding-exercise-B.txt")) {
				foundFileB = true;
			}
			if (foundFileA && foundFileB) break;
		}
		assertTrue(foundFileA && foundFileB);
	}
}
