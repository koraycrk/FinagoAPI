package com.koray.finago.middleware;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import com.koray.finago.helpers.CSVHelper;
import com.koray.finago.models.Language;
import com.koray.finago.models.ResponseMessage;
import com.koray.finago.service.CsvParse;
import com.koray.finago.service.Synonym;
import com.koray.finago.service.Translate;

public class DictionaryMid {
	
	public  ResponseEntity execute(MultipartFile file, Model model, Translate translate, CsvParse csvParse, Synonym synonym) {
		
		ResponseEntity<ResponseMessage> result = null;
		List<String> wordsInEnglish = new ArrayList<String>();
		boolean finished = false;
		String message = "";
		
			if (CSVHelper.hasCSVFormat(file)) {
			    try {
			    	csvParse.csvToEmployees(file, model);
			    	List<String> wordsInTurkish = (List<String>) model.getAttribute("words");
			    	for (String string : wordsInTurkish) {
			    		wordsInEnglish.add(translate.execute(string, Language.TURKISH, Language.ENGLISH));
					}
			    	
			    	if(wordsInEnglish.size() > 0) {
			    		
			    		List synonymsOfWord1 = synonym.execute(wordsInEnglish.get(0));
			    		for (int i = 1; i < wordsInEnglish.size(); i++) {
			    			List synonymsOfTheWord = synonym.execute(wordsInEnglish.get(i));
			    			boolean exist = synonymsOfWord1.stream().anyMatch(element ->   synonymsOfTheWord.contains(element));
			    			if(!exist) {
			    				wordsInTurkish.remove(i);
			    			}
						}
			    		
			    		List<String[]> dataLines = new ArrayList<>();
			    		List<String> csvLines =  new ArrayList<>();
			    		for (String string : wordsInTurkish) {
			    			csvLines.add(string);
						}
			    		csvLines.add(wordsInEnglish.get(0));
			    		dataLines.add((String[]) csvLines.toArray());
			    		givenDataArray_whenConvertToCSV_thenOutputCreated(dataLines);
			    	}
			    	
			    	
			        message = "Uploaded the file successfully: " + file.getOriginalFilename();
			        result = ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
			        finished = true;
			    } catch (Exception e) {
			        message = "Could not upload the file: " + file.getOriginalFilename() + "!";
			        message += e;
			        result = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ResponseMessage(message));
			        finished = true;
			    }
			}
			if (!CSVHelper.hasCSVFormat(file)){

			    message = "File is empty!";
			    result = ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage(message));
			}
			if (!finished) {
			    message = "Please upload a csv file!";
			    result = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
			}
			
		
		return result;
	}
	
	private void givenDataArray_whenConvertToCSV_thenOutputCreated(List<String[]> dataLines) throws IOException {
	    File csvOutputFile = new File("CSV_FILE_NAME");
	    try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
	        dataLines.stream()
	          .map(this::convertToCSV)
	          .forEach(pw::println);
	    }
	    
	}
	
	private String convertToCSV(String[] data) {
	    return Stream.of(data)
	      .map(this::escapeSpecialCharacters)
	      .collect(Collectors.joining(","));
	}
	
	private String escapeSpecialCharacters(String data) {
	    String escapedData = data.replaceAll("\\R", " ");
	    if (data.contains(",") || data.contains("\"") || data.contains("'")) {
	        data = data.replace("\"", "\"\"");
	        escapedData = "\"" + data + "\"";
	    }
	    return escapedData;
	}

}
