package com.koray.finago.service;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.koray.finago.serviceimpl.CsvService;
import com.koray.finago.serviceimpl.TranslateV2;

public interface CsvParse {
	
	CsvParse DEFAULT = new CsvService();
	
	void csvToEmployees(@RequestParam("file") MultipartFile file, Model model);

}
