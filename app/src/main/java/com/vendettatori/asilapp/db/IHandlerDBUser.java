package com.vendettatori.asilapp.db;

public interface IHandlerDBUser {
    void onSuccessSetUser();
    void onFailureSetUser(Exception e);
    void onSuccessRetrieveUser(UserAnagrafici userData);
    void onFailureRetrieveUser(Exception e);
}
