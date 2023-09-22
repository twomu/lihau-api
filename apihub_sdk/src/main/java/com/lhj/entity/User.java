package com.lhj.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户
 * @TableName user
 */

@Data
public class User implements Serializable {

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 性别
     */
    private Integer gender;


}