package de401t.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(min = 1, max = 100)
    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @OneToOne(fetch = FetchType.LAZY)
    private User creator;

    @OneToOne(fetch = FetchType.LAZY)
    private Equipment equipment;

    @Column
    @OneToMany
    private List<Photo> photos;

    @OneToOne(fetch = FetchType.LAZY)
    private Status status;

    @Column(nullable = false)
    private Date date;

    @Column
    private Date taskCompleteDate;

    @Column
    @Lob
    @Type(type = "org.hibernate.type.BinaryType")
    private byte[] document;

}
