package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dao.Dao;
import com.example.demo.model.BookingDetailsModel;
import com.example.demo.model.ProductDetailsModel;
import com.example.demo.model.Response;
import com.example.demo.model.SignUpUserModel;

@CrossOrigin("*")
@RestController
@RequestMapping("/auth")


public class Controller {
	
	@Autowired
	private Dao dao;
	
	// Create a new user and checks only the email is correct(if needed we can use any input to check) and push into the user_tables.
	@PostMapping("/createuser")
	public ResponseEntity<Response> createNewUser(@RequestBody SignUpUserModel datas)
	{
		return ResponseEntity.ok(dao.createNewUser(datas));
	}
	
	//LoginUser.
	@PostMapping("/loginuser")
	public ResponseEntity<Response> loginUser(@RequestBody SignUpUserModel datas)
	{
		return ResponseEntity.ok(dao.loginUser(datas));
	}
	
	//User details update.
	@PostMapping("/updateuser")
	public ResponseEntity<Response> userUpdate(@RequestBody SignUpUserModel datas)
	{
		return ResponseEntity.ok(dao.userUpdate(datas));
	}
	
	//user account delete.
	@PostMapping("/deleteuser")
	public ResponseEntity<Response> deleteUser(@RequestParam String sNo)
	{
		return ResponseEntity.ok(dao.deleteUser(sNo));
	}
	
	// Create a new product details and push into the product_details.
	@PostMapping("/addproduct")
	public ResponseEntity<Response> createNewProduct(@RequestBody ProductDetailsModel datas)
	{
		return ResponseEntity.ok(dao.createNewProduct(datas));
	}
	
	// Get all products to visible in the home screen.
	@PostMapping("/geteveryproduct")
	public ResponseEntity<Response> geteveryproduct()
	{
		return ResponseEntity.ok(dao.geteveryproduct());
	}
	
	// Get particular products to visible in the home screen by category.
	@PostMapping("/getparticularproduct")
	public ResponseEntity<Response> getparticularproduct(@RequestBody ProductDetailsModel datas)
	{
		return ResponseEntity.ok(dao.getparticularproduct(datas));
	}
	
	//update a product details.
	@PostMapping("/updateproduct")
	public ResponseEntity<Response> UpdateProduct(@RequestBody ProductDetailsModel datas)
	{
		return ResponseEntity.ok(dao.UpdateProduct(datas));
	}
	
	//user account delete.
	@PostMapping("/deleteproduct")
	public ResponseEntity<Response> deleteProduct(@RequestParam String productSNo)
	{
		return ResponseEntity.ok(dao.deleteProduct(productSNo));
	}
	
	//Book order
	@PostMapping("/bookorder")
	public ResponseEntity<Response> bookOrder(@RequestBody BookingDetailsModel datas)
	{
		return ResponseEntity.ok(dao.bookOrder(datas));
	}
	
	//Send Email
	@PostMapping("/toemail")
	public ResponseEntity<Response> sendEmail(@RequestParam String toEmail)
	{
		return ResponseEntity.ok(dao.sendEmail(toEmail));
	}
	
	//Check OTP
	@PostMapping("/checkotp")
	public ResponseEntity<Response> checkotp(@RequestParam CharSequence otp)
	{
		return ResponseEntity.ok(dao.checkotp(otp));
	}
	
	
	
//	@PostMapping("/edit")
//	public ResponseEntity<Response> updateAll(@RequestBody SignUpUserModel datas)
//	{
//		return ResponseEntity.ok(dao.updateAll(datas));
//	}
	
//	@PostMapping("/edit")
//	public ResponseEntity<Response> updateOne(@RequestBody SignUpUserModel datas)
//	{
//		return ResponseEntity.ok(dao.updateOne(datas));
//	}
}
