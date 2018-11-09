package com.github.springjwt.domain.authority;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "authority")
public class Authority {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "authority_seq")
    @SequenceGenerator(name = "authority_seq", sequenceName = "authority_seq", allocationSize = 1)
    private Long id;
    @Column(name = "name", length = 50)
    @Enumerated(EnumType.STRING)
    @NotNull
    private AuthorityName name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AuthorityName getName() {
        return name;
    }

    public void setName(AuthorityName name) {
        this.name = name;
    }
}
