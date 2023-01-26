package com.mfadhili.registration.rest;

import com.mfadhili.registration.dto.Users;
import com.mfadhili.registration.exception.CustomErrorType;
import com.mfadhili.registration.repository.UsersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class Controller {
    public static final Logger logger = LoggerFactory.getLogger(Controller.class);

    private UsersRepository usersRepository;

    @Autowired
    public Controller(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @GetMapping("/")
    public ResponseEntity<List<Users>> listAllUsers() {
        logger.info("Fetching all users");
        List<Users> users = usersRepository.findAll();
        if (users.isEmpty()) {
            return new ResponseEntity<List<Users>>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<Users>>(users, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Users> getUserById(@PathVariable("id") final Long id) {
        logger.info("Fetching User with id {}", id);
        Users user = usersRepository.findById2(id);
        if (user == null) {
            logger.error("User with id {} not found.", id);
            return new ResponseEntity<>(new CustomErrorType("User with id " + id + " not found"), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<Users>(user, HttpStatus.OK);
    }

    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Users> createUser(@RequestBody final Users user) {
        logger.info("Creating User : {}", user);
        if (usersRepository.findByName(user.getName()) != null) {
            logger.error("Unable to create. A User with name {} already exist", user.getName());
            return new ResponseEntity<Users>(
                    new CustomErrorType(
                            "Unable to create new user. A User with name " + user.getName() + " already exist."),
                    HttpStatus.CONFLICT);
        }
        usersRepository.save(user);
        return new ResponseEntity<Users>(user, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Users> updateUser(@PathVariable("id") final Long id, @RequestBody Users user) {
        logger.info("Updating User with id {}", id);
        Users currentUser = usersRepository.findById2(id);
        if (currentUser == null) {
            logger.error("Unable to update. User with id {} not found.", id);
            return new ResponseEntity<Users>(
                    new CustomErrorType("Unable to upate. User with id " + id + " not found."), HttpStatus.NOT_FOUND);
        }
        currentUser.setName(user.getName());
        currentUser.setAddress(user.getAddress());
        currentUser.setEmail(user.getEmail());
        usersRepository.saveAndFlush(currentUser);
        return new ResponseEntity<Users>(currentUser, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Users> deleteUser(@PathVariable("id") final Long id) {
        logger.info("Deleting User with id {}", id);
        Users user = usersRepository.findById2(id);
        if (user == null) {
            logger.error("Unable to delete. User with id {} not found.", id);
            return new ResponseEntity<Users>(
                    new CustomErrorType("Unable to delete. User with id " + id + " not found."), HttpStatus.NOT_FOUND);
        }
        usersRepository.deleteById(id);
        return new ResponseEntity<Users>(new CustomErrorType("Deleted User with id " + id + "."),
                HttpStatus.NO_CONTENT);
    }


}
