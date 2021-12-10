package com.api.helpers;

import java.util.List;

import com.api.models.Users;

import io.restassured.RestAssured;
import support.Configurations;
import tests.TestSupporter;

public class UserServiceHelper extends TestSupporter {
	
	private static final String Base_URL=Configurations.getInstance().getProperty("APIBase_URL");
	
	
	public UserServiceHelper() {
		RestAssured.baseURI=Base_URL;
		
		
	}
	
	public List<Users> GetAllusers(){
		return null;
		
	}

}
