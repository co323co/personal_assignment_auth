package com.assignment.entity;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter @Setter
@Table(name = "auth")
@Entity
public class Auth {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @OneToOne
    @OnDelete(action= OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @Column(name = "refresh_token", nullable = false)
    private String refreshToken;

}