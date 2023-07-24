package uz.yangilanish.client.data.dto.auth;

public class LoginRequest extends AuthRequest {

    private String phone;

    public LoginRequest(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
