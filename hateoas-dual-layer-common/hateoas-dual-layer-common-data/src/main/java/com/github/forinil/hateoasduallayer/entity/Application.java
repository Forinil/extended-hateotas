package com.github.forinil.hateoasduallayer.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@AllArgsConstructor
public class Application {

    @Id
    @GeneratedValue
    private int id;

    @Column(nullable = false)
    private Date creationDate;

    @Column(nullable = false)
    private String content;

}
