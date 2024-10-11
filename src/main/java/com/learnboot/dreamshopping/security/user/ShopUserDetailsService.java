package com.learnboot.dreamshopping.security.user;

import com.learnboot.dreamshopping.exception.ResourceNotFoundException;
import com.learnboot.dreamshopping.models.User;
import com.learnboot.dreamshopping.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShopUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

       User user =  Optional.ofNullable(userRepository.findByEmail(username))
                .orElseThrow(()-> new UsernameNotFoundException("User Not Found!"));

        return ShopUserDetails.buildUserDetails(user);
    }
}
