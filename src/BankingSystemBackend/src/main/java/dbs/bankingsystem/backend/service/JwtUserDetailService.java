package dbs.bankingsystem.backend.service;

import dbs.bankingsystem.backend.repositories.*;
import dbs.bankingsystem.backend.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.ArrayList;

/**
 * @author: ANG KUO SHENG CLEMENT
 * @date: 9-Sep-2023
 */

@Service("userDetailService")
@ComponentScan(basePackages = "com.test.project.security.model")
public class JwtUserDetailService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			User user = userRepository.findByUsername(username);
			if (user == null) {
				throw new UsernameNotFoundException("User not found with mobile no: " + username);
			}
			return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
		} catch (Exception e){
			throw new UsernameNotFoundException("User not found with mobile no: " + username);
		}
	}

}
