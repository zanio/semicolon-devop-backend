package com.semicolondevop.suite.service.activity;

import com.semicolondevop.suite.client.exception.UserAlreadyExistException;
import com.semicolondevop.suite.model.activity.Activity;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

/**
 * @author with Username zanio and fullname Aniefiok Akpan
 * @created 25/10/2020 - 8:55 PM
 * @project com.semicolondevop.suite.service.activity in ds-suite
 */
@Slf4j
public class ActivityServiceImplTest {

    @Mock
    private ActivityService activityServiceImpl;

    @Before
    public void setUp() {

        activityServiceImpl = mock(ActivityService.class);
    }

    @Test
    public void whenfindFirst_isCalled_thenReturnActivity() throws UserAlreadyExistException {

        Activity activity = new Activity();

        activity.setId(Long.getLong("34343"));



        when(activityServiceImpl.findFirst())
                .thenReturn(activity);

        Activity found1 = activityServiceImpl.findFirst();
        verify(activityServiceImpl, times(1)).findFirst();

        assertThat(found1).isNotNull();

    }
}