package ru.oculus.database.service.table;

import java.math.BigDecimal;

public class TableInfo {
	private String name;

	private BigDecimal size;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getSize() {
		return size;
	}

	public void setSize(BigDecimal size) {
		this.size = size;
	}

}
