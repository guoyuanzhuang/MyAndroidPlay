package com.gyz.myandroidframe.bean;

import java.io.InputStream;

import com.gyz.myandroidframe.app.AppException;

/**
 * 登录用户实体类
 * 
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class User extends BaseEntity {

	private int uid;
	private String location;
	private String name;
	private int followers;
	private int fans;
	private int score;
	private String face;
	private String account;
	private String pwd;
	private boolean isRememberMe;

	private String jointime;
	private String gender;
	private String devplatform;
	private String expertise;
	private int relation;
	private String latestonline;
	
	/**
	 * 数据解析接口
	 * @author guoyuanzhuang
	 * 目的1、多态让我们可以实现不同的解析方式；2、减少依赖性增强扩展
	 */
	public interface UserParse{
		User getUsers(InputStream stream) throws AppException;
	}

	public boolean isRememberMe() {
		return isRememberMe;
	}

	public void setRememberMe(boolean isRememberMe) {
		this.isRememberMe = isRememberMe;
	}

	public String getJointime() {
		return jointime;
	}

	public void setJointime(String jointime) {
		this.jointime = jointime;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getDevplatform() {
		return devplatform;
	}

	public void setDevplatform(String devplatform) {
		this.devplatform = devplatform;
	}

	public String getExpertise() {
		return expertise;
	}

	public void setExpertise(String expertise) {
		this.expertise = expertise;
	}

	public int getRelation() {
		return relation;
	}

	public void setRelation(int relation) {
		this.relation = relation;
	}

	public String getLatestonline() {
		return latestonline;
	}

	public void setLatestonline(String latestonline) {
		this.latestonline = latestonline;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getFollowers() {
		return followers;
	}

	public void setFollowers(int followers) {
		this.followers = followers;
	}

	public int getFans() {
		return fans;
	}

	public void setFans(int fans) {
		this.fans = fans;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getFace() {
		return face;
	}

	public void setFace(String face) {
		this.face = face;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
}
