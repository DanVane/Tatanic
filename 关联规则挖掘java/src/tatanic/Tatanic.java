/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tatanic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author weangdan
 */
public class Tatanic {

    /**
     * @param args the command line arguments
     *
     * 为了减少编码，先用libreoffice Calc将PassengerId,Name,Cabin,Ticket,Fare删掉
     */
    public static void main(String[] args) {
        // TODO code application logic here

        try {
            BufferedReader reader = new BufferedReader(new FileReader("./train.csv"));
            reader.readLine();//第一行信息，为标题信息，不用,如果需要，注释掉 

            File csv = new File("./predata.txt"); // CSV数据文件 

            BufferedWriter bw = new BufferedWriter(new FileWriter(csv, true)); // 附加 
            String line = null;
            double age = 0;
            while ((line = reader.readLine()) != null) {
                if (line.contains(",,")) {
                    continue;//去除不完全项
                }
                String item[] = line.split(",");//CSV格式文件为逗号分隔符文件，这里根据逗号切分                 
                if(item.length!=7) continue;
                if (item[0].equals("0")) {
                    item[0] = "Dead";
                } else {
                    item[0] = "Survived";
                }
                item[1] += "class";
                age = Double.parseDouble(item[3]);
                if (age <= 12) {
                    item[3] = "child";
                } else if (age <= 20) {
                    item[3] = "teenager";
                } else if (age <= 40) {
                    item[3] = "youth";
                } else if (age <= 60) {
                    item[3] = "middle-aged";
                } else {
                    item[3] = "elderly";
                }

                if (item[4].equals("0")) {
                    item[4] = "noSibsp";
                } else {
                    item[4] = "yesSibsp";
                }

                if (item[5].equals("0")) {
                    item[5] = "noParch";
                } else {
                    item[5] = "yesParch";
                }

                bw.write(item[0] + " " + item[1] + " " + item[2] + " " + item[3] + " " + item[4] + " " + item[5]+" "+item[6]);
                bw.newLine();            // 添加新的数据行 
            }
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
