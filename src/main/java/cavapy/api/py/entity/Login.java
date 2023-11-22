package cavapy.api.py.entity;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Entity
@Table(name = "login", schema = "public")
public class Login {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_login")
    private Integer idLogin;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "cavapy_user")
    private String cavapyUser;

    @Column(name = "cavapy_password")
    private Integer cavapyPassword;

    @Column(name = "email")
    private String email;

}
