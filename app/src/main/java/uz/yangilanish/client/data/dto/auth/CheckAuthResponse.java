package uz.yangilanish.client.data.dto.auth;

import uz.yangilanish.client.data.dto.Response;

public class CheckAuthResponse extends Response {

    private ResponseData data;

    public ResponseData getData() {
        return data;
    }

    public void setData(ResponseData data) {
        this.data = data;
    }
}
