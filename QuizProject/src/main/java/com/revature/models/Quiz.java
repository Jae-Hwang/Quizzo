package com.revature.models;

import java.sql.Timestamp;

public class Quiz {

	private int qid;
	private String name;
	private Timestamp created;
	private int createdBy;

	public Quiz() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Quiz(int qid, String name, Timestamp created, int createdBy) {
		super();
		this.qid = qid;
		this.name = name;
		this.created = created;
		this.createdBy = createdBy;
	}

	public int getQid() {
		return qid;
	}

	public void setQid(int qid) {
		this.qid = qid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
		result = prime * result + ((created == null) ? 0 : created.hashCode());
		result = prime * result + createdBy;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + qid;
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
		Quiz other = (Quiz) obj;
		if (created == null) {
			if (other.created != null)
				return false;
		} else if (!created.equals(other.created))
			return false;
		if (createdBy != other.createdBy)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (qid != other.qid)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Quiz [qid=" + qid + ", name=" + name + ", created=" + created + ", createdBy=" + createdBy + "]";
	}

}
