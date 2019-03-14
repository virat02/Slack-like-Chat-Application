package edu.northeastern.ccs.im.controller;

import edu.northeastern.ccs.im.communication.CommunicationUtils;
import edu.northeastern.ccs.im.communication.NetworkResponse;
import edu.northeastern.ccs.im.communication.NetworkResponseImpl;
import edu.northeastern.ccs.im.communication.PayloadImpl;
import edu.northeastern.ccs.im.service.CircleService;
import edu.northeastern.ccs.im.service.UserService;
import edu.northeastern.ccs.im.userGroup.User;

public class CircleController {

    private CircleService circleService = new CircleService();
    private UserService userService = new UserService();

    /**
     * @param username
     * @return A list of follower's of this user's followers
     */
    public NetworkResponse viewFollowersOfFollowers(String username) {
        try {

            User u = userService.search(username);
            return new NetworkResponseImpl(NetworkResponse.STATUS.SUCCESSFUL,
                    new PayloadImpl(CommunicationUtils.toJsonArray(circleService.viewFollowersOfFollowers(u))));
        } catch (IllegalArgumentException e) {
            return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED,
                    new PayloadImpl(null));
        }
    }

    /**
     * Get the followers for this user
     * @param username
     * @return
     */
    public NetworkResponse viewFollowers(String username) {
        try {
            return new NetworkResponseImpl(NetworkResponse.STATUS.SUCCESSFUL,
                    new PayloadImpl(CommunicationUtils.toJsonArray(userService.getFollowers(username))));
        } catch (IllegalArgumentException e) {
            return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED,
                    new PayloadImpl(null));
        }
    }

//    /**
//     * Get the followees for this user
//     * @param username
//     * @return
//     */
//    public NetworkResponse viewFollowees(String username) {
//        try {
//            return new NetworkResponseImpl(NetworkResponse.STATUS.SUCCESSFUL,
//                    new PayloadImpl(CommunicationUtils.toJsonArray(userService.getFollowees(username))));
//        } catch (IllegalArgumentException e) {
//            return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED,
//                    new PayloadImpl(null));
//        }
//    }
}
