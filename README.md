package com.ctrlaltdefeat.Bartr.service;

import com.ctrlaltdefeat.Bartr.model.Course;
import com.ctrlaltdefeat.Bartr.model.User;
import com.ctrlaltdefeat.Bartr.repository.CourseRepository;
import com.ctrlaltdefeat.Bartr.repository.EnrollmentRepository;
import com.ctrlaltdefeat.Bartr.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public EnrollmentService(EnrollmentRepository enrollmentRepository,
                             UserRepository userRepository,
                             CourseRepository courseRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
    }

    public String enroll(String userId, String courseId) {
        // 1. Get user and course from Appwrite
        User user = userRepository.getDocument(userId);
        Course course = courseRepository.getDocument(courseId);

        int userXp = user.getXp();
        int xpCost = course.getXpCost();

        if (userXp < xpCost) {
            throw new RuntimeException("Insufficient XP to enroll in this course.");
        }

        // 2. Deduct XP from user
        user.setXp(userXp - xpCost);
        Map<String, Object> updatedUser = objectMapper.convertValue(user, Map.class);
        userRepository.updateDocument(userId, updatedUser);

        // 3. Credit XP to creator (instructor)
        String creatorId = course.getCreatorId();
        User creator = userRepository.getDocument(creatorId);
        creator.setXp(creator.getXp() + xpCost);
        Map<String, Object> updatedCreator = objectMapper.convertValue(creator, Map.class);
        userRepository.updateDocument(creatorId, updatedCreator);

        // 4. Create enrollment
        Map<String, Object> enrollmentData = new HashMap<>();
        enrollmentData.put("userId", userId);
        enrollmentData.put("courseId", courseId);

        return enrollmentRepository.createDocument(enrollmentData);
    }
}
