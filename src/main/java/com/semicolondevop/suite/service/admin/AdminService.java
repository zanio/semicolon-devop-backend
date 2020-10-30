package com.semicolondevop.suite.service.admin;

/* Aniefiok Akpan
 *created on 5/16/2020
 *inside the package */

import com.semicolondevop.suite.exception.UserAlreadyExistException;
import com.semicolondevop.suite.model.admin.Admin;
import com.semicolondevop.suite.model.developer.Developer;

import java.util.List;

public interface AdminService {

     Admin save(Admin admin) throws UserAlreadyExistException;

     List<Admin> findAllAdmin();

     Admin findAdminById(Integer adminId);

     void deleteById(Integer adminId);

    Admin update(Admin admin);

    Admin findUserByEmail(String email);

     Developer convertPassword(Integer saverId);
}
