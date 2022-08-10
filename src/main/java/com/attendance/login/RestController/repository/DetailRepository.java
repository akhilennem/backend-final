package com.attendance.login.RestController.repository;


import com.attendance.login.RestController.model.Details;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Transactional
@Repository

public interface DetailRepository extends CrudRepository<Details,String> {

     Details findByName(String name);

     Details findByEmail(String email);

    void deleteByEmail(String email);

 Details getByEmail(String email);


}
