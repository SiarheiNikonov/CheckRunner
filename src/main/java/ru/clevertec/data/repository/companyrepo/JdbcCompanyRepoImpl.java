package ru.clevertec.data.repository.companyrepo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.clevertec.data.model.Company;
import ru.clevertec.util.jdbc.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


@RequiredArgsConstructor
public class JdbcCompanyRepoImpl implements CompanyRepo {

    private final ConnectionPool connectionPool;

    private static final String ADD_COMPANY_QUERY =
            "INSERT INTO company (company_name, company_address, company_tel_number) " +
                    "VALUES (?, ?, ?)";

    private static final String GET_COMPANY_BY_ID_QUERY = "SELECT * FROM company WHERE company_id = ?";

    @Override
    public boolean addCompany(Company company) {
        try (Connection connection = connectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(ADD_COMPANY_QUERY);
            statement.setString(1, company.getCompanyName());
            statement.setString(2, company.getCompanyAddress());
            statement.setString(3, company.getCompanyTelNumber());
            int changedRows = statement.executeUpdate();
            return changedRows == 1;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public Company getCompanyById(int id) {
        try (Connection connection = connectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(GET_COMPANY_BY_ID_QUERY);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String companyName = resultSet.getString("company_name");
                String companyAddress = resultSet.getString("company_address");
                String companyTelNumber = resultSet.getString("company_tel_number");
                return new Company(id, companyName,companyAddress, companyTelNumber);
            } return null;
        } catch (SQLException ex) {
            return null;
        }
    }
}
