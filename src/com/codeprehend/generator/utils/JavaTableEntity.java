package com.codeprehend.generator.utils;

import java.util.LinkedHashMap;
import java.util.Map;

public class JavaTableEntity {
	private String schemaName;
	private String originalTableName;
	private String tableName;
	private Map<String, String> fields = new LinkedHashMap<String, String>();
	//Hashmap with the java field name as key and original db type as value
	private Map<String, String> originalDbFields = new LinkedHashMap<String, String>();
	
	
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public Map<String, String> getFields() {
		return fields;
	}
	public void setFields(Map<String, String> fields) {
		this.fields = fields;
	}
	public String getSchemaName() {
		return schemaName;
	}
	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}
	public String getOriginalTableName() {
		return originalTableName;
	}
	public void setOriginalTableName(String originalTableName) {
		this.originalTableName = originalTableName;
	}
	public Map<String, String> getOriginalDbFields() {
		return originalDbFields;
	}
	public void setOriginalDbFields(Map<String, String> originalDbFields) {
		this.originalDbFields = originalDbFields;
	}
}
