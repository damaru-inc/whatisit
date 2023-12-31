package com.damaru.whereisit.model;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name="room")
public class Room implements Saveable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="room_seq")
    @SequenceGenerator(name="room_seq",sequenceName="room_sequence", allocationSize=1)
    private Long id;

    @NotNull
    @Size(min = 1, max = 128)
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        System.out.println("Setting id to " + id);
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public String toString() {
        return String.format("[Room %3d %s]", id, name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Room other = (Room) obj;
        return Objects.equals(id, other.id);
    }

}
