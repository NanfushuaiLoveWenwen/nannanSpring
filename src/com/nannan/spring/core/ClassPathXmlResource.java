package com.nannan.spring.core;

import com.nannan.spring.beans.BeansException;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.net.URL;
import java.util.Iterator;

//从xml文件加载bean信息
public class ClassPathXmlResource implements Resource<Element> {
    private Document document;
    private Element rootElement;
    Iterator<Element> elementIterator;

    public ClassPathXmlResource(String fileName) {
        //获取同包路径下的资源的完整的xml路径·
        try {
            URL xmlPath = this.getClass().getClassLoader().getResource(fileName);
            SAXReader saxReader = new SAXReader();
            this.document = saxReader.read(xmlPath).getDocument();
            this.rootElement = document.getRootElement();
            this.elementIterator = this.rootElement.elementIterator();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean hasNext() {
        return this.elementIterator.hasNext();
    }

    @Override
    public Element next() {
        return this.elementIterator.next();
    }
}
