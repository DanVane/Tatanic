package tatanic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ����������Ƶ�������ھ� ������List<List<String>>���͵�record��������ʽ�����ݶ����ڴ棻
 *
 * ���������k-1��ѡ�����ɱ�ѡ�������ݿ��¼record�������֧�ֶȵ�k-1�����ϣ�������֧�ֶȼ���������������Ŷȵļ��ϣ�
 * ���������Ŷȵļ���Ϊ�գ�����ֹͣ�� ��������������Ŷȵļ��ϣ��Լ���Ӧ��֧�ֶȺ����Ŷȣ���������֧�ֶȵ�k-1���������k����ѡ����������һ��ѭ����
 * ֱ��������������ȫ��Ƶ����
 */
public class Aprori {

    static boolean endTag = false;
    static Map<Integer, Integer> dCountMap = new HashMap<Integer, Integer>(); // k-1Ƶ�����ļ�����
    static Map<Integer, Integer> dkCountMap = new HashMap<Integer, Integer>();// kƵ�����ļ�����
    static List<List<String>> record = new ArrayList<List<String>>();// ���ݼ�¼��
    final static double MIN_SUPPORT = 0.2;// ��С֧�ֶ�
    final static double MIN_CONF = 0.5;// ��С���Ŷ�
    static double Dead_Support;
    static int lable = 1;// �������ʱ��һ����ǣ���¼��ǰ�ڴ�ӡ�ڼ���������
    static List<Double> confCount = new ArrayList<Double>();// ���Ŷȼ�¼��
    static List<List<String>> confItemset = new ArrayList<List<String>>();// ����֧�ֶȵļ���
    static List<List<String>> deadItemset = new ArrayList<List<String>>();// ����֧�ֶȵ�Dead����

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        record = getRecord();// ��ȡԭʼ���ݼ�¼
        List<List<String>> cItemset = findFirstCandidate();// ��ȡ��һ�εı�ѡ��
        List<List<String>> lItemset = getSupportedItemset(cItemset);// ��ȡ��ѡ��cItemset����֧�ֵļ���
//        for(int i=0;i<lItemset.size();i++){
//            System.out.println(lItemset.get(i));
//        }
        while (endTag != true) {// ֻҪ�ܼ����ھ�
            List<List<String>> ckItemset = getNextCandidate(lItemset);// ��ȡ����һ�εı�ѡ��
            List<List<String>> lkItemset = getSupportedItemset(ckItemset);// ��ȡ��ѡ��cItemset����֧�ֵļ���
            getConfidencedItemset(lkItemset, lItemset, dkCountMap, dCountMap);// ��ȡ��ѡ��cItemset�������Ŷȵļ���
            if (confItemset.size() != 0)// �������Ŷȵļ��ϲ�Ϊ��
            {
                printConfItemset(confItemset);// ��ӡ�������Ŷȵļ���
            }
            confItemset.clear();// ������Ŷȵļ���
            cItemset = ckItemset;// �������ݣ�Ϊ�´�ѭ������׼��
            lItemset = lkItemset;
            dCountMap.clear();
            dCountMap.putAll(dkCountMap);
        }
        liftDeadItemset();
        
    }

    /**
     * @param confItemset2 �������������Ƶ����
     */
    static int num=0;
    private static void printConfItemset(List<List<String>> confItemset2) {
        System.out.print("*********Ƶ��ģʽ�ھ���***********\n");
        System.out.print("������Ŀ��"+confItemset2.size()+"\n");
        String temp;
        for (int i = 0; i < confItemset2.size(); i++) {
            int j = 0;
            temp=confItemset2.get(i).get(confItemset2.get(i).size() - 3);
        //     System.out.println(temp);
        //    if(!temp.contains("Survived")) continue;
            if(!temp.contains("Dead")) continue;
            num++;
            for (j = 0; j < confItemset2.get(i).size() - 3; j++) {
                System.out.print(confItemset2.get(i).get(j) + " ");
            }
            System.out.print("-->");
            System.out.print(confItemset2.get(i).get(j++));
            System.out.print("���֧�ֶȣ�" + confItemset2.get(i).get(j++));
            System.out.print("���Ŷȣ�" + confItemset2.get(i).get(j++) + "\n");
       //     System.out.println(confItemset2.get(i));
            deadItemset.add(confItemset2.get(i));
        }
    //    System.out.print("������Ŀ��"+deadItemset.size()+"\n");
        System.out.print("������Ŀ��"+num+"\n");
    }

      private static void printlnLiftItemset(List<List<String>> confItemset2) {
        System.out.print("*********�����������۽��***********\n");
        System.out.print("����������Ŀ��"+confItemset2.size()+"\n");
        String temp;
        for (int i = 0; i < confItemset2.size(); i++) {
            int j = 0;
            temp=confItemset2.get(i).get(confItemset2.get(i).size() - 4);
            for (j = 0; j < confItemset2.get(i).size() - 4; j++) {
                System.out.print(confItemset2.get(i).get(j) + " ");
            }
            System.out.print("-->");
            System.out.print(confItemset2.get(i).get(j++));
            System.out.print("���֧�ֶȣ�" + confItemset2.get(i).get(j++));
            System.out.print("���Ŷȣ�" + confItemset2.get(i).get(j++));
            System.out.print("lift��" + confItemset2.get(i).get(j++) + "\n");
        }
    }
    
    /**
     * @param lkItemset
     * @param lItemset
     * @param dkCountMap2
     * @param dCountMap2 ����lkItemset��lItemset��dkCountMap2��dCountMap2����������Ŷȵļ���
     */
    private static List<List<String>> getConfidencedItemset(
            List<List<String>> lkItemset, List<List<String>> lItemset,
            Map<Integer, Integer> dkCountMap2, Map<Integer, Integer> dCountMap2) {
        for (int i = 0; i < lkItemset.size(); i++) {
            getConfItem(lkItemset.get(i), lItemset, dkCountMap2.get(i),
                    dCountMap2);
        }
        return null;
    }

    /**
     * @param list
     * @param lItemset
     * @param count
     * @param dCountMap2 ���鼯��list�Ƿ�����������Ŷ�Ҫ�� ����������ȫ�ֱ���confItemset���list
     * �粻�����򷵻�null
     */
    private static List<String> getConfItem(List<String> list,
            List<List<String>> lItemset, Integer count,
            Map<Integer, Integer> dCountMap2) {
        for (int i = 0; i < list.size(); i++) {
            List<String> testList = new ArrayList<String>();
            for (int j = 0; j < list.size(); j++) {
                if (i != j) {
                    testList.add(list.get(j));
                }
            }
            int index = findConf(testList, lItemset);//����testList�е�������lItemset��λ��
            Double conf = count * 1.0 / dCountMap2.get(index);
            if (conf > MIN_CONF) {//�������Ŷ�Ҫ��
                testList.add(list.get(i));
                Double relativeSupport = count * 1.0 / (record.size() - 1);
                testList.add(relativeSupport.toString());
                testList.add(conf.toString());
                confItemset.add(testList);//��ӵ��������Ŷȵļ�����
            }
        }
        return null;
    }

    
    
    /**
     * @param testList
     * @param lItemset ����testList�е�������lItemset��λ��
     */
    private static int findConf(List<String> testList,
            List<List<String>> lItemset) {
        for (int i = 0; i < lItemset.size(); i++) {
            boolean notHaveTag = false;
            for (int j = 0; j < testList.size(); j++) {
                if (haveThisItem(testList.get(j), lItemset.get(i)) == false) {
                    notHaveTag = true;
                    break;
                }
            }
            if (notHaveTag == false) {
                return i;
            }
        }
        return -1;
    }

    /**
     * @param string
     * @param list ����list���Ƿ����string
     * @return boolean
     */
    private static boolean haveThisItem(String string, List<String> list) {
        for (int i = 0; i < list.size(); i++) {
            if (string.equals(list.get(i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * ��ȡ���ݿ��¼
     */
    private static List<List<String>> getRecord() {
        TxtReader readRecord = new TxtReader();
        return readRecord.getRecord();
    }

    /**
     * @param cItemset ���cItemset���������֧�ֶȼ���
     */
    private static List<List<String>> getSupportedItemset(
            List<List<String>> cItemset) {
        // TODO Auto-generated method stub
        boolean end = true;
        List<List<String>> supportedItemset = new ArrayList<List<String>>();
        int k = 0;
        for (int i = 0; i < cItemset.size(); i++) {
            int count = countFrequent(cItemset.get(i));//ͳ�Ƽ�¼��
            if (count >= MIN_SUPPORT * (record.size() - 1)) {// countֵ����֧�ֶ����¼���ĳ˻���������֧�ֶ�Ҫ��
                if (cItemset.get(0).size() == 1) {
                    dCountMap.put(k++, count);
                } else {
                    dkCountMap.put(k++, count);
                }
                supportedItemset.add(cItemset.get(i));
                System.out.println(cItemset.get(i)+"    "+((double)count/(record.size())));
                if(cItemset.get(i).contains("Dead")&&cItemset.get(i).size()==1){
                    Dead_Support = (double)count/(record.size());
            //        System.out.println(Dead_Support);
                }
                end = false;
            }
        }
        endTag = end;
 //       System.out.println(supportedItemset.size());
        return supportedItemset;
    }

    /**
     * @param list ͳ�����ݿ��¼record�г���list�еļ��ϵĸ���
     */
    private static int countFrequent(List<String> list) {
        int count = 0;
        for (int i = 0; i < record.size(); i++) {
            boolean notHavaThisList = false;
            for (int k = 0; k < list.size(); k++) {
                boolean thisRecordHave = false;
                for (int j = 0; j < record.get(i).size(); j++) {
                    if (list.get(k).equals(record.get(i).get(j))) {
                        thisRecordHave = true;
                    }
                }
                if (!thisRecordHave) {// ɨ��һ���¼���һ�У�����list.get(i)���ڼ�¼��ĵ�j���У���list��������j����
                    notHavaThisList = true;
                    break;
                }
            }
            if (notHavaThisList == false) {
                count++;
            }
        }
        return count;
    }

    /**
     * @param cItemset
     * @return nextItemset
     * ����cItemset�����һ���ı�ѡ�����飬����ı�ѡ�������е�ÿ�����ϵ�Ԫ�صĸ�����cItemset�еļ��ϵ�Ԫ�ش�1
     */
    private static List<List<String>> getNextCandidate(
            List<List<String>> cItemset) {
        List<List<String>> nextItemset = new ArrayList<List<String>>();
        for (int i = 0; i < cItemset.size(); i++) {
            List<String> tempList = new ArrayList<String>();
            for (int k = 0; k < cItemset.get(i).size(); k++) {
                tempList.add(cItemset.get(i).get(k));
            }
            for (int h = i + 1; h < cItemset.size(); h++) {
                for (int j = 0; j < cItemset.get(h).size(); j++) {
                    tempList.add(cItemset.get(h).get(j));
                    if (isSubsetInC(tempList, cItemset)) {// tempList���Ӽ�ȫ����cItemset��
                        List<String> copyValueHelpList = new ArrayList<String>();
                        for (int p = 0; p < tempList.size(); p++) {
                            copyValueHelpList.add(tempList.get(p));
                        }
                        if (isHave(copyValueHelpList, nextItemset))//nextItemset��û��copyValueHelpList�������
                        {
                            nextItemset.add(copyValueHelpList);
                        }
                    }
                    tempList.remove(tempList.size() - 1);
                }
            }
        }

        return nextItemset;
    }

    /**
     * @param copyValueHelpList
     * @param nextItemset
     * @return boolean ����nextItemset���Ƿ����copyValueHelpList
     */
    private static boolean isHave(List<String> copyValueHelpList,
            List<List<String>> nextItemset) {
        for (int i = 0; i < nextItemset.size(); i++) {
            if (copyValueHelpList.equals(nextItemset.get(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param tempList
     * @param cItemset
     * @return ���� tempList�ǲ���cItemset���Ӽ�
     */
    private static boolean isSubsetInC(List<String> tempList,
            List<List<String>> cItemset) {
        boolean haveTag = false;
        for (int i = 0; i < tempList.size(); i++) {// k����tempList���Ӽ��Ƿ���k-1��Ƶ������
            List<String> testList = new ArrayList<String>();
            for (int j = 0; j < tempList.size(); j++) {
                if (i != j) {
                    testList.add(tempList.get(j));
                }
            }
            for (int k = 0; k < cItemset.size(); k++) {
                if (testList.equals(cItemset.get(k))) {// �Ӽ�������k-1Ƶ������
                    haveTag = true;
                    break;
                }
            }
            if (haveTag == false)// ����һ���Ӽ�����k-1Ƶ������
            {
                return false;
            }
        }

        return haveTag;
    }

    /**
     * �������ݿ��¼�����һ����ѡ��
     */
    private static List<List<String>> findFirstCandidate() {
        List<List<String>> tableList = new ArrayList<List<String>>();
        List<String> lineList = new ArrayList<String>();

        int size = 0;
        for (int i = 0; i < record.size(); i++) {
            for (int j = 0; j < record.get(i).size(); j++) {
                if (lineList.isEmpty()) {
                    lineList.add(record.get(i).get(j));
                } else {
                    boolean haveThisItem = false;
                    size = lineList.size();
                    for (int k = 0; k < size; k++) {
                        if (lineList.get(k).equals(record.get(i).get(j))) {
                            haveThisItem = true;
                            break;
                        }
                    }
                    if (haveThisItem == false) {
                        lineList.add(record.get(i).get(j));
                    }
                }
            }
        }
        for (int i = 0; i < lineList.size(); i++) {
            List<String> helpList = new ArrayList<String>();
            helpList.add(lineList.get(i));
            tableList.add(helpList);
        }
        return tableList;
    }
    
    private static void liftDeadItemset(){
        List<List<String>> liftList = new ArrayList<List<String>>();
        List<String> liftString = new ArrayList<String>();
        double lift=0.0;
        int size=0;
        for(int i=0;i<deadItemset.size();i++){
            liftString = deadItemset.get(i);
            size = liftString.size();
            lift = Double.parseDouble(liftString.get(size-1))/Dead_Support;
            liftString.add(""+lift);
            liftList.add(liftString);
        }
        printlnLiftItemset(liftList);
        sortItemset(liftList);
    }
    
    private static void sortItemset(List<List<String>> list){
        List<Item> itemset = new ArrayList<Item>();
        String str = "";
        for(List<String> temp:list){
            str = "";
            int j=0;
            for(;j<temp.size()-4;j++){
                str +=" "+temp.get(j);
            }
            Item item = new Item();
            str+="==>";
            str+=temp.get(j++);
            item.setRule(str);
            item.setSupport(Double.parseDouble(temp.get(j++)));
            item.setConfidence(Double.parseDouble(temp.get(j++)));
            item.setLift(Double.parseDouble(temp.get(j++)));
            itemset.add(item);
        }
        Collections.sort(itemset);
        printSortItemset(itemset,10);
    }
    
  private static void printSortItemset(List<Item> itemset,int num) {
        System.out.print("*********���������������۽��***********\n");
        String temp;
        for (int i = 0; i < num; i++) {
           System.out.print(itemset.get(i).getRule());
           System.out.print("���֧�ֶȣ�" + itemset.get(i).getSupport());
           System.out.print("���Ŷȣ�" + itemset.get(i).getConfidence());
           System.out.print("lift��" +itemset.get(i).getLift() +"\n");
        }
    }
}
