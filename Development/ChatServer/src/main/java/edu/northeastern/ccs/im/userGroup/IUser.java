package edu.northeastern.ccs.im.userGroup;

import edu.northeastern.ccs.jpa.Profile;

import java.util.Date;
import java.util.List;

public interface IUser extends IUserGroup {
   Profile getProfile();

}
