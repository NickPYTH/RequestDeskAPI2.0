package de401t.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Data
@NoArgsConstructor
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(min = 4, max = 255)
    @Column(unique = true, nullable = false)
    private String name;

    @Size(min = 4, max = 255)
    @Column(unique = true, nullable = false, name = "name_ru")
    private String nameRu;

    @Column(unique = true, nullable = false)
    private Integer code;

    @Override
    public String getAuthority() {
        return getName();
    }

}
