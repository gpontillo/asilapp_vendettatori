package com.vendettatori.asilapp.db;

public interface IHandlerDBUser {
    void onSuccessSetUser();
    void onFailureSetUser(Exception e);
    void onRetrieveUser(UserAnagrafica userData);
    void onErrorRetrieveUser(Exception e);
}
