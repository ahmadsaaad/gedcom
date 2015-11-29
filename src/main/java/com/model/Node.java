/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.model;

/**
 *
 * @author ahmad
 */
public class Node {

    private int depth;
    private String tag;
    private String value;
    private boolean isParent;
    private boolean tagIsID;

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value .replace("<", "&#60;")
                    .replace(">", "&#62;")
                    .replace("&", "&#38;")
                    .replace("'", "&#39;")
                    .replace("\"", "&#34;");
    }

    public boolean isIsParent() {
        return isParent;
    }

    public void setIsParent(boolean isParent) {
        this.isParent = isParent;
    }

    public boolean isTagIsID() {
        return tagIsID;
    }

    public void setTagIsID(boolean tagIsID) {
        this.tagIsID = tagIsID;
    }

    @Override
    public String toString() {
        String xmlNode = "";
        if (this.isParent == false) {

            xmlNode = "<" + this.tag + ">" + this.value
                   
                    + "</" + this.tag + ">";

        } else {
            if (!this.tagIsID) {
                xmlNode = "<" + this.tag + " value=" + "\"" + this.value + "\">";
            } else {
                xmlNode = "<" + this.value + " id=" + "\"" + this.tag + "\">";
            }
        }
        return xmlNode;
    }

    public String GetCloseTag() {

        if (!this.tagIsID) {
            return "</" + this.tag + ">";
        } else {
            return "</" + this.value + ">";
        }

    }

}
