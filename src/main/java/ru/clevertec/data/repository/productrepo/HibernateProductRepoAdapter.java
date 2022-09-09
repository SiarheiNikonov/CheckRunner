package ru.clevertec.data.repository.productrepo;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.clevertec.data.model.Product;
import ru.clevertec.util.exceptions.ProductNotFoundException;
import ru.clevertec.util.exceptions.RepositoryException;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class HibernateProductRepoAdapter implements ProductRepository{

    private final HibernateProductRepoImpl jpaRepository;

    @Override
    public Product getById(Long id) throws RepositoryException {
        return jpaRepository.findById(id).orElseThrow(
                () -> new RepositoryException("Product not found")
        );
    }

    @Override
    public Product add(Product item) throws RepositoryException {
        return jpaRepository.save(item);
    }

    @Override
    public boolean remove(Long id) throws RepositoryException {
        jpaRepository.deleteById(id);
        return true;
    }

    @Override
    public boolean update(Product product) throws RepositoryException {
        jpaRepository.save(product);
        return true;
    }

    @Override
    public List<Product> findAll(Integer pageSize, long lastItemId) throws RepositoryException {

        return jpaRepository.findAll(Pageable.ofSize(pageSize), lastItemId);
    }
}
