//packages
package com.example.demo.services;

import org.springframework.stereotype.Service;

import com.example.demo.model.BookingDetailsModel;
import com.example.demo.model.ProductDetailsModel;
import com.example.demo.model.Response;
import com.example.demo.model.SignUpUserModel;

@Service
public interface Services { 
	
	public Response createNewUser(SignUpUserModel datas);
	
	public Response userUpdate(SignUpUserModel datas);
	
	public Response loginUser(SignUpUserModel datas);
	
	public Response deleteUser(String sNo);
	
	public Response createNewProduct(ProductDetailsModel datas);

	public Response geteveryproduct();
	
	public Response getparticularproduct(ProductDetailsModel datas);

	public Response UpdateProduct(ProductDetailsModel datas);
	
	public Response deleteProduct(String productSNo);
	
	public Response bookOrder(BookingDetailsModel datas);

	public Response sendEmail(String toEmail);

	public Response checkotp(CharSequence otp);
	
}