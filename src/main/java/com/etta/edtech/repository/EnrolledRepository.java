package com.etta.edtech.repository;

import com.etta.edtech.model.Enrolled;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnrolledRepository extends JpaRepository<Enrolled, Integer> {
    Enrolled findByCognitoUserId(String cognitoUserId);
    List<Enrolled> findAllByCourse_EducatorId(String educatorId);
    void deleteAllByCourse_EducatorId(String educatorId);
    Boolean existsByCognitoUserId(String cognitoUserId);
    void deleteByCognitoUserId(String cognitoUserId);
}
