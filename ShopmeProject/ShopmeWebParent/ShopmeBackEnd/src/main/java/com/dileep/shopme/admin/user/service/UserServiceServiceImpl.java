package com.dileep.shopme.admin.user.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dileep.shopme.admin.exception.UserNotFoundException;
import com.dileep.shopme.admin.user.RoleRepository;
import com.dileep.shopme.admin.user.UserRepository;
import com.dileep.shopme.admin.user.UserService;
import com.dileep.shopme.common.entity.Role;
import com.dileep.shopme.common.entity.User;

@Service
@Transactional
public class UserServiceServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public List<User> listAll() {
		return (List<User>) userRepository.findAll();
	}

	@Override
	public List<Role> listRoles() {
		// TODO Auto-generated method stub
		return roleRepository.findAll();
	}

	@Override
	public User saveUser(User user) {
		boolean isUpdatingUser = (user.getId() != null);

		if (isUpdatingUser) {
			User existingUser = userRepository.findById(user.getId()).get();
			if (user.getPassword().isEmpty()) {
				user.setPassword(existingUser.getPassword());
			} else {
				encodePassword(user);
			}
		} else {
			encodePassword(user);
		}
		return userRepository.save(user);
	}

	private void encodePassword(User user) {
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
	}

	@Override
	public boolean isEmailUnique(Integer id, String email) {
		User userByEmail = userRepository.getUserByEmail(email);
		System.out.println(userByEmail);
		if (userByEmail == null)
			return true;
		System.out.println();
		boolean isCreatingNew = (id == null);

		if (isCreatingNew) {
			if (userByEmail != null)
				return false;
		} else {
			if (userByEmail.getId() != id)
				return false;
		}
		return true;
	}

	@Override
	public User get(Integer id) throws UserNotFoundException {
		try {
			return userRepository.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new UserNotFoundException("Could not find any user with ID" + id);
		}

//		return null;
	}

	@Override
	public void delete(Integer id) throws UserNotFoundException {
		Long countById = userRepository.countById(id);
		
		if(countById == null || countById == 0) {
			throw new UserNotFoundException("Could not find any user with ID" + id);
		}
		userRepository.deleteById(id);
	}

	@Override
	public void updateUserenableStatus(Integer id, boolean enabled) {
		// TODO Auto-generated method stub
		userRepository.updateEnabledStatus(id, enabled);
	}
	
	
}
