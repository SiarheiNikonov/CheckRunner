package ru.clevertec.data.repository.companyrepo;

import ru.clevertec.data.model.Company;

public interface CompanyRepo {

    boolean addCompany(Company company);
    Company getCompanyById(int id);
}
