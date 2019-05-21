package com.ashok.csv.typeinference;

public class InferTypeImpl implements InferType{

    // I have coded only 2 types for now - Double and String.
    // The correct order to approach type inference is as follows
    // try to parse as Integer, on failure,
    // try Long, on failure,
    // try Double, on failure,
    // try Timestamp, on failure,
    // try Boolean, on failure
    // parse as String

    public  String inferType (String data) {
        if ( null != data ) {
            try {
                double d = Double.parseDouble(data);
                return "Double";
            } catch ( NumberFormatException e ) {
                return "String";
            }
        }
        return null;
    }



}
