package com.nhp.dao;

import com.nhp.entity.*;
import com.nhp.context.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;

public class StudentDAO {
	Connection conn = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
	
	public List<Student> getListStudent(){
		try {
			String query = "SELECT * FROM Student";
			conn = new DBContext().getConnection();
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			
			List<Student> result = new ArrayList<>();
			
			while(rs.next()) {
				int Id = rs.getInt(1);
				String Name = rs.getString(2);
				int Grade = rs.getInt(3);
				String BirthDate = rs.getString(4);
				String Address = rs.getString(5);
				String Note = rs.getString(6);
				
				Student student = new Student(Id,Name,Grade,BirthDate,Address,Note);
				
				result.add(student);
			}
			
			System.out.print(result.toString());
			
			conn.close();
			
			return result;
		} catch (Exception e) {
			System.out.print("EXCEPTION GET O FILE STUDENTDAO.JAVA");
		}
		
		return null;
	}
	
	public void addStudent(Student student) {
		try {
			
			String query = "INSERT INTO Student (StudentId, StudentName, Grade, BirthDate, Address, Note) VALUES (?, ?, ?, ?, ?, ?)";
			conn = new DBContext().getConnection();
			ps = conn.prepareStatement(query);
			
			ps.setInt(1, student.get_studentId());
            ps.setString(2, student.get_studentName());
            ps.setInt(3, student.get_grade());
            ps.setString(4, student.get_birthdate());
            ps.setString(5, student.get_address());
            ps.setString(6, student.get_note());
            
            ps.executeUpdate();
            conn.close();
			
		} catch (Exception e) {
			System.out.print("EXCEPTION POST O FILE STUDENTDAO.JAVA");
		}
		
	}
	
	public void updateStudent(Student student) {
		try {
			String query = "UPDATE Student SET StudentName=?, Grade=?, BirthDate=?, Address=?, Note=? WHERE StudentId=?";
			conn = new DBContext().getConnection();
			ps = conn.prepareStatement(query);
			
			ps.setString(1, student.get_studentName());
			ps.setInt(2, student.get_grade());
			ps.setString(3, student.get_birthdate());
			ps.setString(4, student.get_address());
			ps.setString(5, student.get_note());
			ps.setInt(6, student.get_studentId());
			
			ps.executeUpdate();
			
			conn.close();
		}catch (Exception e) {
			System.out.print("EXCEPTION UPDATE O FILE STUDENTDAO.JAVA");
		}
	}
	
	public void deleteStudent(Student student) {
		try {
			String query = "DELETE FROM Student WHERE StudentId=?";
			conn = new DBContext().getConnection();
			ps = conn.prepareStatement(query);
			
			ps.setInt(1, student.get_studentId());
			
			ps.executeUpdate();
			conn.close();
			
		} catch (Exception e) {
			System.out.print("EXCEPTION DELETE O FILE STUDENTDAO.JAVA");
		}
	}
}
