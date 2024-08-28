package cavapy.api.py.continental.model;

import jakarta.persistence.Column;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class UserProfile {

    private Integer id;

    @Column(name = "id_system_profile_pk")
    private Integer idSystemProfile;

    @Column(name = "profile_name")
    private String profileName;

    @Column(name = "description")
    private String description;

    @Column(name = "profile_registry_date")
    private Timestamp registryDate;

    @Column(name = "mnemonic")
    private String mnemonic;

    @Column(name = "name")
    private String name;

    @Column(name = "lastname")
    private String lastName;

    @Column(name = "LOGIN_USER")
    private String loginUser;

    @Column(name = "email")
    private String email;

    @Column(name = "user_registry_date")
    private Timestamp userRegistryDate;

}
