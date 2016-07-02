package com.appKroid;

/**
 * Created by Kinchit.
 */
public class serviceMasterOSK {

    //https://offersplit-dev.herokuapp.com/api/user/
    //https://offersplit-dev.herokuapp.com/api/deal/
    //http://
    final public static String urlPreBASE = "https://offersplit-dev.herokuapp.com/api/";
    final public static String urlPreMAIN_User = urlPreBASE + "user/";
    final public static String urlPreMAIN_Deal = urlPreBASE + "deal/";

    public static String slash = "/";
    public static String device_type = "android";

    final public static String urlLogin = "login";
    final public static String urlSignup = "signup";
    final public static String urlLogout = "logout";
    final public static String urlForgotPassReq = "forgotpasswordReq";
    final public static String urlPasswordReset = "passwordReset";
    final public static String urlUpdateProfile = "updateprofile";
    final public static String urlChangePassword = "changepassword";
    final public static String urlPostDeal = "postdeal";
    final public static String urlUpdateDeal = "updatedeal";
    final public static String urlDeleteDeal = "deletedeal";
    final public static String urlSearchDeal = "getdeal";
    final public static String urlHistory = "gethistory";
    final public static String urlAcceptDeal = "acceptdeal";
    final public static String urlRejectDeal = "rejectdeal";
    final public static String urlSocialLogin = "sociallogin";
    final public static String urlUsername = "username";

    final public static String urlXMPbase_user = "http://acra.dowhistle.com:9090/plugins/restapi/v1/users";

    //-------

    public static String getLoginURL() {
        return urlPreMAIN_User + urlLogin;//post
    }//

    public static String getSignupURL() {
        return urlPreMAIN_User + urlSignup;//post
    }//

    public static String getLogoutURL() {
        return urlPreMAIN_User + urlLogout;//delete **
    }//

    public static String getForgotPassReqURL() {
        return urlPreMAIN_User + urlForgotPassReq;//post
    }

    public static String getPasswordResetURL() {
        return urlPreMAIN_User + urlPasswordReset;//post
    }

    public static String getUpdateProfileURL() {
        return urlPreMAIN_User + urlUpdateProfile;//post **
    }

    public static String getChangePasswordURL() {
        return urlPreMAIN_User + urlChangePassword;//post **
    }

    public static String getPostDealURL() {
        return urlPreMAIN_Deal + urlPostDeal;//post **
    }

    public static String getUpdateDealURL(String dealId) {
        return urlPreMAIN_Deal + urlUpdateDeal +slash+ dealId;//put **
    }

    public static String getDeleteDealURL(String dealId) {
        return urlPreMAIN_Deal + urlDeleteDeal +slash+ dealId;//delete **
    }

    public static String getSearchDealURL() {
        return urlPreMAIN_Deal + urlSearchDeal;//post **
    }

    public static String getHistoryURL() {
        return urlPreMAIN_Deal + urlHistory;//get **
    }

    public static String getAcceptDealURL() {
        return urlPreMAIN_Deal + urlAcceptDeal;//put **
    }

    public static String getRejectDealURL() {
        return urlPreMAIN_Deal + urlRejectDeal;//put **
    }

    public static String getSocialLoginURL() {
        return urlPreMAIN_User + urlSocialLogin;//post
    }//

    public static String getMXPregiUserURL() {
        return urlXMPbase_user;//post
    }//

    public static String getMXPaddToGrpUserURL(String username, String groupname) {
        //POST http://example.org:9090/plugins/restapi/v1/users/testuser/groups/testGroup
        return urlXMPbase_user +"/"+ username +"/groups/"+ groupname;//post
    }//

    public static String getUsernameFromIDURL(String uid) {
        return urlPreMAIN_User + urlUsername + slash + uid;//get
    }//

    public String getJSON(String from, String to, String text) {
        return "{\"from\":\"" +from+ "\",\"to\":\"" +to+ "\",\"text\":\"" +text+ "\"}";
    }

    // ** Authorization:accessToken required
    /* DVL-Up-er.Kroid */
}
