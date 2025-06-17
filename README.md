Enrollment model:

package com.bartr.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "enrollment")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Enrollment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@ManyToOne
    @JoinColumn(name = "courseId", nullable = false)
	private Course course;

	@ManyToOne
    @JoinColumn(name = "learnerId", nullable = false)
	private User learner;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "enrollmentDate")
	private Date enrollmentDate;



}

Enrollment COntroller:

package com.bartr.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bartr.model.Enrollment;
import com.bartr.service.EnrollmentService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;
    
    @PostMapping("/enroll")
    public ResponseEntity<Enrollment> enroll(
        @RequestParam int learnerId,
        @RequestParam int courseId
    ) {
        return ResponseEntity.ok(enrollmentService.enrollUser(learnerId, courseId));
    }

    @GetMapping
    public ResponseEntity<List<Enrollment>> getAllEnrollments() {
        return ResponseEntity.ok(enrollmentService.getAllEnrollments());
    }

    @GetMapping("/learner/{learnerId}")
    public ResponseEntity<List<Enrollment>> getByLearner(@PathVariable int learnerId) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentsByLearner(learnerId));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Enrollment>> getByCourse(@PathVariable int courseId) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentsByCourse(courseId));
    }

    @GetMapping("/isEnrolled")
    public ResponseEntity<Boolean> isEnrolled(
        @RequestParam int learnerId,
        @RequestParam int courseId
    ) {
        return ResponseEntity.ok(enrollmentService.isEnrolled(learnerId, courseId));
    }



}

Enrollment Service:

package com.bartr.service;

import java.util.List;

import com.bartr.model.Enrollment;

public interface EnrollmentService {

    Enrollment enrollUser(int learnerId, int courseId);

    List<Enrollment> getAllEnrollments();

    List<Enrollment> getEnrollmentsByLearner(int learnerId);

    List<Enrollment> getEnrollmentsByCourse(int courseId);

    boolean isEnrolled(int learnerId, int courseId);
}


EnrollmentServiceImpl:

package com.bartr.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.bartr.model.Course;
import com.bartr.model.Enrollment;
import com.bartr.model.User;
import com.bartr.repository.CourseRepository;
import com.bartr.repository.EnrollmentRepository;
import com.bartr.repository.UserRepository;
import com.bartr.service.EnrollmentService;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class EnrollmentServiceImpl implements EnrollmentService {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;

    @Override
    public Enrollment enrollUser(int learnerId, int courseId) {
        User learner = userRepository.findById(learnerId)
            .orElseThrow(() -> new RuntimeException("Learner not found"));

        Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new RuntimeException("Course not found"));

        User creator = course.getCreator();

        // Check if already enrolled
        if (enrollmentRepository.existsByLearnerAndCourse(learner, course)) {
            throw new RuntimeException("User already enrolled in this course");
        }

        // Check XP
        int xpCost = course.getCategory().getXpCost();
        if (learner.getXp() < xpCost) {
            throw new RuntimeException("Insufficient XP to enroll in this course");
        }

        // Deduct from learner, add to creator
        learner.setXp(learner.getXp() - xpCost);
        creator.setXp(creator.getXp() + xpCost);

        userRepository.save(learner);
        userRepository.save(creator);

        Enrollment enrollment = Enrollment.builder()
            .learner(learner)
            .course(course)
            .enrollmentDate(new Date())
            .build();

        return enrollmentRepository.save(enrollment);
    }

    @Override
    public List<Enrollment> getAllEnrollments() {
        return enrollmentRepository.findAll();
    }

    @Override
    public List<Enrollment> getEnrollmentsByLearner(int learnerId) {
        User learner = userRepository.findById(learnerId)
            .orElseThrow(() -> new RuntimeException("Learner not found"));
        return enrollmentRepository.findByLearner(learner);
    }

    @Override
    public List<Enrollment> getEnrollmentsByCourse(int courseId) {
        Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new RuntimeException("Course not found"));
        return enrollmentRepository.findByCourse(course);
    }

    @Override
    public boolean isEnrolled(int learnerId, int courseId) {
    User learner = userRepository.findById(learnerId)
            .orElseThrow(() -> new RuntimeException("Learner not found"));
    Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new RuntimeException("Course not found"));
    return enrollmentRepository.existsByLearnerAndCourse(learner, course);
}



    


}



Enrollment repository:
package com.bartr.repository;

import com.bartr.model.Course;
import com.bartr.model.Enrollment;
import com.bartr.model.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {
    
    boolean existsByLearnerAndCourse(User learner, Course course);

    List<Enrollment> findByLearner(User learner);

    List<Enrollment> findByCourse(Course course);

}
