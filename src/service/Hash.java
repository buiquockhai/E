package service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Hash {

	public static String Hash(File file, String algo) throws Exception{
		
//		Convert file to bytes
		FileInputStream fileInputStream = new FileInputStream(file);
		byte[] bytes = new byte[(int)file.length()];
		fileInputStream.read(bytes);
		
		MessageDigest messageDigest = MessageDigest.getInstance(algo);
		byte[] result = messageDigest.digest(bytes);
		
		fileInputStream.close();
		
		return new String(result);
	}
	
	
}
