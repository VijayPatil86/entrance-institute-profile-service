package com.neec.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.neec.entity.StudentProfile;

public interface StudentProfileRepository extends JpaRepository<StudentProfile, Long> {

}
