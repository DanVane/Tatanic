/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tatanic;

/**
 *
 * @author Administrator
 */
public class Item implements Comparable<Item> {

    private String rule;
    private Double support;
    private Double confidence;
    private Double lift;

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public Double getSupport() {
        return support;
    }

    public void setSupport(Double support) {
        this.support = support;
    }

    public Double getConfidence() {
        return confidence;
    }

    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }

    public Double getLift() {
        return lift;
    }

    public void setLift(Double lift) {
        this.lift = lift;
    }

    @Override
    public int compareTo(Item o) {
        //����֧�ֶ�����
        // return -(this.getSupport().compareTo(o.getSupport())); 
        //�������Ŷ�����
        return -(this.getConfidence().compareTo(o.getConfidence()));
        //����liftֵ����      
        // return -(this.getLift().compareTo(o.getLift()));
    }

}
