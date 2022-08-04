package com.ytvideoshare.backend.service;

import com.ytvideoshare.backend.Email.EmailService;
import com.ytvideoshare.backend.Exception.DuplicateEmailException;
import com.ytvideoshare.backend.domain.AppUser;
import com.ytvideoshare.backend.domain.Role;
import com.ytvideoshare.backend.dto.AppUserRequest;
import com.ytvideoshare.backend.dto.AppUserResponse;
import com.ytvideoshare.backend.repo.AppUserRepo;
import com.ytvideoshare.backend.repo.RoleRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AppUserService implements UserDetailsService {
    private final AppUserRepo appUserRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;
    @Autowired private EmailService emailService;

    /**
     * Maps built in UserDetailService to our custom user model: AppUserService
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUser appUser = appUserRepo.findAppUserByEmail(email);
        if(appUser == null){
            log.error("User not found");
            throw new UsernameNotFoundException("User not found");
        }
        else{
            log.info("User found {}", email);
        }

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(appUser.getRole().getName()));
        return new User(appUser.getEmail(), appUser.getPassword(), authorities);
    }

    /**
     * Returns User Response DTO given the email
     * @param email A String literal
     * @return the user
     */
    public AppUserResponse getUserResponse(String email){
        return new AppUserResponse(appUserRepo.findAppUserByEmail(email));
    }

    /**
     * Returns user given the email
     * @param email A String literal
     * @return the user
     */
    public AppUser getUser(String email){
        return appUserRepo.findAppUserByEmail(email);
    }

    /**
     * Saved the AppUser to the database
     * Checks
     *  - If No name or email was given
     *  - If email already exists
     *  - If password is less than 8 characters
     * @param userRequest A AppUserRequest DTO Object
     * @param role A AppUserRequest DTO Object
     * @return Saved AppUser
     */
    public AppUserResponse saveUser(AppUserRequest userRequest, Role role, String siteurl) throws DuplicateEmailException {

        if(appUserRepo.findAppUserByEmail(userRequest.getEmail()) != null){
            throw new DuplicateEmailException("User with email already exists.");
        }

        AppUser user = new AppUser();
        user.setName(userRequest.getName());
        user.setEmail(userRequest.getEmail());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setRole(role);
        user.setVerificationCode((int)(Math.random()*(999999-100000+1)+100000 ));  // random number between 100000 and 999999

        AppUser savedUser =  appUserRepo.save(user);
        emailService.sendVerificationCode(savedUser, siteurl);
        return new AppUserResponse(savedUser);
    }

    /**
     * Verify user given email and verification code
     * @param email User phone
     * @param code Verification Code
     * @return True if succeeded False otherwise
     */
    public Boolean verifyUser(String email, int code){
        AppUser user = appUserRepo.findAppUserByEmail(email);
        if(code == user.getVerificationCode()){
            appUserRepo.verifyAppUserByEmail(email);
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Save a role | Used by Admin
     * @param role role object
     * @return saved role
     */
    public Role saveRole(Role role){
        log.info("Saving new role {}.", role.getName());
        return roleRepo.save(role);
    }

    /**
     * Get the Role given a roleName
     * @param roleName A String literal
     * @return A Role Object
     */
    public Role getRole(String roleName){
        return roleRepo.findRoleByName(roleName);
    }

}
