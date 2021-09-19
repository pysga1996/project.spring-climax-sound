package com.alpha.model.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Collection;
import java.util.Date;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(value = {"songs", "albums"}, allowGetters = true, ignoreUnknown = true)
public class GenreDTO {

    private Integer id;

    @NotBlank
    private String name;

    @JsonBackReference("song-genre")
    private Collection<SongDTO> songs;

    @JsonBackReference("album-genre")
    private Collection<AlbumDTO> albums;

    private Date createTime;

    private Date updateTime;

    private Integer status;
}
