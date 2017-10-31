package com.pfe.DAO;

public class AlfrescoUser {
    private String url;
    private String userName;
    private String password;
    private String enabled           = "true";
    private String firstName;
    private String lastName;
    private String email;
    private String quota             = "-1";
    private String sizeCurrent       = "0";
    private String emailFeedDisabled = "false";

    public String getUrl() {
        return url;
    }

    public void setUrl( String url ) {
        this.url = url;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName( String userName ) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword( String password ) {
        this.password = password;
    }

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled( String enabled ) {
        this.enabled = enabled;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName( String firstName ) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName( String lastName ) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail( String email ) {
        this.email = email;
    }

    public String getQuota() {
        return quota;
    }

    public void setQuota( String quota ) {
        this.quota = quota;
    }

    public String getSizeCurrent() {
        return sizeCurrent;
    }

    public void setSizeCurrent( String sizeCurrent ) {
        this.sizeCurrent = sizeCurrent;
    }

    public String getEmailFeedDisabled() {
        return emailFeedDisabled;
    }

    public void setEmailFeedDisabled( String emailFeedDisabled ) {
        this.emailFeedDisabled = emailFeedDisabled;
    }

}
