package org.geekhub.andrewsalive.helper;

import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ArticleCollection {

	
	Vector<Article> articles = new Vector<Article>();
	
	public Vector<Article> asVector() {
        return articles;
    }
	
	public Article get(int index) {
        return articles.get(index);
    }
	
	private void add(Article article) {
        articles.addElement(article);
    }

	public static ArticleCollection unloadJson(JSONObject jsonData) throws JSONException{
		ArticleCollection articleCollection = new ArticleCollection();
		

        JSONArray jsonEntry = jsonData.getJSONArray("results");
		
		
//		JSONObject jsonFeed = jsonData.getJSONObject("feed");
//        JSONArray jsonEntry = jsonFeed.getJSONArray("entry");

        for (int i = 0; i < jsonData.length(); i++) {
            articleCollection.add(Article.fromJson(jsonEntry.getJSONObject(i)));
        }
		
		
		return articleCollection;
	}
}
