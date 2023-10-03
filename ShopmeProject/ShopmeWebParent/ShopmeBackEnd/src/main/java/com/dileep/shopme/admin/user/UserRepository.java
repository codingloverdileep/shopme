package com.dileep.shopme.admin.user;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.dileep.shopme.common.entity.User;

public interface UserRepository extends CrudRepository<User, Integer>{

	@Query("SELECT u From User u WHERE u.email=:email")
	public User getUserByEmail(@Param("email") String email);
	
	public Long countById(Integer id);

	@Modifying
	@Query("UPDATE  User u SET u.enabled =?2 WHERE u.id= ?1")
	public void updateEnabledStatus(Integer id , boolean enabled);
	
}