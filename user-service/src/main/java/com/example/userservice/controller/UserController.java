package com.example.userservice.controller;

import com.example.userservice.dto.UserDto;
import com.example.userservice.jpa.UserEntity;
import com.example.userservice.service.UserService;
import com.example.userservice.vo.Greeting;
import com.example.userservice.vo.RequestUser;
import com.example.userservice.vo.ResponseUser;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class UserController {

  private final Environment env;
//    return env.getProperty("greeting.message");

  private final Greeting greeting;

  private final UserService userService;

  @GetMapping("/health-check")
  @Timed(value = "users.status", longTask = true)
  public String status() {
    return String.format("It`s Working in User Service " +
                    "port(local.server.port)=%s, " +
                    "port(server.port)=%s, " +
                    "token secret=%s, " +
                    "token expiration=%s",
            env.getProperty("local.server.port"),
            env.getProperty("server.port"),
            env.getProperty("token.secret"),
            env.getProperty("token.expiration_time"));
  }

  @GetMapping("/welcome")
  @Timed(value = "users.welcome", longTask = true)
  public String welcome() {
    return "Welcome user-service!!";
  }

  @PostMapping("/users")
  public ResponseEntity<ResponseUser> createUser(@RequestBody RequestUser user) {
    ModelMapper mapper = new ModelMapper();
    mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    UserDto userDto = mapper.map(user, UserDto.class);
    UserDto returnUserDto = userService.createUser(userDto);
    ResponseUser responseUser = mapper.map(returnUserDto, ResponseUser.class);
    return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
  }

  @GetMapping("/users")
  public ResponseEntity<List<ResponseUser>> getUsers() {
    Iterable<UserEntity> users = userService.getUserByAll();

    List<ResponseUser> result = new ArrayList<>();
    users.forEach(c -> result.add(new ModelMapper().map(c, ResponseUser.class)));

    return ResponseEntity.status(HttpStatus.OK).body(result);
  }

  @GetMapping("/users/{userId}")
  public ResponseEntity<ResponseUser> getUser(@PathVariable("userId") String userId) {
    UserDto userDto = userService.getUserByUserId(userId);
    ResponseUser result = new ModelMapper().map(userDto, ResponseUser.class);
    return ResponseEntity.status(HttpStatus.OK).body(result);
  }
}
