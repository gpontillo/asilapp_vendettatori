package com.vendettatori.asilapp.db;

import android.app.Activity;
import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class UserAnagraficaFileManager {
    private String filename;
    private Activity context;

    public UserAnagraficaFileManager(Activity context, String filename) {
        this.context = context;
        this.filename = filename;
    }

    public void saveUserData(UserAnagrafica userDataToSave) throws IOException {
        FileOutputStream fileStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
        ObjectOutputStream out = new ObjectOutputStream(fileStream);
        out.writeObject(userDataToSave);
        out.close();
        fileStream.close();
    }

    public UserAnagrafica retriveUserData() throws IOException, ClassNotFoundException {
        if(checkIfFileExist()) {
            FileInputStream fileStream = context.openFileInput(filename);
            ObjectInputStream in = new ObjectInputStream(fileStream);
            UserAnagrafica userData = (UserAnagrafica) in.readObject();
            in.close();
            fileStream.close();
            return userData;
        }
        else
            return null;
    }

    public boolean deleteFile() throws IOException, ClassNotFoundException {
        if(checkIfFileExist()) {
            File dir = context.getFilesDir();
            File file = new File(dir, filename);
            return file.delete();
        }
        else
            return false;
    }

    public boolean checkIfFileExist() {
        File file = new File(context.getFilesDir(),filename);
        return file.exists();
    }
}
