package com.koray.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.koray.finago.helpers.CSVHelper;
import com.koray.finago.middleware.DictionaryMid;
import com.koray.finago.models.Language;
import com.koray.finago.models.ResponseMessage;
import com.koray.finago.models.TranslationResult;
import com.koray.finago.service.CsvParse;
import com.koray.finago.service.Synonym;
import com.koray.finago.service.Translate;
import com.koray.finago.serviceimpl.APIException;



@CrossOrigin
@RestController
@RequestMapping("/api/dict")
public class DictionaryController {
	
	private Translate translate = Translate.DEFAULT;
	private CsvParse csvParse = CsvParse.DEFAULT;
	private Synonym synonym = Synonym.DEFAULT;
	
	@SuppressWarnings("rawtypes")
	@PostMapping("/createfile")
	public ResponseEntity<ResponseMessage> createFile(@RequestParam("file") MultipartFile file, Model model) {
		
		ResponseEntity<ResponseMessage> result = null;
		List<String> wordsInEnglish = new ArrayList<String>();
		boolean finished = false;
		String message = "";

		DictionaryMid middleware = new DictionaryMid();
		result = middleware.execute(file, model, translate, csvParse, synonym);
        return result;
	}
	
	

}
