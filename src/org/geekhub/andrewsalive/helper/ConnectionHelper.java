package org.geekhub.andrewsalive.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;


public class ConnectionHelper {
//	private static String tweeterNickName;
	private static String URL = "";
	
	
	public static ArticleCollection getArticles(String tweeterNickName) throws IOException, JSONException {
		URL ="http://search.twitter.com/search.json?q="+tweeterNickName+"&rpp=25&page=1";
		
        String responce = makeGetRequest();
    	
    	JSONObject jsonObject = new JSONObject(responce);
        return ArticleCollection.unloadJson(jsonObject);
                
//        JSONArray jsonArray = new JSONArray(responce);      
//        return ArticleCollection.unloadJson(jsonArray);
    }
	
	
	private static String makeGetRequest() throws IOException {
        
		StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(URL);
        HttpResponse response = client.execute(httpGet);
        StatusLine statusLine = response.getStatusLine();
        int statusCode = statusLine.getStatusCode();
        if (statusCode == 200) {
            HttpEntity entity = response.getEntity();
            InputStream content = entity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(content));
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        }
        return builder.toString();
    }
}
