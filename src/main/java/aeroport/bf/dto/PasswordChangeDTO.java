package aeroport.bf.dto;
/**
 * A DTO representing a password change required data - current and new password.
 */
public class PasswordChangeDTO {
    private String currentPassword;
    private String newPassword;
    private String email;

    public PasswordChangeDTO() {
        // Empty constructor needed for Jackson.
    }

    public PasswordChangeDTO(String currentPassword, String newPassword, String email) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
        this.email = email;
    }
    

    public String getCurrentPassword() {

        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
