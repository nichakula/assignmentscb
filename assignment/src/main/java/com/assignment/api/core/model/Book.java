package com.assignment.api.core.model;

import java.math.BigDecimal;

public class Book implements Comparable<Book>{
	
	private Long id;
	private BigDecimal price;
	private String book_name;
	private String author_name;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public String getBook_name() {
		return book_name;
	}
	public void setBook_name(String book_name) {
		this.book_name = book_name;
	}
	public String getAuthor_name() {
		return author_name;
	}
	public void setAuthor_name(String author_name) {
		this.author_name = author_name;
	}
	public String toString() {
        return ("(" + book_name + ", " + id + ", " + author_name + ")");
    }
	@Override
	public int compareTo(Book o) {
		return toString().compareTo(o.toString());
	}
}
