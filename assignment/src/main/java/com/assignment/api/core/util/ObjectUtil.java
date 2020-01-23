package com.assignment.api.core.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.assignment.api.core.model.Book;

public class ObjectUtil {
	public static List<Book> arrayToBookList(Book[] books){
		List<Book> res = new ArrayList<>();
		for(Book book:books) {
			res.add(book);
		}
		return res;
	}
	public static HashMap listToHm(List<Book> list) {
		HashMap hm = new HashMap();
		for(Book boook:list) {
			hm.put(boook.getId(), boook.getPrice());
		}
		return hm;
	}
	
	public static ArrayList<Book> removeDuplicates(List<Book> list) 
    { 
        ArrayList<Book> newList = new ArrayList<Book>(); 
        for (Book element : list) { 
            if (!newList.contains(element)) { 
  
                newList.add(element); 
            } 
        } 
        return newList; 
    } 
}
