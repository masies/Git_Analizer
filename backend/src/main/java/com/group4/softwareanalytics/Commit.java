package com.group4.softwareanalytics;
import org.springframework.data.annotation.Id;


public class Commit {
    @Id private Long id;

	private String message;
	private String developer;

	public Commit(Long id, String message, String developer){
		this.id = id;
		this.developer = developer;
		this.message = message;
	}

	public String getDeveloper() {
		return developer;
	}

	public void setDeveloper(String developer) {
		this.developer = developer;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
