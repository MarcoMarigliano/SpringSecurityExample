package it.softwareinside.testSecurity.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Random;
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
    
    private String attivate_code=creazione();
    
    private boolean isEnabled=false;

    @DBRef
    private Set<Role> roles = new HashSet<>();
    
    public Set<Role> getRoles() {
        return roles;
    }
    
    public static String creazione() {
    	Random rand = new Random();
    	
        String lettere= "abcdefghijklmnopqrstuvwxyz";
        String cf = "";
        
        for(int i =0; i < 40; i++)
            if( i % 2 == 0) {
            cf += rand.nextInt(10);
            }else {
                int numero = rand.nextInt(26);
                cf+= lettere.charAt(numero);
            }
        return cf;
    }
    
    
    
    
    
}