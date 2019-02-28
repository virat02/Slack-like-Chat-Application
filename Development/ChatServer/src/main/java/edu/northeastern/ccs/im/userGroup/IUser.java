package edu.northeastern.ccs.im.userGroup;

import edu.northeastern.ccs.jpa.Profile;

import java.util.Date;
import java.util.List;

public interface IUser extends IUserGroup {
    List<IUser> search(String name);

    void follow(IUser user);

    void sendMessage(String messageText, int iGroupId);

    void setExpiration(int messageId, Date date);

    void deleteGroup(int groupId);

    void createIGroup(int iGroupId);

    Profile getProfile();


}
