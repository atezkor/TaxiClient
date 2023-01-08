package uz.yangilanish.client.dto.auth;

import com.google.gson.annotations.SerializedName;

import uz.yangilanish.client.dto.Response;
import uz.yangilanish.client.models.Company;


public class LoginResponse extends Response {

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {

        @SerializedName("update_required")
        private boolean requiredUpdate;

        @SerializedName("update_url")
        private String updateUrl;

        private Company company;

        public boolean isRequiredUpdate() {
            return requiredUpdate;
        }

        public void setRequiredUpdate(boolean requiredUpdate) {
            this.requiredUpdate = requiredUpdate;
        }

        public String getUpdateUrl() {
            return updateUrl;
        }

        public void setUpdateUrl(String updateUrl) {
            this.updateUrl = updateUrl;
        }

        public Company getCompany() {
            return company;
        }

        public void setCompany(Company company) {
            this.company = company;
        }
    }
}
