package com.codeprehend.generator.code;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.codeprehend.generator.utils.JavaTableEntity;

public class CreateStrReplace {
	
	//In strReplaceV1 the variables are replaced with "FieldName" and "FieldType". In strReplaceV2 the variables are replaced with "FieldNameindex" and "FieldTypeindex"
	private static List<String> strReplaceV1 = new ArrayList<String>();
	private static List<String> strReplaceV2 = new ArrayList<String>();
	private static List<String> strReplace = new ArrayList<String>();
	
	
	public static List<String> createStrReplaceV1(List<String> strClassToGenerate, List<JavaTableEntity> tableEntities) {
		
		Map<String, String> mapFields = tableEntities.get(0).getFields();
		Map<String, String> mapOriginalDBFields = tableEntities.get(0).getOriginalDbFields();
				
		// Using counter c, we check the longest entity key that can be replace in a a row (string data). We keep the entity key in  holdEntityKey.
		// We replace the original names of the variables, the names of variables with first capital and the types of variables.
		// 
		String data = new String();
		String data2 = new String();
		int c;
		int i;
		int indexOfKeyInStr;
		
		int holdIndexOfHoldEntityKey;
		int holdIndexOfHoldDBEntityValue;
		
		String holdEntityKey = new String();
		String holdDBEntityValue = new String();
		
		for(String str : strClassToGenerate) {
			data = str;
			data2 = str;
			c = 0;
			i = 0;
			holdIndexOfHoldEntityKey = 0;
			holdIndexOfHoldDBEntityValue = 0;
			holdEntityKey = "";
			
			for (Map.Entry<String, String> entry : mapFields.entrySet()) {
				// Check if a row contains the entry original name and if the name's length is higher than those found before
				if (data.contains(entry.getKey()) && c < entry.getKey().length()){
					c = entry.getKey().length();
					holdEntityKey = entry.getKey();
					holdIndexOfHoldEntityKey = i;
				}
				
				// check if a row contains the entry's original name with first capital and if the name's length is higher than those found before
				if (data.contains(entry.getKey().substring(0, 1).toUpperCase() + entry.getKey().substring(1)) && c < entry.getKey().length()) {
					c = entry.getKey().length();
					holdEntityKey = entry.getKey();
					holdIndexOfHoldEntityKey = i;
				}
				i = i + 1;
			}
			i = 0;
			for (Map.Entry<String, String> entry : mapOriginalDBFields.entrySet()) {
				if(data.contains(entry.getValue()) && c < entry.getValue().length()) {
					c = entry.getValue().length();
					holdDBEntityValue = entry.getValue();
					holdIndexOfHoldDBEntityValue = i;
					holdEntityKey = entry.getKey();
				}
			}

			// If c is higher than 0 it means that there was found at least one name to be replace and the entity name to be change is at position i in mapFields or in mapOriginalDBFields
			if(c > 0) {
				data2 = data2.replace(mapFields.get(holdEntityKey), "FieldType" + String.valueOf(holdIndexOfHoldEntityKey));
				data2 = data2.replace(holdEntityKey.substring(0, 1).toUpperCase() + holdEntityKey.substring(1), "FieldName" + String.valueOf(holdIndexOfHoldEntityKey));
				data = data.replace(mapFields.get(holdEntityKey), "FieldType");
				data = data.replace(holdEntityKey.substring(0, 1).toUpperCase() + holdEntityKey.substring(1), "FieldName");
				
				// Create data for strReplaceV1
				// To avoid changing java key words, the replace of variables name is made each time we found the holdEntityKey and only if there is no letter in front of the name
				indexOfKeyInStr = -1;
				if(data.contains(holdEntityKey)) {
					indexOfKeyInStr = data.indexOf(holdEntityKey);
					while(indexOfKeyInStr >= 0) {
						if (!Character.isLetter(data.charAt(indexOfKeyInStr - 1)) && data.charAt(indexOfKeyInStr - 1) != '_') {
							// if in front of the variable name is not a letter than we change the name with "fieldName". 
							// the variable name is eliminated by moving to left the substring after the variable anme
							for(int k = 0; k < holdEntityKey.length(); k++) {
								data = data.substring(0,indexOfKeyInStr) + data.substring(indexOfKeyInStr + 1);
							}
							// if the variable name is not followed by any character, "fieldName" is appended at the end of the string. 
							if(indexOfKeyInStr > data.length()) {
								for(int k = 0; k < "fieldName".length(); k++)
									data = data + "fieldName".charAt(k);
							} else {
								// if the variable name is followed by other characters, "fieldName" is inserted at indexOfKeyInStr and substring is moved to right.
								for(int k = 0; k < "fieldName".length(); k++) {
									data = data.substring(0, indexOfKeyInStr + k) + "fieldName".charAt(k) + data.substring(indexOfKeyInStr + k);
								}
							}
						}
						indexOfKeyInStr = data.indexOf(holdEntityKey, indexOfKeyInStr + 1);
					}
				}
				
				indexOfKeyInStr = -1;
				if(data.contains(holdDBEntityValue)) {
					indexOfKeyInStr = data.indexOf(holdDBEntityValue);
					while (indexOfKeyInStr > 0) {
						if (!Character.isLetter(data.charAt(indexOfKeyInStr - 1))) {
							for (int k = 0; k < holdDBEntityValue.length(); k++) {
								data = data.substring(0, indexOfKeyInStr) + data.substring(indexOfKeyInStr + 1);
							}
							if(indexOfKeyInStr > data.length()) {
								for(int k = 0; k < "field_name".length(); k++) {
									data = data + "field_name".charAt(k);
								}
							} else {
								for (int k = 0; k < "field_name".length(); k ++) {
									data = data.substring(0, indexOfKeyInStr + k) + "field_name".charAt(k) + data.substring(indexOfKeyInStr + k);
								}
							}
						}
						indexOfKeyInStr = data.indexOf(holdDBEntityValue, indexOfKeyInStr + 1);
					}
				}
				
				// Create data2 for strReplaceV2
				// To avoid changing java key words, the replace of variables name is made each time we found the holdEntityKey and only if there is no letter in front of the name
				// indexOfKeyInStr is used to go through the string and find the index at which there is a variable name
				indexOfKeyInStr = -1;
				if(data2.contains(holdEntityKey)) {
					indexOfKeyInStr = data2.indexOf(holdEntityKey);
					while(indexOfKeyInStr >= 0) {
						if (!Character.isLetter(data2.charAt(indexOfKeyInStr - 1))) {
							// delete in the string the variable name that needs to be changed by concatenating the substrings in front and after the variable name
							for(int k = 0; k < holdEntityKey.length(); k++) {
								data2 = data2.substring(0,indexOfKeyInStr) + data2.substring(indexOfKeyInStr + 1);
							}
							// append at the end of string fieldName in case there is nothing after the variable name
							if(indexOfKeyInStr > data2.length()) {
								for(int k = 0; k < ("fieldName"+ String.valueOf(holdIndexOfHoldEntityKey)).length(); k++) {
									data2 = data2 + ("fieldName"+ String.valueOf(holdIndexOfHoldEntityKey)).charAt(k);
								}
							} 
							// insert fieldName at the indexOfKeyInStr and move to the right the substring after indexOfKeyInStr
							else {
								for(int k = 0; k < ("fieldName"+ String.valueOf(holdIndexOfHoldEntityKey)).length(); k++) {
									data2 = data2.substring(0, indexOfKeyInStr + k) + ("fieldName"+ String.valueOf(holdIndexOfHoldEntityKey)).charAt(k) + data2.substring(indexOfKeyInStr + k);
								}
							}
							
							//data2 = data2.replace(holdEntityKey, "fieldName" + String.valueOf(holdIndexOfHoldEntityKey));
							//data = data.replace(holdEntityKey, "fieldName");
						}
						indexOfKeyInStr = data2.indexOf(holdEntityKey, indexOfKeyInStr + 1);
					}
				}
				
				indexOfKeyInStr = -1;
				if(data2.contains(holdDBEntityValue)) {
					indexOfKeyInStr = data2.indexOf(holdDBEntityValue);
					while (indexOfKeyInStr > 0) {
						if (!Character.isLetter(data2.charAt(indexOfKeyInStr - 1))) {
							for (int k = 0; k < holdDBEntityValue.length(); k++) {
								data2 = data2.substring(0, indexOfKeyInStr) + data2.substring(indexOfKeyInStr + 1);
							}
							if(indexOfKeyInStr > data2.length()) {
								for(int k = 0; k < ("field_name"+ String.valueOf(holdIndexOfHoldDBEntityValue)).length(); k++) {
									data2 = data2 + ("field_name"+ String.valueOf(holdIndexOfHoldDBEntityValue)).charAt(k);
								}
							} else {
								for (int k = 0; k < ("field_name"+ String.valueOf(holdIndexOfHoldDBEntityValue)).length(); k ++) {
									data2 = data2.substring(0, indexOfKeyInStr + k) + ("field_name"+ String.valueOf(holdIndexOfHoldDBEntityValue)).charAt(k) + data2.substring(indexOfKeyInStr + k);
								}
							}
						}
						indexOfKeyInStr = data.indexOf(holdDBEntityValue, indexOfKeyInStr + 1);
					}
				}
				
			}
			
			strReplaceV1.add(data);
			strReplaceV2.add(data2);
		}
		
		System.out.println("\nVerificare replace:");
		for (String str : strReplaceV1) {
			System.out.println(str);
			strReplace.add(str);
		}
		
		strReplace.add("demarcation line");
		for (String str : strReplaceV2) {
			//System.out.println(str);
			strReplace.add(str);
		}
		
		return strReplace;
	}

}
