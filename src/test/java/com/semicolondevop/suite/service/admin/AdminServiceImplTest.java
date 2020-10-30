package com.semicolondevop.suite.service.admin;

import com.semicolondevop.suite.exception.UserAlreadyExistException;
import com.semicolondevop.suite.model.admin.Admin;
import com.semicolondevop.suite.model.applicationUser.ApplicationUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

/**
 * @author with Username zanio and fullname Aniefiok Akpan
 * @created 25/10/2020 - 8:55 PM
 * @project com.semicolondevop.suite.service.admin in ds-suite
 */
@Slf4j
public class AdminServiceImplTest {


    @Mock
    private AdminService adminServiceImpl;

    @Before
    public void setUp() {

        adminServiceImpl = mock(AdminService.class);
    }

    @Test
    public void whenValidAdmin_thenAdminShouldBeSaved() throws UserAlreadyExistException {
        SecureRandom secureRandom = new SecureRandom();

        Admin tempSaver = new Admin();
        tempSaver.setFirstname("Oluwatobi");
        tempSaver.setLastname("Omotosho");
        tempSaver.setPhoneNumber("090989899978");
        tempSaver.setPassword("test123");
        tempSaver.setUsername("tboydv1@gmail.com");

        ApplicationUser tempUser = new ApplicationUser();
        tempUser.setId(1 + secureRandom.nextInt(345245));
        tempUser.setUsername(tempSaver.getUsername());
        tempUser.setPassword(tempSaver.getPassword());
        tempUser.setRole("ADMIN");

        when(adminServiceImpl.save((tempSaver)))
                .thenReturn(tempSaver);

        Admin admin = adminServiceImpl.save(tempSaver);
        verify(adminServiceImpl, times(1)).save(tempSaver);

        assertThat(admin).isNotNull();
    }

    @Test
    public void whenValidAdminList_thenAListOfAdminShouldBeFound() throws UserAlreadyExistException {

        Admin tempSaver = new Admin();
        tempSaver.setAdminId(12);
        tempSaver.setFirstname("Oluwatobi");
        tempSaver.setLastname("Omotosho");
        tempSaver.setPhoneNumber("090989899978");
        tempSaver.setPassword("test123");
        tempSaver.setUsername("tboydv1@gmail.com");

        Admin found1 = adminServiceImpl.save(tempSaver);


        Admin tempSaver2 = new Admin();
        tempSaver2.setAdminId(5);
        tempSaver.setAdminId(12);
        tempSaver.setFirstname("Oluwatobi");
        tempSaver.setLastname("Omotosho");
        tempSaver.setPhoneNumber("090989119978");
        tempSaver.setPassword("test123");
        tempSaver.setUsername("tboydv63@gmail.com");
        Admin found2 = adminServiceImpl.save(tempSaver2);

        List<Admin> arrayList = new ArrayList<>();
        arrayList.add(found2);
        arrayList.add(found1);

        when(adminServiceImpl.findAllAdmin())
                .thenReturn(arrayList);

        List<Admin> foundList = adminServiceImpl.findAllAdmin();

        assertThat(foundList.size()).isEqualTo(arrayList.size());
    }
    @Test
    public void whenValidAdminId_thenReturnAdmin() throws UserAlreadyExistException {

        Admin tempSaver2 = new Admin();
        tempSaver2.setAdminId(5);



        when(adminServiceImpl.findAdminById(5))
                .thenReturn(tempSaver2);

        Admin found1 = adminServiceImpl.findAdminById(5);
        verify(adminServiceImpl, times(1)).findAdminById(5);

        assertThat(found1).isNotNull();


    }

    @Test
    public void whenUpdateAdmin_thenAdminRecordShouldBeUpdated() throws UserAlreadyExistException {

        Admin tempSaver2 = new Admin();
        tempSaver2.setAdminId(5);



        when(adminServiceImpl.update(tempSaver2))
                .thenReturn(tempSaver2);

        Admin found1 = adminServiceImpl.update(tempSaver2);
        verify(adminServiceImpl, times(1)).update(found1);


        assertThat(found1).isNotNull();

    }
    @Test
    public void whenfindUserByEmail_isCalled_thenReturnAdmin() throws UserAlreadyExistException {

        Admin tempSaver2 = new Admin();
        tempSaver2.setAdminId(5);
        tempSaver2.setUsername("zanio");



        when(adminServiceImpl.findUserByEmail("zanio"))
                .thenReturn(tempSaver2);

        Admin found1 = adminServiceImpl.findUserByEmail("zanio");
        Admin found2 = adminServiceImpl.findUserByEmail("akp");
        verify(adminServiceImpl, times(1)).findUserByEmail("zanio");

        assertThat(found1).isNotNull();
        assertThat(found2).isNull();

    }
}