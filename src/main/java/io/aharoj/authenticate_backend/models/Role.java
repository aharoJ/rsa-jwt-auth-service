package io.aharoj.authenticate_backend.models;

import org.springframework.security.core.GrantedAuthority;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "role_id")
  private Integer roleId;

  private String authority;

  public Role() {
    super();
  }

  public Role(String authority) {
    this.authority = authority;
  }

  public Role(Integer roleId, String authority) {
    this.roleId = roleId;
    this.authority = authority;
  }

  public Integer getRoleId() {
    return roleId;
  }

  public void setRoleId(Integer roleId) {
    this.roleId = roleId;
  }

  public String getAuthority() {
    return authority;
  }

  public void setAuthority(String authority) {
    this.authority = authority;
  }
}
