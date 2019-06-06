package model;

/**
 *
 * @author jorge
 */
public class Usuario{
    
    private int id;
    private String user;
    private String password;
    private byte [] image; 
    private String email;
    private String passwordEmail;
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordEmail() {
        return passwordEmail;
    }

    public void setPasswordEmail(String passwordEmail) {
        this.passwordEmail = passwordEmail;
    }
        
    
    
}
