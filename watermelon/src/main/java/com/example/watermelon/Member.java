package com.example.watermelon;

// DB의 login 테이블 내용 getter, setter
public class Member {
    public int userID, userPW;
    public String user_str_id, userName;

    // set function
    public void setUserId(int userID) { this.userID = userID; }
    public void setUserStr_id(String user_str_id) { this.user_str_id = user_str_id; }
    public void setUserPw(int userPW) { this.userPW = userPW; }
    public void setUserName(String userName) { this.userName = userName; }

    // get function
    public int getUserId() { return this.userID; }
    public String getUserStr_id() { return this.user_str_id; }
    public int getUserPW() { return this.userPW; }
    public String getUserName() { return this.userName; }
}
