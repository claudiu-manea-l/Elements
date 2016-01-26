package com.codez.elements.restful_list.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.codez.elements.db.tables.CStaffTable;
import com.codez.mainlibrary.restful_list.model.SQLObject;
import com.codez.mainlibrary.utilities.OtherUtils;

import java.util.List;

/**
 * Created by Eptron on 1/25/2016.
 */
public class StaffModel extends SQLObject{
    private String Name;
    private String NameFirst;
    private String Mobile;
    private String Email;
    private String City;
    private String ZipCode;
    private String Street;
    private String StreetNumber;
    private String StreetBox;
    private String Telephone;
    private String Fax;
    private String LanguageNL;
    private String LanguageFR;
    private String LanguageDE;
    private String LanguageEN;
    private String PictureURL;
    private long timestamp;
    private Integer PersonnelID;
    private List<AvailableItem> Availability;

    @Override
    public int getId() {
        return PersonnelID;
    }


    public StaffModel() {
    }

    public StaffModel(Cursor cursor) {
        Name = cursor.getString(cursor.getColumnIndex(CStaffTable.NAME));
        NameFirst = cursor.getString(cursor.getColumnIndex(CStaffTable.NAME_FIRST));
        Mobile = cursor.getString(cursor.getColumnIndex(CStaffTable.MOBILE));
        Email = cursor.getString(cursor.getColumnIndex(CStaffTable.EMAIL));
        City = cursor.getString(cursor.getColumnIndex(CStaffTable.CITY));
        ZipCode = cursor.getString(cursor.getColumnIndex(CStaffTable.ZIP_CODE));
        Street = cursor.getString(cursor.getColumnIndex(CStaffTable.STREET));
        StreetNumber = cursor.getString(cursor.getColumnIndex(CStaffTable.STREET_NUMBER));
        StreetBox = cursor.getString(cursor.getColumnIndex(CStaffTable.STREET_BOX));
        Telephone = cursor.getString(cursor.getColumnIndex(CStaffTable.TELEPHONE));
        Fax = cursor.getString(cursor.getColumnIndex(CStaffTable.FAX));
        LanguageNL = cursor.getString(cursor.getColumnIndex(CStaffTable.LANGUAGE_NL));
        LanguageFR = cursor.getString(cursor.getColumnIndex(CStaffTable.LANGUAGE_FR));
        LanguageDE = cursor.getString(cursor.getColumnIndex(CStaffTable.LANGUAGE_DE));
        LanguageEN = cursor.getString(cursor.getColumnIndex(CStaffTable.LANGUAGE_EN));
        PictureURL = cursor.getString(cursor.getColumnIndex(CStaffTable.PICTURE_URL));
        PersonnelID = cursor.getInt(cursor.getColumnIndex(CStaffTable.ID_STAFF));
    }

    @Override
    public ContentValues toContentValues() {
        ContentValues temp = new ContentValues();
        temp.put(CStaffTable.ID_STAFF, PersonnelID);
        temp.put(CStaffTable.NAME, getName());
        temp.put(CStaffTable.NAME_FIRST, getNameFirst());
        temp.put(CStaffTable.MOBILE, getMobile());
        temp.put(CStaffTable.EMAIL, getEmail());
        temp.put(CStaffTable.CITY, getCity());
        temp.put(CStaffTable.ZIP_CODE, getZipCode());
        temp.put(CStaffTable.STREET, getStreet());
        temp.put(CStaffTable.STREET_NUMBER, getStreetNumber());
        temp.put(CStaffTable.STREET_BOX, getStreetBox());
        temp.put(CStaffTable.TELEPHONE, getTelephone());
        temp.put(CStaffTable.FAX, getFax());
        temp.put(CStaffTable.LANGUAGE_DE, getLanguageDE());
        temp.put(CStaffTable.LANGUAGE_NL, getLanguageNL());
        temp.put(CStaffTable.LANGUAGE_EN, getLanguageEN());
        temp.put(CStaffTable.LANGUAGE_FR, getLanguageFR());
        temp.put(CStaffTable.PICTURE_URL, getPicture());
        temp.put(CStaffTable.TIMESTAMP, timestamp);
        return temp;
    }

    public List<AvailableItem> getAvailability() {
        return Availability;
    }

    public String getName() {
        return OtherUtils.checkNull(Name);
    }

    public String getNameFirst() {
        return OtherUtils.checkNull(NameFirst);
    }

    public String getMobile() {
        return OtherUtils.checkNull(Mobile);
    }

    public String getEmail() {
        return OtherUtils.checkNull(Email);
    }

    public String getCity() {
        return OtherUtils.checkNull(City);
    }

    public String getStreet() {
        return OtherUtils.checkNull(Street);
    }

    public String getStreetNumber() {
        return OtherUtils.checkNull(StreetNumber);
    }

    public String getStreetBox() {
        return OtherUtils.checkNull(StreetBox);
    }

    public String getTelephone() {
        return OtherUtils.checkNull(Telephone);
    }

    public String getFax() {
        return OtherUtils.checkNull(Fax);
    }

    public String getZipCode() {
        return OtherUtils.checkNull(ZipCode);
    }

    public String getLanguageNL() {
        return OtherUtils.checkNull(LanguageNL);
    }

    public String getLanguageFR() {
        return OtherUtils.checkNull(LanguageFR);
    }

    public String getLanguageDE() {
        return OtherUtils.checkNull(LanguageDE);
    }

    public String getLanguageEN() {
        return OtherUtils.checkNull(LanguageEN);
    }

    public String getPictureImage() {
        return OtherUtils.checkNull(PictureURL);
    }

    public Integer getPersonnelID() {
        return OtherUtils.checkIntNull(PersonnelID);
    }

    public String getPicture() {
        return PictureURL;
    }
}
