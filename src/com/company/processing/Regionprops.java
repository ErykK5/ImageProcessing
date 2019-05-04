package com.company.processing;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
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

        findCenter(minVal, image);

        saveImage(file,image);

    }

    private void findCenter(int[][] minVal, BufferedImage image) {


        class NastedPoint {
            public int x1;
            public int y1;
            public int farX;
            public int farY;
            public int nearX;
            public int nearY;
            public int iter;

            public NastedPoint(int x1, int y1, int farX, int farY, int iter, int nearX, int nearY) {
                this.x1 = x1;
                this.y1 = y1;
                this.farX = farX;
                this.farY = farY;
                this.nearX = nearX;
                this.nearY = nearY;
                this.iter = iter;
            }
        }
        List<NastedPoint> listFirst = new ArrayList<>();
        int arrIter = 0;

        //List<Point> list = new ArrayList<>();
        int iter = 2;
        int x = 0;
        int y = 0;
        for (int i = 0, i2 = i; i < height/diameter-5; i++) {
            for (int j = 0, j2 = j; j < width/diameter-5; j++) {
                if (minVal[i][j] == 1) {
                    while (j2 != width/diameter) {
                        if (minVal[i2][j2+1] == 1) {
                            x++;
                            j2++;
                        } else if (minVal[i2+1][j2] == 1) {
                            y++;
                            i2++;
                        } else {
                            listFirst.add(new NastedPoint(i,j,0,0,iter,width ,height));
                            //arr[arrIter] = new NastedPoint(i, j,0,0, iter);
                            paintObject(i, j, minVal, iter );
                            iter++;
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

        NastedPoint[] arr = new NastedPoint[listFirst.size()];
        for (int it = 0; it < listFirst.size(); it++)
            arr[it] = listFirst.get(it);

        for (int i = 0; i < height/diameter-5; i++) {
            for (int j = 0; j < width/diameter-5; j++) {
                if (minVal[i][j] != 0 && minVal[i][j] != 1) {
                    for (int c = 0; c < arr.length; c++) {
                        if (minVal[i][j] == arr[c].iter && j*diameter > arr[c].farX*diameter)
                            arr[c].farX = j*diameter;
                        if (minVal[i][j] == arr[c].iter && i*diameter > arr[c].farY*diameter)
                            arr[c].farY = i*diameter;
                        if (minVal[i][j] == arr[c].iter && j*diameter < arr[c].nearX*diameter)
                            arr[c].nearX = j*diameter;
                        if (minVal[i][j] == arr[c].iter && i*diameter < arr[c].nearY*diameter)
                            arr[c].nearY = i*diameter;
                    }
                }
            }
        }

        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter("test.txt");
        } catch (IOException e) {
            System.out.println(e);
        }

        int p = Color.RED.getRGB();
        int tmpX;
        int tmpY;
        for (int c = 0; c < arr.length; c++) {
            tmpX = (arr[c].farX+arr[c].nearX)/2;
            tmpY = (arr[c].farY+arr[c].nearY)/2;
            printWriter.println(tmpX + " " + tmpY);
            image.setRGB(tmpX,tmpY,p);
            image.setRGB(tmpX+1,   tmpY,p);
            image.setRGB(tmpX+1,tmpY+1,p);
            image.setRGB(   tmpX,  tmpY+1,p);
            image.setRGB(tmpX-1,tmpY+1,p);
            image.setRGB(tmpX-1,   tmpY,p);
            image.setRGB(tmpX-1,tmpY-1,p);
            image.setRGB(    tmpX ,tmpY-1,p);
            image.setRGB(tmpX+1,tmpY-1,p);
        }

        printWriter.close();
    }

    private void paintObject(int h, int w, int[][] minVal, int iter) {

        if (minVal[h][w] == 1) {
            minVal[h][w] = iter;
            paintObject(h+1, w, minVal, iter);
            paintObject(h, w+1, minVal, iter);
            paintObject(h-1, w, minVal, iter);
            paintObject(h, w-1, minVal, iter);
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
