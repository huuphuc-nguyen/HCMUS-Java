package com.nhp.entity;

public class CourseScore {
	private int _studentId;
	private int _courseId;
	private float _score;
	public int get_studentId() {
		return _studentId;
	}
	public void set_studentId(int _studentId) {
		this._studentId = _studentId;
	}
	public int get_courseId() {
		return _courseId;
	}
	public void set_courseId(int _courseId) {
		this._courseId = _courseId;
	}
	public float get_score() {
		return _score;
	}
	public void set_score(float _score) {
		this._score = _score;
	}
	public CourseScore(int _studentId, int _courseId, float _score) {
		super();
		this._studentId = _studentId;
		this._courseId = _courseId;
		this._score = _score;
	}
	@Override
	public String toString() {
		return "CourseScore [_studentId=" + _studentId + ", _courseId=" + _courseId + ", _score=" + _score + "]";
	}
	
	
	
}
