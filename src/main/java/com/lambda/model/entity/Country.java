package com.lambda.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Collection;

@Entity
@Data
@NoArgsConstructor
@JsonIgnoreProperties(value = {"songs", "albums"}, allowGetters = true)
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotBlank
    private String name;

    @JsonManagedReference("song-country")
    @OneToMany(mappedBy = "country", fetch = FetchType.LAZY)
    @Fetch(value = FetchMode.SUBSELECT)
    private Collection<Song> songs;

    @JsonManagedReference("album-country")
    @OneToMany(mappedBy = "country", fetch = FetchType.LAZY)
    @Fetch(value = FetchMode.SUBSELECT)
    private Collection<Album> albums;

    public Country(String name) {
        this.name = name;
    }
}
