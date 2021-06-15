package com.aruoxi.microservice.user.repository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;


@Data
@Entity
@Table(name = "USERS")
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String email;
    
    private String mobile;

    private LocalDate birthdate;

    private String address;
        
    private String education;
    
    private String occupation;
    
    private String id_card_no;
}
