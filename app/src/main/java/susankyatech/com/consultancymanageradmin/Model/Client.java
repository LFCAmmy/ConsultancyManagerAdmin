package susankyatech.com.consultancymanageradmin.Model;

import java.util.List;

import susankyatech.com.consultancymanageradmin.ConsultancyProfile.Gallery;

public class Client {
    public int id;
    public List<Gallery> galleries;
    public String client_name;
    public String logo;
    public String created_at;
    public String expires_at;
    public String updated_at;
    public String parent_id;
    public String address;
    public String contact;
    public boolean interested;
    public int mobile;
    public Detail detail;
    public List<Subjects> subjects;

}
