package com.example.jpa.enums;

public enum ApiTokenStatus {
	DEFAULT(-1,"DEFAULT"),
	ACTIVE(0,"ACTIVE"),
	STOPPED(1,"STOPPED");
	
	private final Integer value;
	private final String label;
	
	private ApiTokenStatus(Integer value, String label){
		this.value = value;
		this.label = label;
	}

	public Integer getValue() {
		return value;
	}

	public String getLabel() {
		return label;
	}
	
	public static ApiTokenStatus valueOf(Integer value) {
		for (ApiTokenStatus status : ApiTokenStatus.values()) {
			if (value == status.getValue()) {
				return status;
			}
		}
		return DEFAULT;
	}
}
