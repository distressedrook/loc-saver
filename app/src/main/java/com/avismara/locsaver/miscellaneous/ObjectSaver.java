package com.avismara.locsaver.miscellaneous;

import android.content.Context;

import com.avismara.locsaver.entities.LocationInfoEntity;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Created by Avismara on 05/04/15.
 */
public class ObjectSaver {
    public static String fileName = "savedLocations.ser";
    // Serializes an object and saves it to a file
    public static void writeSavedLocationsToFile(Context context,ArrayList<LocationInfoEntity> savedLocations) {
        try {
            FileOutputStream fileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(savedLocations);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Creates an object by reading it from a file
    public static ArrayList<LocationInfoEntity> readSavedLocationsFromFile(Context context) {
        ArrayList<LocationInfoEntity> savedLocations = null;
        try {
            FileInputStream fileInputStream = context.openFileInput(fileName);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            savedLocations = (ArrayList<LocationInfoEntity>) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return savedLocations;
    }

}
