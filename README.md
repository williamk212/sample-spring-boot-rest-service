 
# Sample Spring Boot REST Service - Word Count
## Description
author: William Keung

User can upload a text file to the service where a word count is perform and kept track of. The user can then execute REST queries to gather information on what the service has collected.

This coding exercise has three services defined:
* Word Count - Returns the total count of words and the count of each occurrence of each word after a user has uploaded a file
* File summary - returns details of all files that have been uploaded
* filter - Filters and removes a keyword from a file

## Dependencies and assumptions
This project assumes that the user has maven installed. This project was developed and tested under MacOSX.
[maven link](https://maven.apache.org)

I used the unix "curl" command to invoke the REST services

## Project build
To build and run, these instructions assume that the user is using the command line and in the home directory of the project.
To build, in the project home, run the command:

```
> mvn clean package
```

To run:

```
> mvn spring-boot:run
```

To invoke services, in another command line window, use "curl" to invoke various REST services:
* Word Count:
	> curl -F "file=@src/etc/coding-exercise-blue.txt" http://localhost:8080/words/count
* Filter service:
	> curl -X GET http://localhost:8080/words/coding-exercise-blue.txt/filter?kw=blue
* All files Summary:
	> curl -X GET http://localhost:8080/words/files
	
