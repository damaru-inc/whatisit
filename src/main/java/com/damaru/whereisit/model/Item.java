package com.damaru.whereisit.model;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name="item")
public class Item implements Saveable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="item_seq")
    @SequenceGenerator(name="item_seq",sequenceName="item_sequence", allocationSize=1)
    private Long id;

    @NotNull
    @Size(min = 1, max = 128)
    private String name;
    
    @NotNull
    @ManyToOne
    private Container container;

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
    
    public Container getContainer() {
        return container;
    }

    public void setContainer(Container container) {
        this.container = container;
    }

    @Override
    public String toString() {
        return String.format("[Item %3d %s in %s]", id, name, (container == null ? "Null container" : container.toString()));
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
        Item other = (Item) obj;
        return Objects.equals(id, other.id);
    }

}
