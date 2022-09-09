package ru.clevertec.data.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level=AccessLevel.PRIVATE)
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
