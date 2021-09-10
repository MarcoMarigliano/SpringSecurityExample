package it.softwareinside.testSecurity.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    private String email;

    private String password;

    private LocalDate registerDate;
    
    private boolean isEnabled=true;

    @DBRef
    private Set<Role> roles = new HashSet<>();
    
    public Set<Role> getRoles() {
        return roles;
    }
    
}