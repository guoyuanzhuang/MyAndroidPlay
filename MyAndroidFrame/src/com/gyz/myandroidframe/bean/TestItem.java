package com.gyz.myandroidframe.bean;

import java.io.InputStream;
import java.util.List;

import com.gyz.myandroidframe.app.AppException;
/**
 * 
 * @ClassName RSSItem 
 * @Description RRS测试
 * @author guoyuanzhuang@gmail.com 
 * @date 2014-4-20 上午12:38:52 
 *
 */
public class TestItem extends BaseEntity {
	private String title;
	private String link;
	private String description;
	private String pubDate;

	public TestItem() {
		super();
	}

	public interface TestParse {
		List<TestItem> getTestItems(InputStream stream) throws AppException;
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
