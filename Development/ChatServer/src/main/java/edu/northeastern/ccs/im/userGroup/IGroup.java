package edu.northeastern.ccs.im.userGroup;

import java.util.Date;
import java.util.List;

public interface IGroup extends IUserGroup {
	
	String getName();
	void setName(String name);
	int getId();
	Date getCreatedOn();
	List<Message> getMsgs();
}
