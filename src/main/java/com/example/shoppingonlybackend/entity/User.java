package com.example.shoppingonlybackend.entity;

import lombok.*;
import jakarta.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String username;
    private String password;
    private String role;

    @Column(name = "permissions")
    private String permissions;

    public String getPermissionsJson() {
        return permissions;
    }
    public void setPermissionsJson(String permissions) {
        this.permissions = permissions;
    }
}
