package com.damaru.whereisit.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity(name="room")
@Table(uniqueConstraints = @UniqueConstraint(name="uniqueName", columnNames = "name"))
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="room_sequence")
    private Long id;

    @NotNull
    @Size(min = 1, max = 25)
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
