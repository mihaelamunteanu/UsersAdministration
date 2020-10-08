package com.codeprehend.generator.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EntityFromDbScriptsGenerator {
	public static Scanner fileScanner;
	public static Scanner lineScanner;
	
	private static int extraBrakets = 0;

	public static List<JavaTableEntity> readFileWithScanner(String filePathAndName) {
		List<JavaTableEntity> tableEntities = new ArrayList<JavaTableEntity>();
		extraBrakets = 0;
	    try {
	        fileScanner = new Scanner(new File(filePathAndName));
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();  
	    }
	    while (fileScanner.hasNextLine()) {
	    	lineScanner = new Scanner(fileScanner.nextLine());
	        while (lineScanner.hasNext()) {
	        	String s = goNext();
	            if (s.toUpperCase().equals("CREATE")) {
	            	s = goNext();
	            	if (s.toUpperCase().equals("TABLE")) {
	            		//read table name
	            		s = goNext();
	            		JavaTableEntity tableEntity = new JavaTableEntity();
	            		tableEntities.add(tableEntity);
	            		tableEntity.setTableName(processTableNameToJavaBeanName(s));
	            		tableEntity.setOriginalTableName(processOriginalTableName(s));
	            		tableEntity.setSchemaName(processSchemaName(s));
	            		System.out.println("\nTable name: " + tableEntity.getTableName());
	            		
	            		while (!s.equals("(") && !s.contains("(")) s = goNext();
	            		
	            		boolean continueReading = true;
	            		while (continueReading) { 
	            			s = goNext();
	            			if (s.toUpperCase().contains("CONSTRAINT") || s.toUpperCase().contains("UNIQUE")) {
//	            				s = skipLine(); //constraints are not fields
	            				continueReading = false;
	            			} else {
		            			String fieldName = processFieldNameToJavaProperty(s);
		            			String originalDbField = s;
		            			s = goNext();
		            			String type = s; 
		            			while ( (!s.contains(",") && !s.contains(")") && !s.contains(");" )) || 
		            					(s.contains(",") && extraBrakets > 1 && !s.contains("),")) ||
		            					(s.contains(")") && extraBrakets > 0 && !s.contains("),")) ) {
		            				
		            				s = goNext();
		            				if (!s.equals(",") && !s.equals(")")) type = type + s;
		            			}
		            			type = processTypeNameFromSQLType(type);
		            			tableEntity.getFields().put(fieldName, type);
		            			tableEntity.getOriginalDbFields().put(fieldName, originalDbField);
		            			
		            			 System.out.println("Field + Type: " + fieldName + " + " + type);
		            			if ((s.contains(")") && !s.contains("("))) continueReading = false;
	            			}
	            		}
	            	}
	            }
	           
	        }
	        lineScanner.close();
	    }
	    fileScanner.close();
	    
	    return tableEntities;
	}

	public static String skipLine() {
		String currentString = null;
		if (fileScanner.hasNextLine()) {
			lineScanner.close();
			lineScanner = new Scanner(fileScanner.nextLine());
			if (lineScanner.hasNext()) {
				currentString = lineScanner.next();
				countBrackets(currentString);
				return currentString;
			}
		}
		return currentString;
	}
	
	public static String goNext() {
		String currentString = null;
		boolean searchLineWithText = true;
		while (searchLineWithText) {
			if (lineScanner.hasNext()) {
				currentString = lineScanner.next();
				countBrackets(currentString);
				return currentString;
			}
			if (fileScanner.hasNextLine()) {
				lineScanner.close();
				lineScanner = new Scanner(fileScanner.nextLine());
				if (lineScanner.hasNext()) {
					currentString = lineScanner.next();
					countBrackets(currentString);
					return currentString;
				}
			} else {
				searchLineWithText = false;
			}
		}
		
		return currentString;
	}

	private static void countBrackets(String currentString) {
		for (int i = 0; i < currentString.length(); i++) {
		    if (currentString.charAt(i) == '(') {
		    	extraBrakets++;
		    } 
		    if (currentString.charAt(i) == ')') {
		    	extraBrakets--;
		    } 
		}
		System.out.println("** " + currentString + " extra brackets " + extraBrakets);
	}
	
	public static String processTypeNameFromSQLType(String sqlType) {
		String javaType = "";
		sqlType = sqlType.toLowerCase();
		if (sqlType.startsWith("bigint") || sqlType.startsWith("smallint") || sqlType.startsWith("integer") || sqlType.startsWith("numeric")  ) {
			javaType = "Long";
		} else if (sqlType.startsWith("character") || sqlType.startsWith("char")) {
			javaType = "String";
		} else if (sqlType.startsWith("boolean")) {
			javaType = "Boolean";
		} else if (sqlType.startsWith("time")) {
			javaType = "LocalDateTime";
		} else if (sqlType.startsWith("date")) {
			javaType = "LocalDate";
		}
		
		return javaType;
	}
	
	private static String processSchemaName(String tableName) {
		String schemaName = "public";
		if (tableName.contains(".")) {
			schemaName = tableName.substring(0, tableName.indexOf(".")).toLowerCase();
		}
		return schemaName;
	}
	
	public static String processTableNameToJavaBeanName(String tableName) {
		String javaBeanName = "";
		if (tableName.contains(".")) {
			javaBeanName = tableName.substring(tableName.indexOf(".") + 1, tableName.length());
		}
		javaBeanName = javaBeanName.toLowerCase();
		
		String[] javaBeanNames = javaBeanName.split("_");
		javaBeanName = "";
		for (int wordPosition = 0; wordPosition < javaBeanNames.length; wordPosition++) {
			javaBeanName = javaBeanName + AutogenerateUtils.firstLetterUpperCase(javaBeanNames[wordPosition]);
		}
		
		return javaBeanName;
	}
	
	public static String processOriginalTableName(String tableName) {
		String originalTableName = "";
		if (tableName.contains(".")) {
			originalTableName = tableName.substring(tableName.indexOf(".") + 1, tableName.length());
		}
		
		return originalTableName;
	}
	
	public static String processFieldNameToJavaProperty(String fieldName) {
		String javaName = "";
		if (fieldName.contains(".")) {
			javaName = fieldName.substring(fieldName.indexOf("."), fieldName.length());
		}
		javaName = fieldName.toLowerCase();
		
		if (fieldName.contains("_")) {
			String[] javaBeanNames = javaName.split("_");
			javaName = "";
			for (int wordPosition = 0; wordPosition<javaBeanNames.length; wordPosition++) {
				javaName = javaName + AutogenerateUtils.firstLetterUpperCase(javaBeanNames[wordPosition]);
			}
		}
		javaName = javaName.substring(0, 1).toLowerCase() + javaName.substring(1);
		
		return javaName;
	}

}
