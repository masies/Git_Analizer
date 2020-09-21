package com.group4.softwareanalytics;
import org.springframework.data.annotation.Id;


public class Commit {
    @Id private String id;

	private String hash;
	private String developer;

	public String getDeveloper() {
		return developer;
	}

	public void setDeveloper(String developer) {
		this.developer = developer;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}
}
