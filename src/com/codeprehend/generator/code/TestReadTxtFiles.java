package com.codeprehend.generator.code;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TestReadTxtFiles {
	
	public static String JAVA_FILE = "MonitorizariDsp.java";
	public static String PATH = "D:\\java\\GeneratorGabi\\src\\codeprehend\\ro\\generator\\beans\\";
	
	public static List<String> strClassToGenerate = new ArrayList();
	public static String onlyOneStr = new String();
	private static Scanner myReaderFile;
	private static Scanner myReaderLine;
	private static File myFile;
	
	public static void main(String args[]) {
		
		try {
			myFile = new File(PATH + JAVA_FILE);
			myReaderFile = new Scanner(myFile);
			
		} catch (FileNotFoundException e){
			System.out.println("File not found: " + PATH + JAVA_FILE);
			e.printStackTrace();
		}
		
		while (myReaderFile.hasNextLine()) {
			String data = myReaderFile.nextLine();
			strClassToGenerate.add(data);
		}
		
		myReaderFile.close();
		
		for (String str : strClassToGenerate) {
			System.out.println(str);
		}
		
	}

}
