package com.diegochavez.courses.controller;

import com.diegochavez.courses.model.Course;
import com.diegochavez.courses.service.CourseService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public Flux<Course> getCourses(@RequestParam(defaultValue = "100") int limit) {
        return courseService.getCourses(limit);
    }
}
