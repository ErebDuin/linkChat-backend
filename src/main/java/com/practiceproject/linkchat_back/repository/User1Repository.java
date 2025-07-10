package com.practiceproject.linkchat_back.repository;

import com.practiceproject.linkchat_back.model.User1;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface User1Repository extends JpaRepository<User1, Long> {
    User1 findByEmail(String email);
}