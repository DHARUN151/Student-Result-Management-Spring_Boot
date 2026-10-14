package com.studentresult.management.service;

import com.studentresult.management.entity.Student;
import com.studentresult.management.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(Integer id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + id));
    }
    public Student addStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student updateStudent(Integer id, Student studentDetails) {

        Student student = studentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Student not found with ID: " + id));

        student.setRegNum(studentDetails.getRegNum());
        student.setName(studentDetails.getName());
        student.setDob(studentDetails.getDob());
        student.setGender(studentDetails.getGender());
        student.setPhone(studentDetails.getPhone());
        student.setEmail(studentDetails.getEmail());

        return studentRepository.save(student);
    }

    public void deleteStudent(Integer id) {

        if (!studentRepository.existsById(id)) {
            throw new RuntimeException(
                    "Student not found with ID: " + id);
        }

        studentRepository.deleteById(id);
    }
}