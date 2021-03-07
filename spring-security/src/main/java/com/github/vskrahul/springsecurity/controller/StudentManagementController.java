package com.github.vskrahul.springsecurity.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.vskrahul.springsecurity.model.Student;

@RestController
@RequestMapping("management/api/v1/students")
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class StudentManagementController {

	private static final List<Student> STUDENTS = new ArrayList<>();
	
	static {
		STUDENTS.add(new Student(1, "Rahul Vishvakarma"));
		STUDENTS.add(new Student(2, "Rohit Sharma"));
		STUDENTS.add(new Student(3, "Ankit Vishvakarma"));
	}
	
	@GetMapping
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ADMINTRAINEE')")
	public List<Student> getAllStudents() {
		return STUDENTS;
	}
	
	@PostMapping
	@PreAuthorize("hasAuthority('student:write')")
	public void registerNewStudent(@RequestBody Student student) {
		STUDENTS.add(student);
	}
	
	@DeleteMapping(path = "{studentId}")
	@PreAuthorize("hasAuthority('student:write')")
	public Student deleteStudent(@PathVariable("studentId") final Integer studentId) {
		int idx = STUDENTS.indexOf(new Student(studentId, ""));
		if(idx != -1 && idx < STUDENTS.size())
			return STUDENTS.remove(idx);
		throw new IllegalArgumentException("Invalid student Id " + studentId);
	}
	
	@PutMapping
	@PreAuthorize("hasAuthority('student:write')")
	public Student updateStudent(@RequestBody Student student) {
		int idx = STUDENTS.indexOf(student);
		if(idx != -1 && idx < STUDENTS.size()) {
			Student st = STUDENTS.get(idx);
			return student;
		}
		throw new IllegalArgumentException("Invalid student Id " + student.getStudentId());
	}
}
