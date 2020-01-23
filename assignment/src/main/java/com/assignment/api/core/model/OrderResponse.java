package com.assignment.api.core.model;

import java.math.BigDecimal;

public class OrderResponse {
	BigDecimal price;

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}
}
