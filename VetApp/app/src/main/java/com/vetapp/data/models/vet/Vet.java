package com.vetapp.data.models.vet;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Vet implements Parcelable {

    public static final Creator<Vet> CREATOR = new Creator<Vet>() {
        @Override
        public Vet createFromParcel(Parcel in) {
            return new Vet(in);
        }

        @Override
        public Vet[] newArray(int size) {
            return new Vet[size];
        }
    };

    private String clinicName;
    private String address;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    @DocumentId
    private String docid;


    public Vet() {
    }

    @Exclude
    private Bitmap profilePic;

    protected Vet(Parcel in) {
        docid = in.readString();
        clinicName = in.readString();
        address = in.readString();
        email = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        phone = in.readString();
        profilePic = in.readParcelable(Bitmap.class.getClassLoader());
    }

    @Exclude
    public Bitmap getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(Bitmap profilePic) {
        this.profilePic = profilePic;
    }

    public String getDocid() {
        return docid;
    }

    public String getName() {
        return firstName + " " + lastName;
    }

    public String getClinicName() {
        return clinicName;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(docid);
        dest.writeString(clinicName);
        dest.writeString(address);
        dest.writeString(email);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(phone);
        dest.writeParcelable(profilePic, flags);
    }
}
