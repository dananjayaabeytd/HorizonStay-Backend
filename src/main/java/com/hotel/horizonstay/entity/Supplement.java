package com.hotel.horizonstay.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "supplement")
@Data
public class Supplement implements Serializable {

    @Serial
    private static final long serialVersionUID = 214101981905645865L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String supplementName;
    private Float price;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "season_id")
    private Season season;

}


