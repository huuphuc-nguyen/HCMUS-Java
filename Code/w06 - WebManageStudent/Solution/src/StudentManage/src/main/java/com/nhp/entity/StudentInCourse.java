package com.nhp.entity;

public class StudentInCourse {
	// Attribute
		private int _studentId;
		private String _studentName;
		private int _grade;
		private String _birthdate;
		private String _address;
		private String _note;
		private float _score;
		public int get_studentId() {
			return _studentId;
		}
		public void set_studentId(int _studentId) {
			this._studentId = _studentId;
		}
		public String get_studentName() {
			return _studentName;
		}
		public void set_studentName(String _studentName) {
			this._studentName = _studentName;
		}
		public int get_grade() {
			return _grade;
		}
		public void set_grade(int _grade) {
			this._grade = _grade;
		}
		public String get_birthdate() {
			return _birthdate;
		}
		public void set_birthdate(String _birthdate) {
			this._birthdate = _birthdate;
		}
		public String get_address() {
			return _address;
		}
		public void set_address(String _address) {
			this._address = _address;
		}
		public String get_note() {
			return _note;
		}
		public void set_note(String _note) {
			this._note = _note;
		}
		public float get_score() {
			return _score;
		}
		public void set_score(float _score) {
			this._score = _score;
		}
		public StudentInCourse(int _studentId, String _studentName, int _grade, String _birthdate, String _address,
				String _note, float _score) {
			super();
			this._studentId = _studentId;
			this._studentName = _studentName;
			this._grade = _grade;
			this._birthdate = _birthdate;
			this._address = _address;
			this._note = _note;
			this._score = _score;
		}
		@Override
		public String toString() {
			return "StudentInCourse [_studentId=" + _studentId + ", _studentName=" + _studentName + ", _grade=" + _grade
					+ ", _birthdate=" + _birthdate + ", _address=" + _address + ", _note=" + _note + ", _score="
					+ _score + "]";
		}
		
}
