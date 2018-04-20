package com.game.eddieandmichael.classes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;

public class Walker {

    private String walkerName;
    private transient Bitmap photo;

    public Walker(String walkerName, Bitmap photo) {
        this.walkerName = walkerName;
        this.photo = photo;
    }

    public String getwalkerName() {
        return walkerName;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {


        photo.compress(Bitmap.CompressFormat.PNG,95,out);
        out.defaultWriteObject();
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {

        photo = BitmapFactory.decodeStream(in);
        in.defaultReadObject();
    }
}


