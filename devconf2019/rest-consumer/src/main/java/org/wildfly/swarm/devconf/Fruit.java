package org.wildfly.swarm.devconf;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @author Durgesh Anaokar
 */
@Entity
@Table
@NamedQueries({
        @NamedQuery(name = "fruit.findAll", query = "SELECT f FROM Fruit f")
})
public class Fruit implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private Integer id;

    @Column(length = 40, unique = true)
    private String name;

    public Fruit() {
    }

    public Fruit(String name) {
        this.name = name;
    }
    
    public Fruit(String name, Integer id) {
        this.name = name;
        this.id= id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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
        return "Fruit named `" + name + "` with id of: " + id;
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj)
            return false;
        if (!(obj instanceof Fruit))
            return false;
        Fruit that = (Fruit) obj;
        return that.name.equals(this.name) && Objects.equals(that.id, this.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.name);
    }

}
