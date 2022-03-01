package com.koray.finago.serviceimpl;

import com.koray.finago.models.HTMLEntities;
import com.koray.finago.models.Language;
import com.koray.finago.service.Translate;
import java.net.URL;
import java.net.URLEncoder;
import org.json.JSONArray;
import org.json.JSONObject;

public final class TranslateV2 extends GoogleAPI implements Translate {

	/**
	 * Constants.
	 */
	private static final String URL = "https://www.googleapis.com/language/translate/v2";
	private static final String PAR_TEMPLATE = "key=%s&q=%s&target=%s";

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String execute(final String text, final Language from, final Language to) throws APIException {
		try {
			validateReferrer();

			if (key == null) {
				throw new IllegalStateException("You MUST have a Google API Key to use the V2 APIs. See http://code.google.com/apis/language/translate/v2/getting_started.html");
			}

			final String parameters = String.format(PAR_TEMPLATE, key, URLEncoder.encode(text, ENCODING), to.toString())
					+ (Language.AUTO_DETECT.equals(from) ? "" : String.format("&source=%s", from.toString()));

			final URL url = new URL(URL);

			final JSONObject json = retrieveJSON(url,parameters);

			return getJSONResponse(json);
		} catch (final Exception e) {
			System.out.println("Error: " +e.getMessage());

			throw new APIException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] execute(final String[] text, final Language from, final Language to) throws APIException {
		try {
			validateReferrer();
			
			final Language[] fromArgs = new Language[text.length];
			final Language[] toArgs = new Language[text.length];
			
			for (int i = 0; i<text.length; i++) {
				fromArgs[i] = from;
				toArgs[i] = to;
			}
			
			return execute(text, fromArgs, toArgs);
		} catch (final Exception e) {
			throw new APIException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] execute(final String text, final Language from, final Language[] to) throws APIException {
		try {
			validateReferrer();
			
			final String[] textArgs = new String[to.length];
			final Language[] fromArgs = new Language[to.length];
			
			for (int i = 0; i<to.length; i++) {
				textArgs[i] = text;
				fromArgs[i] = from;
			}
			
			return execute(textArgs, fromArgs, to);
		} catch (final Exception e) {
			throw new APIException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] execute(final String[] text, final Language from[], final Language[] to) throws APIException {
		try {
			validateReferrer();
			
			if (text.length != from.length || from.length != to.length) {
				throw new Exception(
						"[google-api-translate-java] The same number of texts, from and to languages must be supplied.");
			}
			
			if (text.length == 1) {
				return new String[] { execute(text[0], from[0], to[0]) };
			}
			
			final String[] responses = new String[text.length];
			for (int i = 0; i < responses.length; i++) {
				responses[i] = execute(text[i], from[i], to[i]);
			}
			
			return responses;
		} catch (final Exception e) {
			throw new APIException(e);
		}
	}
	
	/**
	 * Returns the JSON response data as a String. Throws an exception if the status is not a 200 OK.
	 * 
	 * @param json The JSON object to retrieve the response data from.
	 * @return The responseData from the JSONObject.
	 * @throws Exception If the responseStatus is not 200 OK.
	 */
	private static String getJSONResponse(final JSONObject json) throws Exception {
		final JSONObject data = json.getJSONObject("data");
		final JSONArray translations = data.getJSONArray("translations");
		final JSONObject translation = translations.getJSONObject(0);
		final String translatedText = translation.getString("translatedText");
		
		return HTMLEntities.unhtmlentities(translatedText);
	}
}
