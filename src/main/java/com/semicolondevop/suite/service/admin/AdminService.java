package com.semicolondevop.suite.service.admin;

/* Aniefiok Akpan
 *created on 5/16/2020
 *inside the package */

import com.semicolondevop.suite.client.exception.UserAlreadyExistException;
import com.semicolondevop.suite.model.admin.Admin;
import com.semicolondevop.suite.model.developer.Developer;

import java.util.Collection;
import java.util.List;

public interface AdminService {

    public Admin save(Admin admin) throws UserAlreadyExistException;

    public List<Admin> findAllAdmin();

    public Admin findAdminById(Integer adminId);

    public void deleteById(Integer adminId);

    Admin update(Admin admin);

    Admin findUserByEmail(String email);

    public Developer convertPassword(Integer saverId);
}
