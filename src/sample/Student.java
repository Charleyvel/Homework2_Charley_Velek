package sample;

import java.util.UUID;

public class Student
{
    public String Name;
    public UUID ID;
    public int Age;
    public String Major;
    public double GPA;

    public String toString() //Where the signature is returning strings
    {
        return(this.Name + " " + this.Age + " " + this.Major + " " + this.GPA);
    }
}

