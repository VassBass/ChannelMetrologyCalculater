package service.certificate.converter.kt200k.model;

import util.StringHelper;

import java.util.ArrayList;
import java.util.List;

public class XmlElement {
    private final String name;
    private String data;

    private final XmlElement parent;
    private XmlElement previous;
    private XmlElement next;

    private final List<XmlElement> children;

    public XmlElement(String name, XmlElement parent) {
        this.name = name;
        this.parent  = parent;
        this.children = new ArrayList<>();

        if (parent != null) {
            parent.addChild(this);
        }
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getData() {
        if (data == null) {
            StringBuilder dataBuilder = new StringBuilder();
            for (XmlElement child : children) {
                String d = child.getData();
                if (StringHelper.nonEmpty(d)) {
                    dataBuilder.append('&').append(d);
                }
            }
            return dataBuilder.toString();
        } else return data;
    }

    public String getName() {
        return name;
    }

    public XmlElement getPrevious() {
        return previous;
    }

    public void setPrevious(XmlElement previous) {
        this.previous = previous;
    }

    public XmlElement getNext() {
        return next;
    }

    public void setNext(XmlElement next) {
        this.next = next;
    }

    public void addChild(XmlElement xmlElement) {
        children.add(xmlElement);
    }

    public void deleteChild(XmlElement xmlElement) {
        children.remove(xmlElement);
    }

    public void deleteChild(int index) {
        children.remove(index);
    }

    public XmlElement getChild(int index) {
        return children.get(index);
    }

    public XmlElement getParent() {
        return parent;
    }

    public XmlElement getLatsChild() {
        int childrenCount = children.size();
        return childrenCount > 0 ? children.get(--childrenCount) : null;
    }

    public List<XmlElement> getChildren() {
        return children;
    }
}
