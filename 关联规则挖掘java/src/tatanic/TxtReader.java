package tatanic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
//本程序用于读取矩阵型的记录数据，并转换为List<List<String>>格式数据

public class TxtReader {

    public List<List<String>> getRecord() {
        List<List<String>> record = new ArrayList<List<String>>();

        try {
            String encoding = "GBK"; // 字符编码(可解决中文乱码问题 )
            File file = new File("./predata.txt");
            if (file.isFile() && file.exists()) {
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file), encoding);
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTXT = null;
                while ((lineTXT = bufferedReader.readLine()) != null) {//读一行文件
                    String[] lineString = lineTXT.split(" ");
                    List<String> lineList = new ArrayList<String>();
                    for (int i = 0; i < lineString.length; i++) {
                        lineList.add(lineString[i]);
                    }
                    record.add(lineList);
                }
                read.close();
            } else {
                System.out.println("找不到指定的文件！");
            }
        } catch (Exception e) {
            System.out.println("读取文件内容操作出错");
            e.printStackTrace();
        }

//        for(int i=0;i<record.size();i++ ){
//            System.out.println(record.get(i));
//        }
        return record;
    }
}
