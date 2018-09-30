package com.example.aluno.ichurch.Model;

/**
 * Created by aluno on 20/06/2018.
 */

public class Results {
    private Photos[] photos;

    private String id;

    private String place_id;

    private String icon;

    private String name;

    private String formatted_address;

    private String phone_number;

    private String rating;

    private String[] types;

    private String reference;

    private Geometry geometry;

    private Opening_hours opening_hours;


    public Photos[] getPhotos ()
    {
        return photos;
    }

    public void setPhotos (Photos[] photos)
    {
        this.photos = photos;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getPlace_id ()
    {
        return place_id;
    }

    public void setPlace_id (String place_id)
    {
        this.place_id = place_id;
    }

    public String getIcon ()
    {
        return icon;
    }

    public void setIcon (String icon)
    {
        this.icon = icon;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getFormatted_address ()
    {
        return formatted_address;
    }

    public void setFormatted_address (String formatted_address)
    {
        this.formatted_address = formatted_address;
    }

    public String getRating ()
    {
        return rating;
    }

    public void setRating (String rating)
    {
        this.rating = rating;
    }

    public String[] getTypes ()
    {
        return types;
    }

    public void setTypes (String[] types)
    {
        this.types = types;
    }

    public String getReference ()
    {
        return reference;
    }

    public void setReference (String reference)
    {
        this.reference = reference;
    }

    public Geometry getGeometry ()
    {
        return geometry;
    }

    public void setGeometry (Geometry geometry)
    {
        this.geometry = geometry;
    }

    public String getPhone_number() { return phone_number; }

    public void setPhone_number(String phone_number) { this.phone_number = phone_number; }

    public Opening_hours getOpening_hours() { return opening_hours; }

    public void setOpening_hours(Opening_hours opening_hours) { this.opening_hours = opening_hours; }

    @Override
    public String toString()
    {
        return "ClassPojo [photos = "+photos+", id = "+id+", place_id = "+place_id+", icon = "+icon+", name = "+name+", formatted_address = "+formatted_address+", rating = "+rating+", types = "+types+", reference = "+reference+", geometry = "+geometry+"]";
    }
}
