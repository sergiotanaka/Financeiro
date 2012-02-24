package examples;

import com.jgoodies.binding.beans.Model;

public class Feed extends Model {
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String siteUrl;
	private String feedUrl;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSiteUrl() {
		return siteUrl;
	}

	public void setSiteUrl(String siteUrl) {
		this.siteUrl = siteUrl;
	}

	public String getFeedUrl() {
		return feedUrl;
	}

	public void setFeedUrl(String feedUrl) {
		this.feedUrl = feedUrl;
	}

}
