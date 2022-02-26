package com.example.userservice.jpa;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(nullable = false, length = 50, unique = true)
  private String email;
  @Column(nullable = false, length = 50)
  private String name;
  @Column(nullable = false, unique = true)
  private String userId;
  @Column(nullable = false, unique = true)
  private String encryptedPwd;
}
