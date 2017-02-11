package com.kush.app.stayput.manager;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by Kush on 11.02.2017.
 *
 * FileManager class that handles serialization of generic serializables
 */

public class FileManager {

    public static final String FILENAME = "StayPutData.ser";

    /**
     * Saves a serializable object.
     *
     * @param context The application context.
     * @param objectToSave The object to save.
     * @param fileName The name of the file.
     * @param <T> The type of the object.
     */

    public static <T extends Serializable> void saveFile(Context context, T objectToSave, String fileName) {
        try {
            FileOutputStream fileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(objectToSave);

            objectOutputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads a serializable object.
     *
     * @param context The application context.
     * @param fileName The filename.
     * @param <T> The object type.
     *
     * @return the serializable object.
     */

    public static<T extends Serializable> T readFile(Context context, String fileName) {
        T objectToReturn = null;

        try {
            FileInputStream fileInputStream = context.openFileInput(fileName);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            //Assume correct file is opened. Might want to add an extra check for this.
            //noinspection unchecked
            objectToReturn = (T) objectInputStream.readObject();

            objectInputStream.close();
            fileInputStream.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return objectToReturn;
    }

    /**
     * Deletes a specified file.
     *
     * @param context The application context.
     * @param filename The name of the file.
     */

    public static void deleteFile(Context context, String filename) {
        context.deleteFile(filename);
    }

}
