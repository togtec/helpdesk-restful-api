package com.rodrigo.helpdesk.resources.exceptions;

import java.io.Serializable;

public class FieldMessage implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String fieldName;
	private String defaultMessage;
	
	public FieldMessage() {			
		super();
	}

	public FieldMessage(String fieldName, String defaultMessage) {
		super();
		this.fieldName = fieldName;
		this.defaultMessage = defaultMessage;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getDefaultMessage() {
		return defaultMessage;
	}

	public void setDefaultMessage(String defaultMessage) {
		this.defaultMessage = defaultMessage;
	}
	
}
