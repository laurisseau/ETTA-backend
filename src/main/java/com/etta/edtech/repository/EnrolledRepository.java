package com.etta.edtech.repository;

import com.etta.edtech.model.Course;
import com.etta.edtech.model.Enrolled;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnrolledRepository extends JpaRepository<Enrolled, Integer> {
}
