package com.phdprogress.phd_progress.repository;

import com.phdprogress.phd_progress.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}