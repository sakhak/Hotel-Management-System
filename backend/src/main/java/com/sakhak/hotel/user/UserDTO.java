package com.sakhak.hotel.users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Long id;

    private Long roleId;

    private String roleName;

    private String username;

    private String email;

    private String password;

    private Boolean enabled;
}