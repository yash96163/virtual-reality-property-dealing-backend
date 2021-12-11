package com.hashedin.virtualproperty.application.entities;

import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Table(name="PropertyImage")
@Data
public class PropertyImage {
    @Id
    private String publicId;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name="propertyId")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Property property;

    public PropertyImage(){}

    public PropertyImage(String publicId, String url, String name, Property property){
        this.publicId = publicId;
        this.url = url;
        this.name = name;
        this.property = property;
    }
}
