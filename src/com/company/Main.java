package com.company;

import com.company.processing.Close;
import com.company.processing.GeoDistance;
import com.company.processing.Ordfilt2;
import com.company.processing.Regionprops;

public class Main {

    public static void main(String[] args) {

        if (Integer.parseInt(args[1]) > 4 || Integer.parseInt(args[1]) < 0 ) {
            System.out.println("Error");
        }

        Integer number = Integer.parseInt(args[1]);
        String path = args[0];

        switch (number) {
            case 1 :
                Regionprops regionprops = new Regionprops(path);
                regionprops.start();
                break;
            case 2 :
                Ordfilt2 ordfilt2 = new Ordfilt2(path);
                ordfilt2.start();
                break;
            case 3 :
                Close close = new Close(path);
                close.start();
                break;
            case 4 :
                GeoDistance geoDistance = new GeoDistance(path);
                geoDistance.start();
                break;
            default:
                break;
        }
    }
}
