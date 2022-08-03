package ru.clevertec.data.model;

public class Company {
    private final Integer companyId;
    private final String companyName;
    private final String companyAddress;
    private final String companyTelNumber;

    public Company(Integer companyId, String companyName, String companyAddress, String companyTelNumber) {
        this.companyId = companyId;
        this.companyName = companyName;
        this.companyAddress = companyAddress;
        this.companyTelNumber = companyTelNumber;
    }

    public Company(String companyName, String companyAddress, String companyTelNumber) {
        this(null, companyName, companyAddress, companyTelNumber);
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public String getCompanyTelNumber() {
        return companyTelNumber;
    }
}
