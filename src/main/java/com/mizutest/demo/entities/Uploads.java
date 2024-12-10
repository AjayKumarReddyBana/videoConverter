package com.mizutest.demo.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="UPLOADS")
public class Uploads {
	
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	
    @Column(name = "size", nullable = false, columnDefinition = "TEXT")
	private double size;
    
    @Column(name = "name", nullable = false, columnDefinition = "TEXT")
	private String name;
    
    
    @Column(name = "email", nullable = true, columnDefinition = "TEXT")
	private String email;
    
    
    @Column(name = "language", nullable = false, columnDefinition = "TEXT")
	private String language;
    
    @Column(name = "update_type", nullable = false, columnDefinition = "TEXT")
	private String updateType;
    
    
    @Column(name = "progress", nullable = false, columnDefinition = "TEXT")
	private double progress;
    
    /*@Column(name = "downloadUrl", nullable = true, columnDefinition = "TEXT")
	private String downloadUrl;*/
    
    
   

	public double getProgress() {
		return progress;
	}

	public void setProgress(double progress) {
		this.progress = progress;
	}

	public void setSize(double size) {
		this.size = size;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getUpdateType() {
		return updateType;
	}

	public void setUpdateType(String updateType) {
		this.updateType = updateType;
	}

	@Override
	public String toString() {
		return "Uploads [id=" + id + ", size=" + size + ", name=" + name + ", email=" + email + ", language=" + language
				+ ", updateType=" + updateType + "]";
	}

	public double getSize() {
		return size;
	}


	
    
    
    
   

}
