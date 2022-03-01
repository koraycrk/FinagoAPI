package com.koray.finago.serviceimpl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

/**
 * Makes generic Google API functionality available to specific API classes.
 * 
 * @author Richard Midwinter
 * @author Kramar Tomas
 */
public abstract class DictionaryAPI {
	
	/**
	 * Default encoding to use.
	 */
	protected static final String ENCODING = "UTF-8";
	
    /**
     * The HTTP referrer and API key attribute.
     * 
     * These allow Google to distinguish between programs.
     */
    protected static String referrer, key;
    
    /**
     * Sets the HTTP Referrer.
     * @param pReferrer The HTTP referrer parameter.
     */
    public static void setHttpReferrer(final String pReferrer) {
    	referrer = pReferrer;
    }
    
    /**
     * Sets the API key.
     * @param pKey The API key.
     */
    public static void setKey(final String pKey) {
    	key = pKey;
    }
    
    public static void validateReferrer() throws Exception {
    	if (referrer == null || referrer.length() == 0) {
    		throw new Exception("[google-api-translate-java] Referrer is not set. Call setHttpReferrer().");
    	}
    }
    
    /**
     * Forms an HTTP request, sends it using POST method and returns the result of the request as a JSONObject.
     * 
     * @param url The URL to query for a JSONObject.
     * @param parameters Additional POST parameters
     * @return The translated String.
     * @throws Exception on error.
     */
    protected static JSONObject retrieveJSON(final URL url, final String parameters) throws Exception {
    	try {
    		final HttpURLConnection uc = (HttpURLConnection) url.openConnection();
    		uc.setRequestProperty("referer", referrer);
    		uc.setRequestMethod("POST");
    		uc.setDoOutput(true);
            uc.setRequestProperty("X-HTTP-Method-Override", "GET");
            
			final PrintWriter pw = new PrintWriter(uc.getOutputStream());
			pw.write(parameters);
			pw.close();
			uc.getOutputStream().close();
    		
    		try {
    			final String result = inputStreamToString(uc.getInputStream());
    			
    			return new JSONObject(result);
    		} finally { // http://java.sun.com/j2se/1.5.0/docs/guide/net/http-keepalive.html
    			if (uc.getInputStream() != null) {
    				uc.getInputStream().close();
    			}
    			if (uc.getErrorStream() != null) {
    				uc.getErrorStream().close();
    			}
    			if (pw != null) {
    				pw.close();
    			}
    		}
    	} catch (Exception ex) {
    		throw new Exception("[google-api-translate-java] Error retrieving translation.", ex);
    	}
    }
    
    /**
     * Reads an InputStream and returns its contents as a String.
     * Also effects rate control.
     * @param inputStream The InputStream to read from.
     * @return The contents of the InputStream as a String.
     * @throws Exception on error.
     */
    private static String inputStreamToString(final InputStream inputStream) throws Exception {
    	final StringBuilder outputBuilder = new StringBuilder();
    	
    	try {
    		String string;
    		if (inputStream != null) {
    			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, ENCODING));
    			while (null != (string = reader.readLine())) {
    				outputBuilder.append(string).append('\n');
    			}
    		}
    	} catch (Exception ex) {
    		throw new Exception("[google-api-translate-java] Error reading translation stream.", ex);
    	}
    	
    	return outputBuilder.toString();
    }
}

