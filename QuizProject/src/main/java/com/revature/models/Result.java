package com.revature.models;

import java.sql.Timestamp;

public class Result {

	private int userid;
	private int qid;
	private int grade;
	private String answers;
	private Timestamp finished;

	public Result() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Result(int userid, int qid, int grade, String answers, Timestamp finished) {
		super();
		this.userid = userid;
		this.qid = qid;
		this.grade = grade;
		this.answers = answers;
		this.finished = finished;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public int getQid() {
		return qid;
	}

	public void setQid(int qid) {
		this.qid = qid;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public String getAnswers() {
		return answers;
	}

	public void setAnswers(String answers) {
		this.answers = answers;
	}

	public Timestamp getFinished() {
		return finished;
	}

	public void setFinished(Timestamp finished) {
		this.finished = finished;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((answers == null) ? 0 : answers.hashCode());
		result = prime * result + ((finished == null) ? 0 : finished.hashCode());
		result = prime * result + grade;
		result = prime * result + qid;
		result = prime * result + userid;
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
		Result other = (Result) obj;
		if (answers == null) {
			if (other.answers != null)
				return false;
		} else if (!answers.equals(other.answers))
			return false;
		if (finished == null) {
			if (other.finished != null)
				return false;
		} else if (!finished.equals(other.finished))
			return false;
		if (grade != other.grade)
			return false;
		if (qid != other.qid)
			return false;
		if (userid != other.userid)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Result [userid=" + userid + ", qid=" + qid + ", grade=" + grade + ", answers=" + answers + ", finished="
				+ finished + "]";
	}

}
