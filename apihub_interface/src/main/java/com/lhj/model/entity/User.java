package com.lhj.model.entity;

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




}