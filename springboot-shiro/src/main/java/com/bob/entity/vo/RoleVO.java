package com.bob.entity.vo;

import com.bob.entity.Permission;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Data
public class RoleVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String roleName;

    private Set<Permission> permissions;
}
