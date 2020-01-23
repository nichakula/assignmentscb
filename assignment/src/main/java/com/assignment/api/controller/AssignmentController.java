package com.assignment.api.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Produces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import com.assignment.api.core.dao.IAssignmentDao;
import com.assignment.api.core.model.Book;
import com.assignment.api.core.model.OrderRequest;
import com.assignment.api.core.model.OrderResponse;
import com.assignment.api.core.model.User;
import com.assignment.api.core.service.AssignmentService;
import com.assignment.api.core.util.JsonUtil;
import com.assignment.api.core.util.ObjectUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;

@SuppressWarnings("unused")
@Controller
@RequestMapping("/")
public class AssignmentController {
	final static Logger logger = Logger.getLogger(AssignmentController.class);
	@Autowired
	private Environment env;
	
	@Autowired
	AssignmentService assignmentService;

	@PostMapping(path= "/login", consumes = "application/json", produces = "application/json")
	public ResponseEntity<User> addEmployee(@RequestBody User login) throws Exception 
	{       
		logger.info("begin addEmployee");
		try {
			int res = assignmentService.login(login);
			if(res > 0) {
				logger.info("finish login");
				return ResponseEntity.status(HttpStatus.OK).body(null);
			}else {
				return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(null);
			}
			
		}catch(Exception ex) {
			logger.error(ex);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
	
	@GetMapping(path= "/users", produces = "application/json")
	public ResponseEntity<User> getUser() throws Exception 
	{     
		
		logger.info("begin getUser");
		try {
			User user = assignmentService.getUser();
			if(user == null) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
			}
			user.setUsername(null);
			logger.info("finish getUser");
			return ResponseEntity.status(HttpStatus.OK).body(user);
		}catch(Exception ex) {
			logger.error(ex);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
		
	}
	
	@DeleteMapping(path= "/users", produces = "application/json")
	public ResponseEntity<User> deleteUser() throws Exception 
	{  
		
		logger.info("begin deleteUser");
		try {
			User user = assignmentService.getUser();
			if(user!=null) {
				assignmentService.deleteUser(user.getUsername());
			}else {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
			}
			logger.info("finish deleteUser");
			return ResponseEntity.status(HttpStatus.OK).body(null);
		}catch(Exception ex) {
			logger.error(ex);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
		
	}
	
	@PostMapping(path= "/user", consumes = "application/json", produces = "application/json")
	public ResponseEntity<User> addUser(@RequestBody User login) throws Exception 
	{       
		logger.info("begin addUser");
		try {
			assignmentService.addUser(login);
			logger.info("finish addUser");
			return ResponseEntity.status(HttpStatus.OK).body(login);
		}catch(Exception ex) {
			logger.error(ex);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(login);
		}
		
	}
	
	@PostMapping(path= "/users/orders", consumes = "application/json", produces = "application/json")
	public ResponseEntity<OrderResponse> orders(@RequestBody OrderRequest order) throws Exception 
	{       
		logger.info("begin orders");
		try {
			OrderResponse orderResponse = new OrderResponse();
			User user = assignmentService.getUser();
			if(user == null) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
			}
			
			List<Book> res = new ArrayList<>();
			String jsonRec = JsonUtil.getJsonFromUrl(env.getProperty("books-reccomment-service"));
			String jsonBooks = JsonUtil.getJsonFromUrl(env.getProperty("books-service"));
			ObjectMapper mapper = new ObjectMapper();
			Book[] booksRec = mapper.readValue(jsonRec, Book[].class);
			List<Book> booksRecList = ObjectUtil.arrayToBookList(booksRec);
			Arrays.sort(booksRec);
			Book[] books = mapper.readValue(jsonBooks, Book[].class);
			Arrays.sort(books);
			List<Book> booksList = ObjectUtil.arrayToBookList(books);
			res.addAll(booksRecList);
			res.addAll(booksList);
			
			res = ObjectUtil.removeDuplicates(res);
			List<Long> orderList = order.getOrders();
			HashMap hm = ObjectUtil.listToHm(res);
			BigDecimal sum = new BigDecimal(0);
			if(!CollectionUtils.isEmpty(orderList)) {
				for(Long id:orderList) {
					Object price = hm.get(id);
					if(price!=null) {
						assignmentService.addOrder(user.getUsername(), String.valueOf(id));
						BigDecimal b = (BigDecimal) price;
						sum = sum.add(b);
					}
				}
				orderResponse.setPrice(sum);
			}
			logger.info("finish orders");
			return ResponseEntity.status(HttpStatus.OK).body(orderResponse);
		}catch(Exception ex) {
			logger.error(ex);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
		
	}
	
	@RequestMapping(value = "/books", produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
	public ResponseEntity<List<Book>> books() {
		logger.info("begin books");
		List<Book> res = new ArrayList<>();
		try {
			
			String jsonRec = JsonUtil.getJsonFromUrl(env.getProperty("books-reccomment-service"));
			String jsonBooks = JsonUtil.getJsonFromUrl(env.getProperty("books-service"));
			ObjectMapper mapper = new ObjectMapper();
			Book[] booksRec = mapper.readValue(jsonRec, Book[].class);
			List<Book> booksRecList = ObjectUtil.arrayToBookList(booksRec);
			Arrays.sort(booksRec);
			Book[] books = mapper.readValue(jsonBooks, Book[].class);
			Arrays.sort(books);
			List<Book> booksList = ObjectUtil.arrayToBookList(books);
			res.addAll(booksRecList);
			res.addAll(booksList);

			res = ObjectUtil.removeDuplicates(res);
			
		}catch(Exception ex) {
			logger.error(ex);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
		logger.info("finish books");
		return ResponseEntity.status(HttpStatus.OK).body(res);
	}
	
}
