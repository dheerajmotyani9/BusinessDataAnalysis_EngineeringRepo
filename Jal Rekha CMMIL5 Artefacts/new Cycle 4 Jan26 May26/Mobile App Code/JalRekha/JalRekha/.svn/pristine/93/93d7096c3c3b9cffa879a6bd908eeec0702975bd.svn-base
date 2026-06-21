package mapitgis.jalnigam.rfi.helper;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import mapitgis.jalnigam.BuildConfig;

public class ImageHelper {

    public static final int SELECT_IMAGE_CODE = 1;
    public static final int CAPTURE_IMAGE_CODE = 2;

    public Uri imageUri;
    private Activity context;
    public String photoFilePath;

    public static Bitmap returnedBitmap;

    public ImageHelper(Activity context) {
        this.context = context;
    }

    public void showCaptureOrSelectImageDialog() {

        final CharSequence[] items = {"Gallery", "Camera"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Upload Image");
        builder.setItems(items, (dialog, which) -> {
            if (which == 0) {
                selectImage();
            } else if (which == 1) {
                dispatchTakePictureIntent();
            }

        });

        builder.setNegativeButton("Cancel",
                (dialog, id) -> {
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        context.startActivityForResult(Intent.createChooser(intent, "Select image"), SELECT_IMAGE_CODE);
    }

    public void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (photoFile != null) {
                imageUri = FileProvider.getUriForFile(context,
                        String.format("%s.fileProvider", BuildConfig.APPLICATION_ID),
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                context.startActivityForResult(takePictureIntent, CAPTURE_IMAGE_CODE);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        photoFilePath = image.getAbsolutePath();

        Log.e("tag", "createImageFile " + photoFilePath);
        return image;
    }

    public String getRealPath(Uri uri) {

        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        Cursor cursor = context.getContentResolver().query(uri, filePathColumn, null, null, null);
        cursor.moveToFirst();

        int index = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(index);
        cursor.close();

        return picturePath;
    }

    private static String queryName(ContentResolver resolver, Uri uri) {
        Cursor returnCursor =
                resolver.query(uri, null, null, null, null);
        assert returnCursor != null;
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        String name = returnCursor.getString(nameIndex);
        returnCursor.close();
        return name;
    }

    public String imageToBase64(String filePath) {
        Bitmap bm = BitmapFactory.decodeFile(filePath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 50, baos); // bm is the bitmap object
        byte[] byteArrayImage = baos.toByteArray();
        return Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
    }

    public String moveImageToDestination(String path,double lat,double lon) {

        // TODO: 11-09-2024 : this code for overlay text. on image ---------------------------------
        Bitmap rotatedBitmap = BitmapFactory.decodeFile(path);
        Bitmap bitmap = rotateBitmapIfNeeded(rotatedBitmap,path);
        String locationText = "Latitude: " + lat + ", Longitude: " + lon + " | Date & Time " + new Date();
        Bitmap imageWithLocation = addTextToImage(bitmap, locationText);
        File updatedFile = bitmapToFile(imageWithLocation);

        // TODO: 11-09-2024 : ENDS HERE --------------------------------
        File pictureFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File directory = new File(pictureFolder, PermissionHelper.FOLDER_NAME);

        if (!directory.exists()) {
            boolean c = directory.mkdir();//directory is created;
        }

        String destPath = directory.getPath() + "/" + new File(updatedFile.getPath()).getName();

        File source = new File(updatedFile.getPath().replace("%20", ""));
        File destination = new File(destPath);

        if (source.exists()) {

            try {
                FileChannel srcChannel = new FileInputStream(source).getChannel();
                FileChannel destChannel = new FileOutputStream(destination).getChannel();

                destChannel.transferFrom(srcChannel, 0, srcChannel.size());
                srcChannel.close();
                destChannel.close();

            } catch (IOException e) {
                e.printStackTrace();
                //  Log.e("Exception : ", "" + e.getMessage());
            }
        }



        //  compressImage(destination.getPath(), destination);
        return destination.getPath();

    }

    private static final float maxHeight = 1280.0f;
    private static final float maxWidth = 1280.0f;

    public void compressImage(String imagePath, File destinationPath,ImageCompressListener listener) {

        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(imagePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

        float imgRatio = (float) actualWidth / (float) actualHeight;
        float maxRatio = maxWidth / maxHeight;

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);
        options.inJustDecodeBounds = false;
        options.inDither = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
            bmp = BitmapFactory.decodeFile(imagePath, options);

        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.RGB_565);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

        if (bmp != null) {
            bmp.recycle();
        }

        ExifInterface exif;
        try {
            exif = new ExifInterface(imagePath);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
            } else if (orientation == 3) {
                matrix.postRotate(180);
            } else if (orientation == 8) {
                matrix.postRotate(270);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileOutputStream out = null;
        // String filepath = getFilename();
        try {
            out = new FileOutputStream(destinationPath.getPath());

            //write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
            listener.onImageCompressCompleted(destinationPath.getPath(),false);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            listener.onImageCompressCompleted("Failed to compress image",true);
        }

        //  return filepath;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;

        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    public Bitmap addTextToImage(Bitmap originalBitmap, String text) {
        Bitmap mutableBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(mutableBitmap);
        Paint paint = new Paint();
        paint.setColor(Color.RED);  // Text color
        paint.setTextSize(60);      // Text size
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.LEFT);

        // Draw the text (latitude and longitude) at the bottom of the image
        canvas.drawText(text, 50, mutableBitmap.getHeight() - 50, paint);

        return mutableBitmap;
    }

    public File bitmapToFile(Bitmap bitmap) {
        String savedImagePath = "";
        String imageFileName = "JPEG_" + System.currentTimeMillis() + ".jpg";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/JalRekhaGallery");

        boolean success = true;
        if (!storageDir.exists()) {
            success = storageDir.mkdirs();
        }
        if (success) {
            File imageFile = new File(storageDir, imageFileName);
            savedImagePath = imageFile.getAbsolutePath();
            try {
                OutputStream fOut = new FileOutputStream(imageFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                fOut.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return new File(savedImagePath);  // Return the file object (can be used for further operations)
    }

    private int getExifRotation(String imagePath) {
        try {
            ExifInterface exif = new ExifInterface(imagePath);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return 90;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    return 180;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    return 270;
                default:
                    return 0;  // No rotation needed
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public Bitmap rotateBitmapIfNeeded(Bitmap bitmap, String imagePath) {
        int rotation = getExifRotation(imagePath);
        if (rotation != 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(rotation);
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        }
        return bitmap;
    }

    public String getFileSize(long size) {

        DecimalFormat df = new DecimalFormat("0.00");

        float sizeKb = 1024.0f;
        float sizeMb = sizeKb * sizeKb;
        float sizeGb = sizeMb * sizeKb;
        float sizeTerra = sizeGb * sizeKb;


        if(size < sizeMb)
            return df.format(size / sizeKb)+ " Kb";
        else if(size < sizeGb)
            return df.format(size / sizeMb) + " Mb";
        else if(size < sizeTerra)
            return df.format(size / sizeGb) + " Gb";

        return "";
    }


    public String getFileExtension(String path){
        return path.substring(path.lastIndexOf("."));
    }

    public interface ImageCompressListener {
        void onImageCompressCompleted(String path, boolean isError);
    }

}
