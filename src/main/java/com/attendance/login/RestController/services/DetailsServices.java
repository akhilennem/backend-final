package com.attendance.login.RestController.services;

import com.attendance.login.RestController.model.Details;
import com.attendance.login.RestController.repository.DetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DetailsServices {

    @Autowired
    public DetailRepository detailRepository;

    public Details Update(Details details){
        return detailRepository.save(details);}


    public Details Get(String name){return detailRepository.findByName(name);

    }
    public Details Find(String email) {return detailRepository.findByEmail(email);
    }
}
