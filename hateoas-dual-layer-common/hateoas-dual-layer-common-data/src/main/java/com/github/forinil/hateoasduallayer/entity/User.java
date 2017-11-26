package com.github.forinil.hateoasduallayer.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue
    private int ID;

    @Column(nullable = false)
    private String login;

    @Enumerated(EnumType.STRING)
    @ElementCollection(targetClass = Right.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "rights")
    @Column(name = "right", nullable = false)
    private List<Right> rights;
}
