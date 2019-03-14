package edu.northeastern.ccs.im.service;

import edu.northeastern.ccs.im.userGroup.User;

import java.util.ArrayList;
import java.util.List;

public class CircleService {

    /**
     * @param user
     * @return Returns a list of followees of the user's followers
     */
    public List<List<User>> viewFolloweesOfFollowers(User user) {

        List<List<User>> result = new ArrayList<>();
        List<User> followingList = user.getFollowing();

        for (User u : followingList){
            result.add(u.getFollowee());
        }

        return result;
    }

    /**
     * @param user
     * @return Returns a list of followers of the user's followers
     */
    public List<List<User>> viewFollowersOfFollowers(User user) {

        List<List<User>> result = new ArrayList<>();
        List<User> followingList = user.getFollowing();

        for (User u : followingList){
            result.add(u.getFollowing());
        }

        return result;
    }
}
