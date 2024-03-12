package de401t.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
public class Obj {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @OneToOne(fetch = FetchType.LAZY)
    private Filial filial;

    @OneToMany(mappedBy = "obj", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private List<Equipment> equipments;

}
