package com.company.processing;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class Regionprops {

    private String path;
    private int width;
    private int height;
    private int diameter;


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
        int[][] minVal = new int[width/diameter+1][height/diameter+1];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                values[j][i] = image.getRGB(j, i);
            }
        }

        for (int i = 0, x = 0; i < height; i+=diameter, x++) {
            for (int j = 0, y = 0; j < width; j+=diameter, y++) {
                if (checkIfObject(values,i,j)) {
                    minVal[y][x] = 1;
                } else {
                    minVal[y][x] = 0;
                }
            }
        }

        findCenter(minVal, image, height, width);

        saveImage(file,image);

    }

    private void findCenter(int[][] minVal, BufferedImage image, int height, int width) {

        List<Point> list = new ArrayList<>();
        int x = 0;
        int y = 0;
        for (int i = 0, i2 = i; i < height/diameter-5; i++) {
            for (int j = 0, j2 = j; j < width/diameter-5; j++) {
                if (minVal[i][j] == 1) {
                    while (j2 != width/diameter) {
                        if (minVal[i2][j2++] == 1) {
                            x++;
                        } else if (minVal[i2++][j2] == 1) {
                            y++;
                        } else {
                            list.add(new Point(((j+x/2)*diameter),((i+y/2))*diameter));
                            j += x;
                            x = 0;
                            y = 0;
                            i2 = i;
                            j2 = j;
                            break;
                        }
                    }
                }
            }
        }

        Point point;
        int p = Color.RED.getRGB();
        for (int c = 0; c < list.size(); c++) {
            point = list.get(c);
            image.setRGB(point.x,point.y,p);
            image.setRGB(point.x+1,point.y,p);
            image.setRGB(point.x+1,point.y+1,p);
            image.setRGB(point.x,point.y+1,p);
            image.setRGB(point.x-1,point.y+1,p);
            image.setRGB(point.x-1,point.y,p);
            image.setRGB(point.x-1,point.y-1,p);
            image.setRGB(point.x,point.y-1,p);
            image.setRGB(point.x+1,point.y-1,p);
        }
    }

    public void saveImage(File file, BufferedImage image) {
        try
        {
            file = new File("/home/erykk/IdeaProjects/JavaImageProcessing/src/images/result2.jpg");
            ImageIO.write(image, "jpg", file);
        }
        catch(IOException e)
        {
            System.out.println(e);
        }
    }


    private boolean checkIfObject(int[][] arr, int height, int width) {
        for (int i = height; i<height+diameter; i++) {
            for (int j = width; j<width+diameter; j++) {
                if (arr[i][j] == Color.BLACK.getRGB()) {
                    return false;
                }
            }
        }
        return true;
    }

    public void info() {
        System.out.print("Diameter: ");
        Scanner sc = new Scanner(System.in);
        setDiameter(sc.nextInt());
    }

    public Regionprops(String path) {
        this.path = path;
    }

    public int getDiameter() {
        return diameter;
    }

    public void setDiameter(int diameter) {
        this.diameter = diameter;
    }
}
