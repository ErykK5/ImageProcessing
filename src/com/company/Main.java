package com.company;

import com.company.processing.Ordfilt2;

public class Main {

    public static void main(String[] args) {

        if (Integer.parseInt(args[1]) > 4 || Integer.parseInt(args[1]) < 0 ) {
            System.out.println("Error");
        }

        Integer number = Integer.parseInt(args[1]);
        String path = args[0];

        switch (number) {
            case 1 :
                //
                break;
            case 2 :
                Ordfilt2 ordfilt2 = new Ordfilt2(path);
                ordfilt2.start();
                break;
            case 3 :
                //
                break;
            case 4 :
                //
                break;
            default:
                break;
        }
    }
}
