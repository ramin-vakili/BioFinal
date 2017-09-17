/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biofinal;

import java.util.ArrayList;
import java.util.List;


public class Hamming {

    public static int getHD(String d1, String d2) {
        int distance = 0;
        if (d1 == null || d2 == null) {
            System.out.println("Either String is null");
            System.exit(0); // or throw a RuntimeException here
        }

        d1 = d1.toUpperCase();
        d2 = d2.toUpperCase();

        if (d1.length() != d2.length()) {
            System.out.println("The string are not equal in length.");
            System.exit(0); //or throw a RuntimeException here
        }

        for (int i = 0; i < d1.length(); i++) {
            if (d1.charAt(i) != d2.charAt(i)) {
                distance++;
            }
        }

        return distance;
    }

    public static String[] mergeStrings(String[] x, String[] y) {
        List<String> mergedList = new ArrayList<String>();
        int xp = 0, yp = 0;
        while (xp < x.length && yp < y.length) {
            if (x[xp].compareTo(y[yp]) < 0) {
                mergedList.add(x[xp++]);
            } else if (x[xp].compareTo(y[yp]) > 0) {
                mergedList.add(y[yp++]);
            } else {
                mergedList.add(x[xp]);
                xp++;
                yp++;
            }
        }
        while (xp < x.length) {
            mergedList.add(x[xp++]);
        }
        while (yp < y.length) {
            mergedList.add(y[yp++]);
        }
        return mergedList.toArray(new String[0]);
    }

}
