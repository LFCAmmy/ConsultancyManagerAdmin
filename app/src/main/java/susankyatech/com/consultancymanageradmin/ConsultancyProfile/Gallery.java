package susankyatech.com.consultancymanageradmin.ConsultancyProfile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Gallery {
    public int id;
    public String image;
    public List<File> galleries;
    public ArrayList<String> images;

    public Gallery() {
    }

    public Gallery(String image) {
        this.image = image;
    }
}
