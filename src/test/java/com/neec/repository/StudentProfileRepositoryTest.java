package com.neec.repository;

import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class StudentProfileRepositoryTest {
	@Autowired
	private StudentProfileRepository studentProfileRepository;

	@Test
	void test_existsById_Existing_StudentProfileId() {
		boolean exists = studentProfileRepository.existsById(1L);
		assertFalse(exists);
	}
}
