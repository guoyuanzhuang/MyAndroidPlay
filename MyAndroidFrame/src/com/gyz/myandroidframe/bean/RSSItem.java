package com.gyz.myandroidframe.bean;

import java.io.InputStream;
import java.util.List;

import com.gyz.myandroidframe.app.AppException;

/**
 * RSS
 * @author guoyuanzhuang
 *
 */
public class RSSItem extends BaseEntity{
	private String title;
	private String link;
	private String description;
	private String pubDate;

	public RSSItem() {
		super();
	}
	
	public interface RSSParse{
		List<RSSItem>  getRSSItems(InputStream stream) throws AppException;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPubDate() {
		return pubDate;
	}

	public void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	}

	@Override
	public String toString() {
		return "RSSItem [title=" + title + ", link=" + link + ", description="
				+ description + ", pubDate=" + pubDate + "]";
	}

}
