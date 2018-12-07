package susankyatech.com.consultancymanageradmin.Notice;

import java.io.File;

public class Notice {
    public String title;
    public String category;
    public String date;
    public String notice;
    public File document;

    public Notice(String title, String category, String date, String notice) {
        this.title = title;
        this.category = category;
        this.date = date;
        this.notice = notice;
    }
}
