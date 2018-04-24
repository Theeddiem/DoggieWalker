package com.game.eddieandmichael.classes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;

public class Dog
{

    private String ownerName;
    private transient Bitmap photo;

    public Dog(String ownerName, Bitmap photo) {
        this.ownerName = ownerName;
        this.photo = photo;
    }

    public String getOwnerName() {
        return ownerName;
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


