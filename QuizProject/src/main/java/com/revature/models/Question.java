package com.revature.models;

import java.sql.Timestamp;
import java.util.Arrays;

public class Question {

	private int quid;
	private String description;
	private int type;
	private String[] selections;
	private String catagory;
	private Timestamp created;
	private int createdBy;

	public Question() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Question(int quid, String description, int type, String[] selections, String catagory, Timestamp created,
			int createdBy) {
		super();
		this.quid = quid;
		this.description = description;
		this.type = type;
		this.selections = selections;
		this.catagory = catagory;
		this.created = created;
		this.createdBy = createdBy;
	}

	public int getQuid() {
		return quid;
	}

	public void setQuid(int quid) {
		this.quid = quid;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String[] getSelections() {
		return selections;
	}

	public void setSelections(String[] selections) {
		this.selections = selections;
	}

	public String getCatagory() {
		return catagory;
	}

	public void setCatagory(String catagory) {
		this.catagory = catagory;
	}

	public Timestamp getCreated() {
		return created;
	}

	public void setCreated(Timestamp created) {
		this.created = created;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((catagory == null) ? 0 : catagory.hashCode());
		result = prime * result + ((created == null) ? 0 : created.hashCode());
		result = prime * result + createdBy;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + quid;
		result = prime * result + Arrays.hashCode(selections);
		result = prime * result + type;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Question other = (Question) obj;
		if (catagory == null) {
			if (other.catagory != null)
				return false;
		} else if (!catagory.equals(other.catagory))
			return false;
		if (created == null) {
			if (other.created != null)
				return false;
		} else if (!created.equals(other.created))
			return false;
		if (createdBy != other.createdBy)
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (quid != other.quid)
			return false;
		if (!Arrays.equals(selections, other.selections))
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Question [quid=" + quid + ", description=" + description + ", type=" + type + ", selections="
				+ Arrays.toString(selections) + ", catagory=" + catagory + ", created=" + created + ", createdBy="
				+ createdBy + "]";
	}

}
