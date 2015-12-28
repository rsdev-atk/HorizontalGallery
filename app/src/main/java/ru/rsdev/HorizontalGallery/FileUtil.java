package ru.rsdev.HorizontalGallery;

import android.media.ExifInterface;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class FileUtil {
    DateUtil dateUtil = new DateUtil();
    ArrayList<String> fileList = new ArrayList<>();
    ArrayList<String> fileProperty = new ArrayList<>();
    ExifInterface exif = null;

    public ArrayList<ArrayList<String>> getDir(String dirPath) {

        File file = new File(dirPath);
        File[] filesArray = file.listFiles(); // получаем список файлов
        //Пробуем пройти по найденным в filesArray папкам
        if(filesArray != null) {
            for (File file1 : filesArray) {
                if (file1.isDirectory()) {
                    getDir(file1.getPath());
                }
            }
            for (File file2 : filesArray) {
                if (file2.isFile()) {   //проверяем, файл ли это
                    String filename = file2.getName();
                    String ext = filename.substring(filename.lastIndexOf('.') + 1,
                            filename.length());
                    if (ext.equalsIgnoreCase("JPG")) {
                        fileList.add(file2.getAbsolutePath());
                        ExifInterface exif = null;
                        try {
                            exif = new ExifInterface(file2.toString());
                            String exifAttribute = getExif(exif);
                            /*
                            if(exifAttribute == null)
                                continue;
                            */


                            if(exifAttribute == null)
                                exifAttribute = "Нет даты";

                            if(exifAttribute.equals("Нет даты")){
                                fileProperty.add("Нет даты");
                            }



                            else
                            fileProperty.add(dateUtil.getDateWithoutTime(exifAttribute));

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        ArrayList<ArrayList<String>> list = new ArrayList<>();
        list.add(fileList);
        list.add(fileProperty);
        return list;
    }

    private String getExif(ExifInterface exif) {
        return getTagString(ExifInterface.TAG_DATETIME, exif);
    }

    private String getTagString(String tag, ExifInterface exif) {
        return (exif.getAttribute(tag));
    }

    private void setTagString(String tag, ExifInterface exif, String value){
        try {
            exif.setAttribute(tag, value);
            exif.saveAttributes();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setNewDateFile(String path, String date){
        //char [] fullPuth = path.toCharArray();
        //String realPuth = String.copyValueOf(fullPuth,5,fullPuth.length-2);

        StringBuffer stringBuffer = new StringBuffer(path);
        stringBuffer.delete(0,6);
        String newPuth = stringBuffer.toString();

        try {
            exif = new ExifInterface(newPuth);
            setTagString(ExifInterface.TAG_DATETIME,exif,date);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
