package tatanic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
//���������ڶ�ȡ�����͵ļ�¼���ݣ���ת��ΪList<List<String>>��ʽ����

public class TxtReader {

    public List<List<String>> getRecord() {
        List<List<String>> record = new ArrayList<List<String>>();

        try {
            String encoding = "GBK"; // �ַ�����(�ɽ�������������� )
            File file = new File("./predata.txt");
            if (file.isFile() && file.exists()) {
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file), encoding);
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTXT = null;
                while ((lineTXT = bufferedReader.readLine()) != null) {//��һ���ļ�
                    String[] lineString = lineTXT.split(" ");
                    List<String> lineList = new ArrayList<String>();
                    for (int i = 0; i < lineString.length; i++) {
                        lineList.add(lineString[i]);
                    }
                    record.add(lineList);
                }
                read.close();
            } else {
                System.out.println("�Ҳ���ָ�����ļ���");
            }
        } catch (Exception e) {
            System.out.println("��ȡ�ļ����ݲ�������");
            e.printStackTrace();
        }

//        for(int i=0;i<record.size();i++ ){
//            System.out.println(record.get(i));
//        }
        return record;
    }
}
