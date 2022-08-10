package com.attendance.login.RestController.repository;

import com.attendance.login.RestController.model.QrCode;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface QrRepo extends CrudRepository <QrCode,String>{

    boolean existsByPara(String para);
}
