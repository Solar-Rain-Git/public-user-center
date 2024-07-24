package com.solar.userbackend.Common;

import com.solar.userbackend.Entity.User;
import lombok.Data;

import java.util.List;

@Data
public class UserPageResponse {
    private long current;
    private long pageSize;
    private long total;
    private List<User> userList;

    public UserPageResponse(long current, long pageSize, long total, List<User> userList) {
        this.current = current;
        this.pageSize = pageSize;
        this.total = total;
        this.userList = userList;
    }
}
