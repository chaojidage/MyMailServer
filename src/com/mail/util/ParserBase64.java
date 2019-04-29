package com.mail.util;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

public class ParserBase64 {
	//±àÂë
	public static String toBase64(String text) {		
		Base64.Encoder encoder = Base64.getEncoder();
		byte[] textByte;
		try {
			textByte = text.getBytes("UTF-8");
			String encodedText = encoder.encodeToString(textByte);
			return encodedText;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return "";
	}
	//½âÂë
	public static String parserBase64(String text) {
		
		try {
			Base64.Decoder decoder = Base64.getDecoder();
			return new String(decoder.decode(text), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	 
}
