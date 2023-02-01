package com.credpay.platform.repository;

import com.credpay.platform.model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface AuthorityRepository extends CrudRepository<Authority,Long> {
        Authority findByName(String name);
}
