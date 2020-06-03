package service;

import java.util.List;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.Key;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AES {

	private final String ALGO = "AES";
	private String keyValue;
	
	public AES() {
		super();
	}
	
	public AES(String keyValue) {
		super();
		this.keyValue = keyValue;
	}
	
	public Key generateKey() throws Exception{
		return new SecretKeySpec(keyValue.getBytes(), ALGO);
	}

	public Object encrypt(String iPath, Object object, boolean flag) throws Exception{
		
		String data = "";
		byte[] bytes = {};
		Key key = generateKey();
	    Cipher c = Cipher.getInstance(ALGO);
	    c.init(Cipher.ENCRYPT_MODE, key);
		
		if(object instanceof String) {
			data = (String) object;
			bytes = data.getBytes();
			byte[] encVal = c.doFinal(bytes);
		    return Base64.getEncoder().encodeToString(encVal);
		}
		else {
			File file = (File) object;
			FileInputStream fileInputStream = new FileInputStream(file);
			bytes = new byte[(int)file.length()];
			fileInputStream.read(bytes);
			
			byte[] encVal = c.doFinal(bytes);
			
			String path = iPath + file.getName();
			File rtFile;
			if (flag) rtFile = new File(iPath);
			else rtFile = new File(path);
			FileOutputStream fileOutputStream = new FileOutputStream(rtFile);
			fileOutputStream.write(encVal);
			
			fileInputStream.close();
	        fileOutputStream.close();
			return rtFile;
		}
	}

	public Object decrypt(String iPath, Object object, boolean flag) throws Exception{
		
		String data = "";
		byte[] bytes = {};
		
		Key key = generateKey();
	    Cipher c = Cipher.getInstance(ALGO);
	    c.init(Cipher.DECRYPT_MODE, key);
	    
	    if(object instanceof String) {
			data = (String) object;
			bytes = Base64.getDecoder().decode(data);
			byte[] decVal = c.doFinal(bytes);
		    return new String(decVal);
		}
		else {
			
			File file = (File) object;
			FileInputStream fileInputStream = new FileInputStream(file);
			bytes = new byte[(int)file.length()];
			fileInputStream.read(bytes);
			
			byte[] decVal = c.doFinal(bytes);
			
			String path = iPath + file.getName();
			File rtFile;
			if (flag) rtFile = new File(iPath);
			else rtFile = new File(path);
//			File rtFile = new File(iPath);
			FileOutputStream fileOutputStream = new FileOutputStream(rtFile);
			fileOutputStream.write(decVal);
			
			fileInputStream.close();
	        fileOutputStream.close();
			return rtFile;
		}
	    
	}
	
	public void folderEcrypt(String iPath, File folder) throws Exception {
		File rtFile;
		String path = iPath + folder.getName();
		new File(path).mkdir();
		File[] fArr = folder.listFiles();
		for(File f : fArr) {
			if(f.isDirectory()) folderEcrypt(path + "\\", f);
			else rtFile = (File) encrypt(iPath + folder.getName() + "\\", f,false);
		}
	}
	
	public void folderDecrypt(String iPath, File folder) throws Exception {
		File rtFile;
		String path = iPath + folder.getName();
		new File(path).mkdir();
		File[] fArr = folder.listFiles();
		for(File f : fArr) {
			if(f.isDirectory()) folderDecrypt(path + "\\", f);
			else rtFile = (File) decrypt(iPath + folder.getName() + "\\", f,false);
		}
	}
	
}
