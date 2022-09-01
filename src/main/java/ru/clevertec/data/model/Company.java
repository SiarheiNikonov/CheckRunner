package ru.clevertec.data.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer companyId;
    String companyName;
    String companyAddress;
    String companyTelNumber;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Company company = (Company) o;
        return Objects.equals(companyId, company.companyId) && Objects.equals(companyName, company.companyName) && Objects.equals(companyAddress, company.companyAddress) && Objects.equals(companyTelNumber, company.companyTelNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(companyId, companyName, companyAddress, companyTelNumber);
    }
}
