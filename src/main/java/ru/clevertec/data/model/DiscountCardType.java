package ru.clevertec.data.model;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "discount_card_types")
public class DiscountCardType {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
            @Column(name = "type_id")
    Long id;
    String typeTitle;
    @Column(name = "discount_percent")
    Integer discount;

}
