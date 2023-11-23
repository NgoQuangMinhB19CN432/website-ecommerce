package com.ecommerce.backend.dao;


import com.ecommerce.backend.entity.Product432;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDao extends CrudRepository<Product432, Integer> {
    public List<Product432> findAll(Pageable pageable);

    public List<Product432> findByProductNameContainingIgnoreCaseOrProductDescriptionContainingIgnoreCase(
            String key1, String key2, Pageable pageable
    );
}
