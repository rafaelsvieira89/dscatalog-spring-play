package com.devsuperior.dscatalog.dtos;

import com.devsuperior.dscatalog.entities.Role;

import java.io.Serializable;
import java.util.Objects;

public class RoleDTO implements Serializable {

    private Long id;
    private String authority;


    public RoleDTO() {
    }

    public RoleDTO(Long id, String authority) {
        this.id = id;
        this.authority = authority;
    }

    public RoleDTO(Role entity) {
        this.id = entity.getId();
        this.authority = entity.getAuthority();
    }


    public Long getId() {
        return id;
    }

    public String getAuthority() {
        return authority;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoleDTO entity = (RoleDTO) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.authority, entity.authority);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, authority);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "authority = " + authority + ")";
    }
}
