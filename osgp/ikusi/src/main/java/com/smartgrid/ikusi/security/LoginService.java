package com.smartgrid.ikusi.security;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.smartgrid.ikusi.model.User;
import com.smartgrid.ikusi.repository.UserRepository;

@Service
public class LoginService implements UserDetailsService {	
		
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder bcryptEncoder;		

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = this.userRepository.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException(username);
		}
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), true,
				true, true, true, new ArrayList<>());
	}
	
	public User save(User model) {
		String pass = bcryptEncoder.encode(model.getPassword());
		model.setPassword(pass);
		return userRepository.save(model);
	}
}