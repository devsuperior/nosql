package com.devsuperior.workshopmongo.models.embedded;

import java.io.Serializable;
import java.time.Instant;

public class Comment implements Serializable {
	private static final long serialVersionUID = 1L;

	private String text;
	private Instant moment;
	private Author author;
	
	public Comment() {
	}

	public Comment(String text, Instant moment, Author author) {
		this.text = text;
		this.moment = moment;
		this.author = author;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Instant getMoment() {
		return moment;
	}

	public void setMoment(Instant moment) {
		this.moment = moment;
	}

	public Author getAuthor() {
		return author;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}
}
