package de401t.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Data
@NoArgsConstructor
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(min = 4, max = 255)
    @Column()
    private String name;

    @Column()
    private Integer code;

    @Column
    @Lob
    @Type(type = "org.hibernate.type.ImageType")
    private byte[] data;
}
