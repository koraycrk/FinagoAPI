package com.koray.finago.serviceimpl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.koray.finago.helpers.CSVHelper;
import com.koray.finago.service.CsvParse;

public class CsvService implements CsvParse {
	
	public void csvToEmployees(@RequestParam("file") MultipartFile file, Model model) {
	    // validate file
	    if (file.isEmpty()) {
	        model.addAttribute("message", "Please select a CSV file to upload.");
	        model.addAttribute("status", false);
	    } else {
	    	 

	        try {
	        	List<String> words = CSVHelper.csvToWords(file.getInputStream());
	           

	            model.addAttribute("words", words);
	            model.addAttribute("status", true);

	        } catch (Exception ex) {
	            model.addAttribute("message", "An error occurred while processing the CSV file.");
	            model.addAttribute("status", false);
	        }
	    }

	}

}
