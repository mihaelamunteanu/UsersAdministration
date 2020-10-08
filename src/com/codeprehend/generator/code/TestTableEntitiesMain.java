package com.codeprehend.generator.code;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.codeprehend.generator.utils.EntityFromDbScriptsGenerator;
import com.codeprehend.generator.utils.JavaTableEntity;

public class TestTableEntitiesMain {
	
	public static String SCRIPT_FILE = /*"script_monitorizari_dsp_modif.sql";*/ 
											"codeprehend_scripts.sql";
	public static String PATH = "C:\\Mihaela\\Other projects\\Github website\\GenerateProject\\db\\togenerate\\";
	public static String JAVA_CLASS_FILE = "ApplicationUserDao.java";
	public static String PATH_JAVA_CLASS = "C:\\Mihaela\\Other projects\\Github website\\GenerateProject\\src\\ro\\dsp\\nt\\dao\\";
	
	public static List<JavaTableEntity> tableEntities;
	
	public static List<String> strClassToGenerate = new ArrayList<String>();
	private static Scanner myReaderFile;
	private static File myFile;

	
	public static List<String> strTemp = new ArrayList<String>();
	public static List<String> strReplaceV1 = new ArrayList<String>();
	public static List<String> strReplaceV2 = new ArrayList<String>();
	
	public static void main(String args[]) {
		tableEntities = EntityFromDbScriptsGenerator.readFileWithScanner(PATH + SCRIPT_FILE);
		
		for (Map.Entry<String, String> entry :tableEntities.get(0).getFields().entrySet()) {
			System.out.println(entry.getKey() + " : " + entry.getValue());
		}
		for (Map.Entry<String, String> entry : tableEntities.get(0).getOriginalDbFields().entrySet()) {
			System.out.println(entry.getKey() + " : " + entry.getValue());
		}
		
		try {
			myFile = new File(PATH_JAVA_CLASS + JAVA_CLASS_FILE);
			myReaderFile = new Scanner(myFile);
			
		} catch (FileNotFoundException e){
			System.out.println("File not found: " + PATH_JAVA_CLASS + JAVA_CLASS_FILE);
			e.printStackTrace();
		}
		
		while (myReaderFile.hasNextLine()) {
			String data = myReaderFile.nextLine();
			strClassToGenerate.add(data);
		}
		
		
		strTemp = CreateStrReplace.createStrReplaceV1(strClassToGenerate, tableEntities);
		
		Boolean beforeDemarcation = true;
		for (String str : strTemp) {
			if (str.equals("demarcation line")) {
				beforeDemarcation = false;
			} else {
				if (beforeDemarcation) {
					strReplaceV1.add(str);
				} else {
					strReplaceV2.add(str);
				}
			}
		}
		
		/*
		for (String str : strReplaceV1) {
			System.out.println(str);
		}
		for (String str : strReplaceV2) {
			System.out.println(str);
		}
		*/
		
		strTemp = GenerateGenerator.generateGeneratorJavaClass(strReplaceV1, strReplaceV2, tableEntities);
		for (String str : strTemp) {
			System.out.println(str);
		}

	}
}
