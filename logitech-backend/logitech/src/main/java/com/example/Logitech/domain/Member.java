package com.example.Logitech.domain;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "member",
        uniqueConstraints = {
        @UniqueConstraint(name = "uk_member_email", columnNames = "email")
})

public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;
    @Column(length = 50, nullable = false)
    private String name;
    @Column(name = "login_id", length = 50, nullable = false)
    private String loginId;
    @Column(length = 255, nullable = false)
    private String password;
    @Column(length = 255, nullable = false, unique = true)
    private String email;
    @Column(length = 50)
    private String phone;
    @Column(columnDefinition = "text")
    private String address;



    @CreationTimestamp
    private LocalDateTime createAt;

    @Enumerated(EnumType.STRING)
    private Role role;
}
