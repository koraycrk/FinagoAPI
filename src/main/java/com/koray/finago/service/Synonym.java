package com.koray.finago.service;

import java.util.List;

import com.koray.finago.models.Language;
import com.koray.finago.serviceimpl.GetSyns;
import com.koray.finago.serviceimpl.APIException;
import com.koray.finago.serviceimpl.TranslateV2;

public interface Synonym {
	
	/**
	 * Default instance of the Translate API.
	 */
	Synonym DEFAULT = new GetSyns();

    /**
     * Translates text from a given Language to another given Language using Google Translate.
     * 
     * @param text The String to translate.
     * @param from The language code to translate from.
     * @param to The language code to translate to.
     * @return The translated String.
     * @throws APIException on error.
     */
	List<String> execute(String text) throws APIException;

}
