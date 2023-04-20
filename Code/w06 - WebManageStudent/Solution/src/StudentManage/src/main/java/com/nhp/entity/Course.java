package com.nhp.entity;

public class Course {
	
	// Attribute
	private int _courseId;
	private String _courseName;
	private String _lecture;
	private int _year;
	private String _note;
	
	// Constructor
	public Course(int _courseId, String _courseName, String _lecture, int _year, String _note) {
		this._courseId = _courseId;
		this._courseName = _courseName;
		this._lecture = _lecture;
		this._year = _year;
		this._note = _note;
	}
	
	// Getter Setter
	public int get_courseId() {
		return _courseId;
	}
	public void set_courseId(int _courseId) {
		this._courseId = _courseId;
	}
	public String get_courseName() {
		return _courseName;
	}
	public void set_courseName(String _courseName) {
		this._courseName = _courseName;
	}
	public String get_lecture() {
		return _lecture;
	}
	public void set_lecture(String _lecture) {
		this._lecture = _lecture;
	}
	public int get_year() {
		return _year;
	}
	public void set_year(int _year) {
		this._year = _year;
	}
	public String get_note() {
		return _note;
	}
	public void set_note(String _note) {
		this._note = _note;
	}
	
	
	@Override
	public String toString() {
		return "Course [_courseId=" + _courseId + ", _courseName=" + _courseName + ", _lecture=" + _lecture + ", _year="
				+ _year + ", _note=" + _note + "]";
	}
}
