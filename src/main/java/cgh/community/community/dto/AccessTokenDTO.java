package cgh.community.community.dto;


import lombok.Data;

/*
Model是对数据表实体的映射；DTO是针对于前台页面的封装，可以是一个表或多表。如果Model字段可以直接满足前台页面需要，可以不用定义DTO
*/

@Data
public class AccessTokenDTO {
    private String client_id;
    private String client_secret;
    private String code;
    private String redirect_uri;
    private String state;
}
