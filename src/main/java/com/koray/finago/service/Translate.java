package com.koray.finago.service;

import com.koray.finago.models.Language;
import com.koray.finago.serviceimpl.APIException;
import com.koray.finago.serviceimpl.TranslateV2;

public interface Translate {
	
	/**
	 * Default instance of the Translate API.
	 */
	Translate DEFAULT = new TranslateV2();

    /**
     * Translates text from a given Language to another given Language using Google Translate.
     * 
     * @param text The String to translate.
     * @param from The language code to translate from.
     * @param to The language code to translate to.
     * @return The translated String.
     * @throws APIException on error.
     */
	String execute(String text, Language from, Language to) throws APIException;
	
    /**
     * Translates an array of text Strings from a given Language to another given Language using Google Translate.
     * 
     * @param text The array of Strings to translate.
     * @param from The language code to translate from.
     * @param to The language code to translate to.
     * @return The translated array of String results.
     * @throws APIException on error.
     */
	String[] execute(String[] text, Language from, Language to) throws APIException;
	
    /**
     * Translates a String from a given Language to an Array of Languages using Google Translate.
     * 
     * @param text The String to translate.
     * @param from The language code to translate from.
     * @param to The array of Languages to translate to.
     * @return The translated array of String results.
     * @throws APIException on error.
     */
	String[] execute(String text, Language from, Language[] to) throws APIException;
	
    /**
     * Translates text from a given Language to another given Language using Google Translate.
     * 
     * @param text The array of Strings to translate.
     * @param from The array of Language codes to translate from.
     * @param to The array of Language codes to translate to.
     * @return The translated array of String results.
     * @throws APIException on error.
     */
	String[] execute(String[] text, Language from[], Language[] to) throws APIException;
}
