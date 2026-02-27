package com.diegochavez.courses.service;

import com.diegochavez.courses.model.Course;
import com.diegochavez.courses.repository.CourseRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public Flux<Course> getCourses(int limit) {
        return courseRepository.findAll(limit);
    }
}
