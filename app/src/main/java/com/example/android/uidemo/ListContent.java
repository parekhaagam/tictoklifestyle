package com.example.android.uidemo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Honey on 15-Jun-16.
 */
public class ListContent implements Parcelable{
    //String name,salary;

    String name;
    String Timestamp, First_Name, Last_Name,
            Contact_Number,
            City, State, Pin_Code,
            Email_ID, Item_Purchased, Order_Description,
            Mode_Of_Payment, Total_Quantity, Total_Amount,
            Status, Company, TrackingNo;
    @SerializedName("Address")
    String Address;

    @SerializedName("Landmark_")
    String Landmark;

    public ListContent(String Timestamp, String First_Name, String Last_Name,
                       String Contact_Number, String Address, String Landmark,
                       String City, String State, String Pin_Code,
                       String Email_ID, String Item_Purchased, String Order_Description,
                       String Mode_Of_Payment, String Total_Quantity, String Total_Amount,
                       String Status, String Company, String TrackingNo
    ) {
        this.Timestamp = Timestamp;
        this.First_Name = First_Name;
        this.Last_Name = Last_Name;
        this.Contact_Number = Contact_Number;
        this.Address = Address;
        this.Landmark = Landmark;
        this.City = City;
        this.State = State;
        this.Pin_Code = Pin_Code;
        this.Email_ID = Email_ID;
        this.Item_Purchased = Item_Purchased;
        this.Order_Description = Order_Description;
        this.Mode_Of_Payment = Mode_Of_Payment;
        this.Total_Quantity = Total_Quantity;
        this.Total_Amount = Total_Amount;
        this.Status = Status;
        this.Company = Company;
        this.TrackingNo = TrackingNo;
        this.name = this.First_Name + " " + this.getLast_Name();
    }

    protected ListContent(Parcel in) {
        name = in.readString();
        Timestamp = in.readString();
        First_Name = in.readString();
        Last_Name = in.readString();
        Contact_Number = in.readString();
        City = in.readString();
        State = in.readString();
        Pin_Code = in.readString();
        Email_ID = in.readString();
        Item_Purchased = in.readString();
        Order_Description = in.readString();
        Mode_Of_Payment = in.readString();
        Total_Quantity = in.readString();
        Total_Amount = in.readString();
        Status = in.readString();
        Company = in.readString();
        TrackingNo = in.readString();
        Address = in.readString();
        Landmark = in.readString();
    }

    public static final Creator<ListContent> CREATOR = new Creator<ListContent>() {
        @Override
        public ListContent createFromParcel(Parcel in) {
            return new ListContent(in);
        }

        @Override
        public ListContent[] newArray(int size) {
            return new ListContent[size];
        }
    };

    public String getTimestamp() {
        return Timestamp;
    }

    public void setTimestamp(String timestamp) {
        Timestamp = timestamp;
    }

    public String getFirst_Name() {
        return First_Name;
    }

    public void setFirst_Name(String first_Name) {
        First_Name = first_Name;
    }

    public String getLast_Name() {
        return Last_Name;
    }

    public void setLast_Name(String last_Name) {
        Last_Name = last_Name;
    }

    public String getContact_Number() {
        return Contact_Number;
    }

    public void setContact_Number(String contact_Number) {
        Contact_Number = contact_Number;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getLandmark() {
        return Landmark;
    }

    public void setLandmark(String landmark) {
        Landmark = landmark;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getPin_Code() {
        return Pin_Code;
    }

    public void setPin_Code(String pin_Code) {
        Pin_Code = pin_Code;
    }

    public String getEmail_ID() {
        return Email_ID;
    }

    public void setEmail_ID(String email_ID) {
        Email_ID = email_ID;
    }

    public String getItem_Purchased() {
        return Item_Purchased;
    }

    public void setItem_Purchased(String item_Purchased) {
        Item_Purchased = item_Purchased;
    }

    public String getOrder_Description() {
        return Order_Description;
    }

    public void setOrder_Description(String order_Description) {
        Order_Description = order_Description;
    }

    public String getMode_Of_Payment() {
        return Mode_Of_Payment;
    }

    public void setMode_Of_Payment(String mode_Of_Payment) {
        Mode_Of_Payment = mode_Of_Payment;
    }

    public String getTotal_Quantity() {
        return Total_Quantity;
    }

    public void setTotal_Quantity(String total_Quantity) {
        Total_Quantity = total_Quantity;
    }

    public String getTotal_Amount() {
        return Total_Amount;
    }

    public void setTotal_Amount(String total_Amount) {
        Total_Amount = total_Amount;
    }

    public String getCompany() {
        return Company;
    }

    public void setCompany(String company) {
        Company = company;
    }

    public String getTrackingNo() {
        return TrackingNo;
    }

    public void setTrackingNo(String trackingNo) {
        TrackingNo = trackingNo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(Timestamp);
        parcel.writeString(First_Name);
        parcel.writeString(Last_Name);
        parcel.writeString(Contact_Number);
        parcel.writeString(City);
        parcel.writeString(State);
        parcel.writeString(Pin_Code);
        parcel.writeString(Email_ID);
        parcel.writeString(Item_Purchased);
        parcel.writeString(Order_Description);
        parcel.writeString(Mode_Of_Payment);
        parcel.writeString(Total_Quantity);
        parcel.writeString(Total_Amount);
        parcel.writeString(Status);
        parcel.writeString(Company);
        parcel.writeString(TrackingNo);
        parcel.writeString(Address);
        parcel.writeString(Landmark);
    }
}
