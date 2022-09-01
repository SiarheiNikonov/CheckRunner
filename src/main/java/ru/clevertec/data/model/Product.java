package ru.clevertec.data.model;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Product {
    @Id
    @Column(name = "product_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String title;
    @Column(name = "price_in_cent")
    int priceInCents;
    String description;
    @ManyToOne(optional = false)
    @JoinColumn(name = "company_id_ref")
    Company producer;
    long barcode;
    boolean onSale;
}
