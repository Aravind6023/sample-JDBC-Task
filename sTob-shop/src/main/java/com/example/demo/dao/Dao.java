package com.example.demo.dao;

//JAVA SQL PACKAGES
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

//JAVA UTILITY PACKAGES
import java.util.Calendar;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//JAVA MAIL PACKAGE
import javax.mail.Message;

//JAVA JSON PACKAGES
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

//SPRINGBOOT ANNOTATION
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;


//CUSTOM API
import com.example.demo.model.SignUpUserModel;
import com.example.demo.model.BookingDetailsModel;
import com.example.demo.model.ProductDetailsModel;
import com.example.demo.model.Response;
import com.example.demo.services.Services;

@SuppressWarnings("unused")
@Component
public class Dao implements Services {

	@Autowired
	private JavaMailSender mailSender;

	StringBuilder otp = new StringBuilder();
	String OTP;

	Response res = new Response();

	String url = "jdbc:mysql://127.0.0.1:3306/ownproject";
	String userName = "root";
	String password = "#1234@MYSQL@06";

	// This API create a new user and checks the email(contains @gmail.com) and
	// password(starts within 6-9 and total no equal to 10) is correct and push into
	// the user_tables.
	@Override
	public Response createNewUser(SignUpUserModel datas) {

		String uuid = UUID.randomUUID().toString();
		datas.setsNo(uuid);
		datas.setCreatedBy(uuid);
		datas.setUpdatedBy(uuid);

		Date date = new Date(Calendar.getInstance().getTime().getTime());
		datas.setCreatedDate(date);
		datas.setUpdateDate(date);

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			try (Connection conn = DriverManager.getConnection(url, userName, password);
					Statement st = conn.createStatement();) {
				String createQuery = "INSERT INTO ownproject.user_details(s_no,first_name,last_name,password,gender,email,dob,age,location,phone_number,created_date,created_by,updated_date,updated_by)"
						+ "VALUES('" + datas.getsNo() + "','" + datas.getFirstName() + "','" + datas.getLastName()
						+ "','" + datas.getPassword() + "'," + "'" + datas.getGender() + "','" + datas.getEmail()
						+ "','" + datas.getDob() + "'," + datas.getAge() + ",'" + datas.getLocation() + "','"
						+ datas.getPhoneNumber() + "','" + datas.getCreatedDate() + "','" + datas.getCreatedBy() + "','"
						+ datas.getUpdateDate() + "','" + datas.getUpdatedBy() + "');";

				String getEmail = datas.getEmail();
				boolean checkMail = getEmail.contains("@gmail.com");

				Pattern setPhone = Pattern.compile("^[6-9]{1}[0-9]{9}$");
				Matcher checkPhone = setPhone.matcher("" + datas.getPhoneNumber() + "");
				boolean resultPhone = checkPhone.find();

				if (checkMail) {
					if (resultPhone) {
						st.executeUpdate(createQuery);
						res.setResponseCode(200);
						res.setResponseMessage("Success");
						res.setData("User created Successfully");
					} else {
						res.setResponseCode(500);
						res.setResponseMessage("Failure");
						res.setData("Check your phonenumber");
					}
				} else {
					res.setResponseCode(500);
					res.setResponseMessage("Fail to create user");
					res.setData("Check the given Email");
				}
			} catch (Exception e) {
				e.printStackTrace();
				res.setResponseCode(500);
				res.setResponseMessage("Errors");
				res.setData("Internal Server error");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	// Login User using email and password.
	@SuppressWarnings("unchecked")
	public Response loginUser(SignUpUserModel datas) {
		Response res = new Response();
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String login = "SELECT * FROM ownproject.user_details WHERE email = ? AND password = ?";

			try (Connection conn = DriverManager.getConnection(url, userName, password);
					PreparedStatement preparedStatement = conn.prepareStatement(login)) {
				preparedStatement.setString(1, datas.getEmail());
				preparedStatement.setString(2, datas.getPassword());

				JSONArray jsonArray = new JSONArray();

				try (ResultSet rs = preparedStatement.executeQuery()) {

					while (rs.next()) {
						JSONObject jsonObject = new JSONObject();
						jsonObject.put("firstName", rs.getString("first_name"));
						jsonObject.put("lastName", rs.getString("last_name"));
						jsonObject.put("gender", rs.getString("gender"));
						jsonObject.put("email", rs.getString("email"));
						jsonObject.put("password", rs.getString("password"));
						jsonArray.add(jsonObject);
					}
				}

				res.setResponseCode(200);
				res.setResponseMessage("Success");
				res.setData("Login Successfully!");
				res.setjData(jsonArray);
			} catch (Exception e) {
				e.printStackTrace();
				res.setResponseCode(500);
				res.setResponseMessage("Failure");
				res.setData("Enter a valid login credentials");
			}
		} catch (Exception e) {
			e.printStackTrace();
			res.setResponseCode(500);
			res.setResponseMessage("Errors");
			res.setData("Internal Server error");
		}

		return res;
	}

	// User update details(can be anything).
	@Override
	public Response userUpdate(SignUpUserModel datas) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			try (Connection conn = DriverManager.getConnection(url, userName, password);
					Statement st = conn.createStatement();) {
				String updateAllQuery = "UPDATE ownproject.user_details SET first_name = ?, last_name = ?, password = ?, gender = ?, email = ?, age = ?, phone_number = ? WHERE s_no = ?;";

				PreparedStatement preparedStatement = conn.prepareStatement(updateAllQuery);

				preparedStatement.setString(1, datas.getFirstName());
				preparedStatement.setString(2, datas.getLastName());
				preparedStatement.setString(3, datas.getPassword());
				preparedStatement.setString(4, datas.getGender());
				preparedStatement.setString(5, datas.getEmail());
				preparedStatement.setInt(6, datas.getAge());
				preparedStatement.setLong(7, datas.getPhoneNumber());
				preparedStatement.setString(8, datas.getsNo());

				String getEmail = datas.getEmail();
				boolean checkMail = getEmail.contains("@gmail.com");

				Pattern setPhone = Pattern.compile("^[6-9]{1}[0-9]{9}$");
				Matcher checkPhone = setPhone.matcher("" + datas.getPhoneNumber() + "");
				boolean resultPhone = checkPhone.find();

				if (checkMail) {
					if (resultPhone) {
						int rowsUpdated = preparedStatement.executeUpdate();
						if (rowsUpdated > 0) {
							res.setResponseCode(200);
							res.setResponseMessage("Success");
							res.setData("Profile Updated Successfully!");
						} else {
							res.setResponseCode(500);
							res.setResponseMessage("Failure");
							res.setData("There is an error. Check the given sNo.");
						}
					} else {
						res.setResponseCode(500);
						res.setResponseMessage("Failure");
						res.setData("Check the given phone number. It's invalid.");
					}
				} else {
					res.setResponseCode(500);
					res.setResponseMessage("Failure");
					res.setData("Check the given Email. It's invalid.");
				}
			} catch (Exception e) {
				e.printStackTrace();
				res.setResponseCode(500);
				res.setResponseMessage("Errors");
				res.setData("Internal Server error");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	// Delete an existing user.
	@Override
	public Response deleteUser(String sNo) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			try (Connection conn = DriverManager.getConnection(url, userName, password);
					Statement st = conn.createStatement();) {
				String deleteQuery = "DELETE FROM ownproject.user_details WHERE s_no = '" + sNo + "'; ";
				boolean checkQuery = deleteQuery.isEmpty();

				if (!checkQuery) {
					st.executeUpdate(deleteQuery);
					res.setResponseCode(200);
					res.setResponseMessage("Success");
					res.setData("User Deleted Successfully");
				} else {
					res.setResponseCode(500);
					res.setResponseMessage("Fail to create user");
					res.setData("check the given serial number!");
				}

			} catch (Exception e) {
				e.printStackTrace();
				res.setResponseCode(500);
				res.setResponseMessage("Errors");
				res.setData("Internal Server error");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;

	}

	// Create a new product details and push into the product_details.
	@Override
	public Response createNewProduct(ProductDetailsModel datas) {
		String uuid = UUID.randomUUID().toString();
		datas.setProductSNo(uuid);
		datas.setCreatedBy(uuid);
		datas.setUpdatedBy(uuid);

		Date date = new Date(Calendar.getInstance().getTime().getTime());
		datas.setCreatedDate(date);
		datas.setUpdatedDate(date);

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			try (Connection conn = DriverManager.getConnection(url, userName, password);
					Statement st = conn.createStatement();) {

				String createProductQuery = "INSERT INTO ownproject.product_details(product_sno,product_id,product_name,product_description,product_category,price,contact_number,created_date,created_by,updated_date,updated_by)"
						+ "VALUES('" + datas.getProductSNo() + "','" + datas.getProductId() + "','"
						+ datas.getProductName() + "','" + datas.getProductDescription() + "'," + "'"
						+ datas.getProductCategory() + "'," + datas.getPrice() + ",'" + datas.getPhoneNumber() + "','" + datas.getCreatedDate() + "','"
						+ datas.getCreatedBy() + "','" + datas.getUpdatedDate() + "','" + datas.getUpdatedBy() + "');";

				st.executeUpdate(createProductQuery);
				res.setResponseCode(200);
				res.setResponseMessage("Success");
				res.setData("Product added successfully");
			} catch (Exception e) {
				e.printStackTrace();
				res.setResponseCode(500);
				res.setResponseMessage("Failure");
				res.setData("Check the given data.");
			}

		} catch (Exception e) {
			e.printStackTrace();
			res.setResponseCode(500);
			res.setResponseMessage("Errors");
			res.setData("Internal Server error");
		}
		return res;
	}

	// Get particular products to visible in the home screen by category.
	@SuppressWarnings("unchecked")
	@Override
	public Response getparticularproduct(ProductDetailsModel datas) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String getParticularProduct = "SELECT product_id, product_name, product_description, price, product_category, created_date, created_by FROM ownproject.product_details WHERE product_category = '"
					+ datas.getProductCategory() + "';";

			try (Connection conn = DriverManager.getConnection(url, userName, password);
					Statement st = conn.createStatement();
					ResultSet rs = st.executeQuery(getParticularProduct);) {

				JSONArray jsonArray = new JSONArray();

				while (rs.next()) {
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("productId", rs.getString("product_id"));
					jsonObject.put("productName", rs.getString("product_name"));
					jsonObject.put("productDescription", rs.getString("product_description"));
					jsonObject.put("productCategory", rs.getString("product_category"));
					jsonObject.put("price", rs.getInt("price"));
					jsonObject.put("createdDate", rs.getDate("created_date"));
					jsonObject.put("createdBy", rs.getString("created_by"));

					jsonArray.add(jsonObject);
				}
				if (jsonArray.isEmpty()) {
				    res.setResponseCode(500);
				    res.setResponseMessage("No products found");
				    res.setData("No products were found for the selected category");
				    res.setjData(null);
				} else {
				    res.setResponseCode(200);
				    res.setResponseMessage("Success");
				    res.setData("Selected category has been updated!");
				    res.setjData(jsonArray);
				}
			} catch (Exception e) {
				e.printStackTrace();
				res.setResponseCode(500);
				res.setResponseMessage("fail to get");
				res.setData("No Data's Were Found!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			res.setResponseCode(500);
			res.setResponseMessage("Error");
			res.setData("Internal server error");
		}
		return res;

	}

	// Get all products to visible in the home screen.
	@SuppressWarnings("unchecked")
	@Override
	public Response geteveryproduct() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String getallProduct = "SELECT product_id, product_name, product_description, price, product_category, created_date, created_by FROM ownproject.product_details;";

			try (Connection conn = DriverManager.getConnection(url, userName, password);
					Statement st = conn.createStatement();
					ResultSet rs = st.executeQuery(getallProduct);) {

				JSONArray jsonArray = new JSONArray();

				while (rs.next()) {
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("productId", rs.getString("product_id"));
					jsonObject.put("productName", rs.getString("product_name"));
					jsonObject.put("productDescription", rs.getString("product_description"));
					jsonObject.put("productCategory", rs.getString("product_Category"));
					jsonObject.put("price", rs.getInt("price"));
					jsonObject.put("createdDate", rs.getDate("created_date"));
					jsonObject.put("createdBy", rs.getString("created_by"));

					jsonArray.add(jsonObject);
				}

				res.setResponseCode(200);
				res.setResponseMessage("Success");
				res.setData("All products has been updated!");
				res.setjData(jsonArray);
			} catch (Exception e) {
				e.printStackTrace();
				res.setResponseCode(500);
				res.setResponseMessage("fail to get");
				res.setData("No Data's Were Found!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			res.setResponseCode(500);
			res.setResponseMessage("Error");
			res.setData("Internal server error");
		}
		return res;

	}

	// update the product details.
	@Override
	public Response UpdateProduct(ProductDetailsModel datas) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			try (Connection conn = DriverManager.getConnection(url, userName, password);
					Statement st = conn.createStatement();) {
				String updateAllQuery = "UPDATE ownproject.product_details SET product_name = ?, product_description = ?, price = ? WHERE product_sno = ?;";

				PreparedStatement preparedStatement = conn.prepareStatement(updateAllQuery);

				preparedStatement.setString(1, datas.getProductName());
				preparedStatement.setString(2, datas.getProductDescription());
				preparedStatement.setLong(3, datas.getPrice());
				preparedStatement.setString(4, datas.getProductSNo());

				int rowsUpdated = preparedStatement.executeUpdate();
				if (rowsUpdated > 0) {
					res.setResponseCode(200);
					res.setResponseMessage("Success");
					res.setData("Product Updated Successfully!");
				} else {
					res.setResponseCode(500);
					res.setResponseMessage("Failure");
					res.setData("There is an error. Check the given sNo.");
				}
			} catch (Exception e) {
				e.printStackTrace();
				res.setResponseCode(500);
				res.setResponseMessage("Errors");
				res.setData("Internal Server error");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	// Delete a product.
	@Override
	public Response deleteProduct(String productSNo) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			try (Connection conn = DriverManager.getConnection(url, userName, password);
					Statement st = conn.createStatement();) {
				String deleteQuery = "DELETE FROM ownproject.product_details WHERE product_sno = '" + productSNo + "';  ";
				boolean checkQuery = deleteQuery.isEmpty();

				if (!checkQuery) {
					st.executeUpdate(deleteQuery);
					res.setResponseCode(200);
					res.setResponseMessage("Success");
					res.setData("Product Deleted Successfully");
				} else {
					res.setResponseCode(500);
					res.setResponseMessage("Fail to create user");
					res.setData("check the given serial number!");
				}

			} catch (Exception e) {
				e.printStackTrace();
				res.setResponseCode(500);
				res.setResponseMessage("Errors");
				res.setData("Internal Server error");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	
	//bookOrder API
	public Response bookOrder(BookingDetailsModel datas) {
	    Response res = new Response();
	    
		String uuid = UUID.randomUUID().toString();
		datas.setOrderId(uuid);
		
		Date date = new Date(Calendar.getInstance().getTime().getTime());
		datas.setCreatedDate(date);

	    try {
	        Class.forName("com.mysql.cj.jdbc.Driver");
	        try (Connection conn = DriverManager.getConnection(url, userName, password)) {
	            String insertQuery = "INSERT INTO booking_details (order_id, product_sno, product_name, user_sno, invoice_amount, payment_mode, phone_number, created_by, updated_by, created_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	            try (PreparedStatement preparedStatement = conn.prepareStatement(insertQuery)) {
	                preparedStatement.setString(1, datas.getOrderId());
	                preparedStatement.setString(2, datas.getProductId());
	                preparedStatement.setString(3, datas.getProductName());
	                preparedStatement.setString(4, datas.getUserId());
	                preparedStatement.setDouble(5, datas.getInvoiceAmount());
	                preparedStatement.setString(6, datas.getPaymentMode());
	                preparedStatement.setDouble(7, datas.getPhoneNumber());
	                preparedStatement.setString(8, datas.getProductId());
	                preparedStatement.setString(9, datas.getProductId());
	                preparedStatement.setDate(10, datas.getCreatedDate());

	                int rowsAffected = preparedStatement.executeUpdate();

	                if (rowsAffected > 0) {
	                    res.setResponseCode(200);
	                    res.setResponseMessage("Success");
	                    res.setData("Order has been placed successfully");                    
	                    String selectQuery = "SELECT booking_details.order_id, product_details.product_id, product_details.product_name, product_details.price, user_details.first_name "
				                    		+ "FROM booking_details "
				                    		+ "JOIN product_details ON booking_details.product_sno = product_details.product_sno "
				                    		+ "JOIN user_details ON booking_details.user_sno = user_details.s_no "
				                    		+ "WHERE booking_details.order_id = ?";
	                    try (PreparedStatement selectStatement = conn.prepareStatement(selectQuery)) {
	                    	
	                        selectStatement.setString(1, datas.getOrderId());
	                        ResultSet resultSet = selectStatement.executeQuery();
	                        
	                        if (resultSet.next()) {
	                            String orderDetails = "Order ID: " + resultSet.getString("order_id") +
	                                                 ", Product ID: " + resultSet.getString("product_id") +
	                                                 ", Product Name: " + resultSet.getString("product_name") +
	                                                 ", Price: " + resultSet.getDouble("price") +
	                                                 ", User Name: " + resultSet.getString("first_name");
	                            res.setjData(orderDetails);
	                        }
	                    } 
	                } else {
	                    res.setResponseCode(500);
	                    res.setResponseMessage("Failure");
	                    res.setData("Order placement failed");
	                }
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace(); // Replace with proper logging
	        res.setResponseCode(500);
	        res.setResponseMessage("Failure");
	        res.setData("Internal Server Error");
	    }
	    return res;
	}


	// Send OTP to verify the email.
	@Override
	public Response sendEmail(String toEmail) {
		try {
			Random random = new Random();

			for (int i = 0; i < 4; i++) {
				otp.append(random.nextInt(10));
			}

			OTP = otp.toString();
			System.out.println(OTP);
			String fromMail = "chinnadurai.metro@gmail.com";
			String sendSubject = "OTP for verification";
			String sendMessage = "Your otp is " + OTP + ".";
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom(fromMail);
			message.setSubject(sendSubject);
			message.setText(sendMessage);
			message.setTo(toEmail);

			mailSender.send(message);
			res.setResponseMessage("Success");
			res.setResponseCode(200);
			res.setData("OTP has been sent");
			res.setjData(OTP);

		} catch (Exception e) {
			e.printStackTrace();
			res.setResponseMessage("Error");
			res.setResponseCode(500);
			res.setData("Invalid Email");
		}
		return res;
	}

	@Override
	public Response checkotp(CharSequence otp) {
		try {
			String givenOTP = OTP;
			boolean checkOtp = givenOTP.contains(otp);
			if (checkOtp) {
				res.setResponseMessage("Success");
				res.setResponseCode(200);
				res.setData("Email has been verified");
			} else {
				res.setResponseMessage("Failure");
				res.setResponseCode(500);
				res.setData("check your OTP");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

}
