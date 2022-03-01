package com.koray.finago.serviceimpl;

import com.koray.finago.models.HTMLEntities;
import com.koray.finago.models.Language;
import com.koray.finago.service.Synonym;
import com.koray.finago.service.Translate;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public final class GetSyns extends DictionaryAPI implements Synonym {

	/**
	 * Constants.
	 */
	private static final String URL = "https://www.dictionaryapi.com/api/v3/references/%s/json";
	private static final String PAR_TEMPLATE = "umpire?key=%s";

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> execute(final String text) throws APIException {
		try {
			validateReferrer();

			if (key == null) {
				throw new IllegalStateException("You MUST have a Dictionary API Key to use the  APIs.");
			}

			final String parameters = String.format(PAR_TEMPLATE, key);

			final URL url = new URL(String.format(URL,text));

			final JSONObject json = retrieveJSON(url,parameters);

			return getJSONResponse(json);
		} catch (final Exception e) {
			System.out.println("Error: " +e.getMessage());

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
	private static List<String> getJSONResponse(final JSONObject json) throws Exception {
		List<String> result = new ArrayList<>();
		final JSONObject data = json.getJSONObject("data");
		final JSONArray translations = data.getJSONArray("syns");
		for (int i=0; i < translations.length(); i++) {
			result.add( HTMLEntities.unhtmlentities(translations.getJSONObject(i).toString()));
		}
		
		
		return result;
	}
}
