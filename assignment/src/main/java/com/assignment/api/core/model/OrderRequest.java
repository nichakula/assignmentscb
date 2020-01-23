package com.assignment.api.core.model;

import java.util.List;

public class OrderRequest {
	List<Long> orders;

	public List<Long> getOrders() {
		return orders;
	}

	public void setOrders(List<Long> orders) {
		this.orders = orders;
	}
}
