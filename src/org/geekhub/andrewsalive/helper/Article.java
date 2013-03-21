package org.geekhub.andrewsalive.helper;

import org.json.JSONObject;

public class Article {

	private Long id;
	private String title;
	private String content;
	private String avatar;
	
	public long getId() {
        return id;
    }
	
	public void setId(Long id) {
	    this.id = id;
	}
	 
	public String getTitle() {
	    return title;
	}

	public void setTitle(String title) {
	    this.title = title;
	}
	    
	public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    
    public String getAvatar() {
        return avatar;
    }
	
	public void setAvatar(String avatar) {
	    this.avatar = avatar;
	}
	    
	public static Article fromJson(JSONObject jsonData) {
        Article article = new Article();
        article.setTitle(jsonData.optString("from_user_name"));
        article.setContent(jsonData.optString("text"));
        article.setAvatar(jsonData.optString("profile_image_url"));
        article.setId(Long.decode(jsonData.optString("id_str")));

        return article;
    }
}
