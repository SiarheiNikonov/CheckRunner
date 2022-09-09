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
@Table(name = "discount_cards")
public class DiscountCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "card_id")
    Long id;
    @ManyToOne(optional = false)
    @JoinColumn(name = "card_type")
    DiscountCardType cardType;
}
