package com.company.processing;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Close {

    private String path;
    private int radius;
    private int[][] mask;
    private int width;
    private int height;

    public void start() {

        info();

        BufferedImage image = null;
        File file = null;

        try {
            file = new File(path);
            image = ImageIO.read(file);

        } catch (Exception e) {
            System.out.println(e);
        }

        width = image.getWidth();
        height = image.getHeight();

        int[][] values = new int[width][height];

        createCircle();

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                values[j][i] = image.getRGB(j, i);
                if (values[j][i] == Color.WHITE.getRGB() )
                    values[j][i] = 1;
                else
                    values[j][i] = 0;
            }
        }

        dylatation(values);

        for (int i = 0; i < height; i++) {
            for (int j = 0; j< width; j++) {
                if (values[j][i] == 2)
                    values[j][i] = 1;
            }
        }

        erosion(values);

        for (int i = 0; i < height; i++) {
            for (int j = 0; j< width; j++) {
                if (values[j][i] == 2)
                    values[j][i] = 1;
            }
        }

        int p;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (values[j][i] == 1 || values[j][i] == 2) {
                    p = Color.BLACK.getRGB();
                    image.setRGB(j,i,p);
                } else {
                    p = Color.WHITE.getRGB();
                    image.setRGB(j,i,p);
                }
            }
        }

        saveImage(file,image);
    }

    private void erosion(int[][] values) {
        for (int i = 0; i < height; i++ ) {
            for (int j = 0; j < width; j++ ) {
                if (checkIfFill2(values,i,j)) {
                    values[j][i] = 0;
                } else {
                    values[j][i] = 2;
                }
            }
        }
    }

    private boolean checkIfFill2(int[][] values, int h, int w) {
        if (h<radius || h>height-radius || w<radius || w>width-radius) {
            return false;
        }
        for (int i = h+radius, x = 0; i >= h-radius && h+radius<height; i--, x++) {
            for (int j = w-radius, y = 0; j <= w+radius && w+radius<width; j++, y++) {
                if ((values[j][i] != mask[x][y]) && values[j][i] == 1) {
                    return false;
                }
            }
        }
        return true;
    }

    private void dylatation(int[][] values) {
        for (int i = 0; i < height; i++ ) {
            for (int j = 0; j < width; j++ ) {
                if (checkIfFill(values,i,j)) {
                    values[j][i] = 0;
                } else {
                    values[j][i] = 2;
                }
            }
        }
    }

    private boolean checkIfFill(int[][] values, int h, int w) {

        if (h<radius || h>height-radius || w<radius || w>width-radius) {
            if (h<radius && w<radius) {
                return false; //fix
            } else if (h<radius && w>width-radius) {
                return false; //fix
            } else if (w<radius && h>height-radius) {
                return false; //fix
            } else if (h>height-radius && w>width-radius) {
                return false; //fix
            } else if (h<radius) {
                /*
                for (int i = h+(radius-h), x = 0; i <= h-radius; i++, x++) {
                    for (int j = w-radius, y = 0; j <= w+radius; j++, y++) {
                        if ((values[j][i] == mask[x][y]) && values[j][i] == 1) {
                            return true;
                        }
                    }
                }
                */
                return false;
            } else if (h>height-radius) {
                /*
                for (int i = h+radius, x = 0; i <= h-radius && i < height; i++, x++) {
                    for (int j = w-radius, y = 0; j <= w+radius; j++, y++) {
                        if ((values[j][i] == mask[x][y]) && values[j][i] == 1) {
                            return true;
                        }
                    }
                }
                */
                return false;
            } else if (w<radius) {
                /*
                for (int i = h+radius, x = 0; i <= h-radius; i++, x++) {
                    for (int j = w-(radius-w), y = 0; j <= w+radius; j++, y++) {
                        if ((values[j][i] == mask[x][y]) && values[j][i] == 1) {
                            return true;
                        }
                    }
                }
                 */
                return false;
            } else {
                return false;
            }
        } else {
            for (int i = h+radius, x = 0; i >= h-radius && h+radius<height; i--, x++) {
                for (int j = w-radius, y = 0; j <= w+radius && w+radius<width; j++, y++) {
                    if ((values[j][i] == mask[x][y]) && values[j][i] == 1) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    private void createCircle() {

        double midPoint = (mask.length-1)/2.0;
        for (int col = 0; col < mask.length; col++) {
            int[] row = new int[mask.length];
            double yy = col-midPoint;
            for (int x=0; x<row.length; x++) {
                double xx = x-midPoint;
                if (Math.sqrt(xx*xx+yy*yy)<=midPoint)
                    row[x] = 1;
            }
            mask[col] = row;
        }

    }

    private void info() {
        System.out.print("Radius: ");
        Scanner sc = new Scanner(System.in);
        setRadius(sc.nextInt());
        mask = new int[2*radius+1][2*radius+1];
    }

    public void saveImage(File file, BufferedImage image) {
        try
        {
            file = new File("/home/erykk/IdeaProjects/JavaImageProcessing/src/images/result3.jpg");
            ImageIO.write(image, "jpg", file);
        }
        catch(IOException e)
        {
            System.out.println(e);
        }
    }

    public Close(String path) {
        this.path = path;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }
}
