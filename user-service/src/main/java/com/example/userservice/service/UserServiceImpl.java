package com.example.userservice.service;

import com.example.userservice.client.OrderServiceClient;
import com.example.userservice.dto.UserDto;
import com.example.userservice.error.FeignErrorDecoder;
import com.example.userservice.jpa.UserEntity;
import com.example.userservice.jpa.UserRepository;
import com.example.userservice.mapper.UserMapper;
import com.example.userservice.vo.ResponseOrder;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  private final BCryptPasswordEncoder passwordEncoder;

  private final Environment env;

  private final RestTemplate restTemplate;

  private final OrderServiceClient orderServiceClient;

  private final CircuitBreakerFactory circuitBreakerFactory;


//  private final FeignErrorDecoder feignErrorDecoder;

//  private UserMapper userMapper= Mappers.getMapper(UserMapper.class);
//  private UserMapper userMapper;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    UserEntity userEntity = userRepository.findByEmail(username);

    if (userEntity == null) throw new UsernameNotFoundException(username);

    return new User(userEntity.getEmail(), userEntity.getEncryptedPwd(),
            true, true, true, true
            , new ArrayList<>());
  }

  @Override
  public UserDto createUser(UserDto userDto) {
    userDto.setUserId(UUID.randomUUID().toString());

    ModelMapper mapper = new ModelMapper();
    mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    UserEntity userEntity = mapper.map(userDto, UserEntity.class);
    userEntity.setEncryptedPwd(passwordEncoder.encode(userDto.getPwd()));

//    UserEntity userEntity = userMapper.userDtoToEntity(userDto);
//    userEntity.setEncryptedPwd(passwordEncoder.encode(userDto.getPwd()));

    UserEntity save = userRepository.save(userEntity);

    UserDto returnUserDto = mapper.map(save, UserDto.class);
//    UserDto returnUserDto = userMapper.userEntityToDto(save);
    return returnUserDto;
  }

  @Override
  public UserDto getUserByUserId(String userId) {
    UserEntity userEntity = userRepository.findByUserId(userId);

    if (userEntity == null) {
      throw new UsernameNotFoundException("User not found");
    }

    UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);

//    String orderServiceUrl = env.getProperty("order_service.url");

    /* Using as rest template */
//    String orderUrl = String.format(env.getProperty("order_service.url"), userId);
//    ResponseEntity<List<ResponseOrder>> orderListResponse = restTemplate.exchange(
//            orderUrl, HttpMethod.GET,null, new ParameterizedTypeReference<List<ResponseOrder>>() {});
//    List<ResponseOrder> orders = orderListResponse.getBody();

    /* Using as feign client */
    /* Feign exception handling */
//    List<ResponseOrder> orders = null;
//    try {
//      orders = orderServiceClient.getOrders(userId);
//    } catch (FeignException e) {
//      log.error(e.getMessage());
//    }
//    List<ResponseOrder> orders = orderServiceClient.getOrders(userId);

    /* circuitbreaker */
    log.info("Before call orders microservice");
    CircuitBreaker circuitbreaker = circuitBreakerFactory.create("circuitbreaker");
    List<ResponseOrder> run = circuitbreaker.run(() -> orderServiceClient.getOrders(userId), throwable -> new ArrayList<>());
    log.info("After call orders microservice");

    userDto.setOrders(run);

    return userDto;
  }

  @Override
  public Iterable<UserEntity> getUserByAll() {
    return userRepository.findAll();
  }

  @Override
  public UserDto getUserDetailsByEmail(String userName) {
    UserEntity userEntity = userRepository.findByEmail(userName);

    if (userEntity == null)
      throw new UsernameNotFoundException(userName);

    UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);
    return userDto;
  }

}


