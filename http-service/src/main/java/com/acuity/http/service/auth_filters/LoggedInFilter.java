package com.acuity.http.service.auth_filters;


import com.acuity.db.entities.AcuityAccount;

/**
 * Created by Zachary Herridge on 6/1/2017.
 */
public class LoggedInFilter extends AuthFilter {
    @Override
    boolean isAuthed(AcuityAccount acuityAccount) {
        return true;
    }
}
