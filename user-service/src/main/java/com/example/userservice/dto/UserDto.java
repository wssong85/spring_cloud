package com.example.userservice.dto;

import com.example.userservice.vo.ResponseOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDto {
  private String email;
  private String name;
  private String pwd;
  private String userId;
  private Date createdAt;

  private String encryptedPwd;

  List<ResponseOrder> orders = new ArrayList<>();
}
