package de401t.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Data
@NoArgsConstructor
public class Status {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(min = 1, max = 20)
    @Column(unique = true, nullable = false)
    private String name;

    @Size(min = 1, max = 20)
    @Column(unique = true, nullable = false, name = "name_ru")
    private String nameRu;

    @Column(unique = true, nullable = false)
    private Integer code;

}
