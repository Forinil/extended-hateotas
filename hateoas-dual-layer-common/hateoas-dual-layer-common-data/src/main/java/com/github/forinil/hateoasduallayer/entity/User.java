package com.github.forinil.hateoasduallayer.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Getter
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false)
    private String login;

    @Enumerated(EnumType.STRING)
    @ElementCollection(targetClass = Right.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "rights")
    @Column(name = "right", nullable = false)
    private List<Right> rights;
}
