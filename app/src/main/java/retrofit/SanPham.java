package retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SanPham {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("tensp")
    @Expose
    private String tensp;
    @SerializedName("giasp")
    @Expose
    private String giasp;
    @SerializedName("ha1")
    @Expose
    private String ha1;
    @SerializedName("ha2")
    @Expose
    private String ha2;
    @SerializedName("ha3")
    @Expose
    private String ha3;
    @SerializedName("ha4")
    @Expose
    private String ha4;
    @SerializedName("ha5")
    @Expose
    private String ha5;
    @SerializedName("motasp")
    @Expose
    private String motasp;
    @SerializedName("idloaisp")
    @Expose
    private String idloaisp;
    @SerializedName("idtungloaisp")
    @Expose
    private String idtungloaisp;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTensp() {
        return tensp;
    }

    public void setTensp(String tensp) {
        this.tensp = tensp;
    }

    public String getGiasp() {
        return giasp;
    }

    public void setGiasp(String giasp) {
        this.giasp = giasp;
    }

    public String getHa1() {
        return ha1;
    }

    public void setHa1(String ha1) {
        this.ha1 = ha1;
    }

    public String getHa2() {
        return ha2;
    }

    public void setHa2(String ha2) {
        this.ha2 = ha2;
    }

    public String getHa3() {
        return ha3;
    }

    public void setHa3(String ha3) {
        this.ha3 = ha3;
    }

    public String getHa4() {
        return ha4;
    }

    public void setHa4(String ha4) {
        this.ha4 = ha4;
    }

    public String getHa5() {
        return ha5;
    }

    public void setHa5(String ha5) {
        this.ha5 = ha5;
    }

    public String getMotasp() {
        return motasp;
    }

    public void setMotasp(String motasp) {
        this.motasp = motasp;
    }

    public String getIdloaisp() {
        return idloaisp;
    }

    public void setIdloaisp(String idloaisp) {
        this.idloaisp = idloaisp;
    }

    public String getIdtungloaisp() {
        return idtungloaisp;
    }

    public void setIdtungloaisp(String idtungloaisp) {
        this.idtungloaisp = idtungloaisp;
    }

}