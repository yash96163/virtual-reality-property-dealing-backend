package com.hashedin.virtualproperty.application.repository;

import com.hashedin.virtualproperty.application.entities.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, String> {

    @Query(value = "select count(*) from user_table where email = ?1 or mobile = ?2", nativeQuery = true)
    int countOfUsersByEmailOrMobile(String email, String mobile);

//    @Query(value = "update user_table set name = ?1 , address = ?2 , mobile = ?3 where email = ?4", nativeQuery = true)
//    Optional<User> updateUser(String name , String address , String mobile , String email);
}
