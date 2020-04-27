package cgh.community.community.dto;

import lombok.Data;

/**
 * github用户类
 */
@Data
public class GithubUser {
    private String name;
    private Long id;
    private String bio;
    private String avatarUrl;
}
