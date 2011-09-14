package org.resthub.web.client.test;

/**
 *
 * @author Loïc Frering <loic.frering@gmail.com>
 */
public class DummyUser {
    
    private String fullname;

    public DummyUser(String fullname) {
        this.fullname = fullname;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
    
}
