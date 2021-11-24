package com.xunda.lib.common.common.http;

import java.util.List;

public class HttpBaseList<T> {

	private List<T> data;

	@Override
	public String toString() {
		return "HttpBaseList [list=" + data + "]";
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}



}
