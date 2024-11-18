package ru.skypro.homework.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Login;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.service.UserService;

import java.io.IOException;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private Logger logger = LoggerFactory.getLogger(UserController.class);


    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/set_password")
    public ResponseEntity<?> setPassword(@RequestBody UserEntity user) {
        if(userService.setPassword(user.getUserName(), user.getPassword())){
            return ResponseEntity.ok().build();
        }else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
    @GetMapping("/me/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        String userName = userService.getUser(id);
        if(!userName.isEmpty()){
            return ResponseEntity.ok().body(userName);

        }else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
    @PostMapping("/")
    public ResponseEntity<?> updateUser(@RequestBody UserEntity user) {
        if(userService.updateUser(user)){
            return ResponseEntity.ok().build();
        }else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
    @PostMapping("/{id}/image")
    public void uploadImage(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            userService.updateProfileImage(id, file);
        }catch (IOException e){
            logger.error("Ошибка при загрузке изображения", e);
            throw new RuntimeException(e);
        }
    }

}
