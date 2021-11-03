package com.rebel.youtube.model;

import java.util.List;

public class DataGsonModel {
	 Boolean status;
	 String Message;
	 List<PersonModel> data;

	/**
	 * @return the status
	 */
	public Boolean getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(Boolean status) {
		this.status = status;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return Message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		Message = message;
	}

	/**
	 * @return the personModels
	 */
	public List<PersonModel> getPersonModels() {
		return data;
	}

	/**
	 * @param personModels the personModels to set
	 */
	public void setPersonModels(List<PersonModel> personModels) {
		this.data = personModels;
	}

	@Override
	public String toString() {
		return "DataGsonModel [status=" + status + ", Message=" + Message + ", personModels=" + data + "]";
	}

}
