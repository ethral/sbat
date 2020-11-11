package com.sbat.booking.repository;



import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


import com.sbat.booking.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	
	Optional<User> findByUserName(String userName);

}



