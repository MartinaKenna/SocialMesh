package it.unimib.socialmesh.util;
import androidx.room.TypeConverter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.unimib.socialmesh.model.jsonFields.Image;

import java.lang.reflect.Type;
import java.util.List;

public class Converters {

    @TypeConverter
    public static String fromImageList(List<Image> imageList) {
        if (imageList == null) {
            return null;
        }

        Gson gson = new Gson();
        Type type = new TypeToken<List<Image>>() {}.getType();
        return gson.toJson(imageList, type);
    }

    @TypeConverter
    public static List<Image> toImageList(String imageListString) {
        if (imageListString == null) {
            return null;
        }

        Gson gson = new Gson();
        Type type = new TypeToken<List<Image>>() {}.getType();
        return gson.fromJson(imageListString, type);
    }
}
