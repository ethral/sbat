package com.sbat.booking.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnTransformer;


import lombok.Data;

@Entity
@Table(name= "user", schema= "sbat")
@Data
public class User {
	
	
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;
    
    @Column(name="username")
    private String userName;
    
//    @ColumnTransformer(
//    		read = "pgp_sym_decrypt(password, current_setting('encrypt.key'))",
//    		write = "pgp_sym_encrypt(?, current_setting('encrypt.key'))"
//    		)
    @ColumnTransformer(
    		read = "pgp_sym_decrypt(password, 'secr3t')",
    		write = "pgp_sym_encrypt(?, 'secr3t')"
    		)	
    @Column(name="password", columnDefinition = "bytea")
    private String password;
    
    @Column(name="active")
    private boolean active;
    
    @Column(name="roles")
    private String roles;

}