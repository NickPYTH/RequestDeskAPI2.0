package de401t.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.sql.Time;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    private Task task;

    @Size(min = 1, max = 255)
    @Column(nullable = false)
    private String message;

    @OneToOne(fetch = FetchType.LAZY)
    private User userFrom;

    @OneToOne(fetch = FetchType.LAZY)
    private User userTo;

    @Column(nullable = false)
    private Date date;

}
