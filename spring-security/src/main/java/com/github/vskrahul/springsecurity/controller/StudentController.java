package com.github.vskrahul.springsecurity.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.github.vskrahul.springsecurity.model.Student;
import com.github.vskrahul.springsecurity.util.IpAddressUtil;
import com.github.vskrahul.springsecurity.util.JsonUtil;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("api/v1/students")
@Slf4j
public class StudentController {
	
	private static final List<Student> STUDENTS = new ArrayList<>();
	
	static {
		STUDENTS.add(new Student(1,"Rahul Vishvakarma"));
		STUDENTS.add(new Student(2, "Rohit Sharma"));
		STUDENTS.add(new Student(3, "Ankit Vishvakarma"));
	}

	@GetMapping(path= {"/{studentId}", "/anything"})
	public Student getStudent(@PathVariable("studentId") Integer studentId
								,HttpServletRequest request) {
		log.info("Looking for Student with Id: {}", studentId);
		Student student = null;
		
		if(Objects.nonNull(studentId)) {
			student = STUDENTS.stream()
						.filter(s -> s.getStudentId() == studentId)
						.findFirst()
						.orElseThrow(() -> new IllegalStateException("Student with Id: " + studentId + " doesn't exists"));
		}
		log.info("request receied from IP: {}", IpAddressUtil.retrieveIpAddress(request));
		log.info("[method=getStudent] [studentId={}] [student={}]", studentId, JsonUtil.toJsonString(student));
		return student;
	}
	
	@ExceptionHandler
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public String handleException(Exception e) {
		return e.getMessage();
	}
}