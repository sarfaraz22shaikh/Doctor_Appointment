package com.example.doctor_appointment.Repository;

import static com.cloudinary.utils.ObjectUtils.*;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

import java.util.Map;
import java.util.concurrent.Executors;

public class CloudinaryRepository {

    private final Cloudinary cloudinary;

    public CloudinaryRepository() {
        cloudinary = new Cloudinary(asMap(
                "cloud_name", "dmbxephvk",
                "api_key", "353141219651281",
                "api_secret", "_4vOT0InqwwdsqEYankhahr6vMM"
        ));
    }

    public Task<String> uploadImage(Uri imageUri, Context context) {
        return Tasks.call(Executors.newSingleThreadExecutor(), () -> {
            String filePath = getRealPathFromURI(imageUri, context);
            Map uploadResult = cloudinary.uploader().upload(filePath, emptyMap());
            return uploadResult.get("secure_url").toString(); // Return Cloudinary URL
        });
    }

    private String getRealPathFromURI(Uri contentUri, Context context) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }
}
