package com.pataniqa.coursera.potlatch.server;

import java.util.Date;
import java.util.Random;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pataniqa.coursera.potlatch.model.Gift;

public class TestData {

	private static final ObjectMapper objectMapper = new ObjectMapper();
	
	private static Random random = new Random();
	
	public static Gift randomGift() {
		long id = random.nextLong();
		String title = "Video-"+id;
		String description = "Description about video " + id;
		String videoUri = "http://coursera.org/some/video-"+id;
		String imageUri = "http://coursera.org/some/image-"+id;
		Date created = new Date();
		long userId = random.nextLong();
		long giftChainId = random.nextLong();
		
		return new Gift(id, title, description, videoUri, imageUri, created, userId, giftChainId);
	}
	
	/**
	 *  Convert an object to JSON using Jackson's ObjectMapper
	 *  
	 * @param o
	 * @return
	 * @throws Exception
	 */
	public static String toJson(Object o) throws Exception{
		return objectMapper.writeValueAsString(o);
	}
}
