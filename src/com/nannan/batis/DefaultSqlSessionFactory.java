package com.nannan.batis;

import com.nannan.beans.factory.annotation.Autowired;
import com.nannan.jdbc.core.JdbcTemplate;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DefaultSqlSessionFactory implements SqlSessionFactory {
    @Autowired
    JdbcTemplate jdbcTemplate;

    String mapperLocations;

    @Getter
    Map<String, MapperNode> mapperNodeMap = new HashMap<>();
    public String getMapperLocations() {
        return mapperLocations;
    }
    public void setMapperLocations(String mapperLocations) {
        this.mapperLocations = mapperLocations;
    }

    public void init(){

    }

    private void scanLocation(String location){
        String locationPath = this.getClass().getClassLoader().getResource("").getPath() + location;

        System.out.println("mapper location: " + locationPath);
        File dir = new File(locationPath);

        for (File file : dir.listFiles()) {
            if(file.isDirectory()){
                scanLocation(location + "/" + file.getName());
            }else{
                buildMapperNodes(locationPath+"/"+file.getName());
            }
        }
    }

    private Map<String, MapperNode> buildMapperNodes(String filePath){
        System.out.println(filePath);
        SAXReader saxReader = new SAXReader();
        URL xmlPath = this.getClass().getClassLoader().getResource(filePath);

        try{
            Document document = saxReader.read(xmlPath);
            Element rootElement = document.getRootElement();

            String nameSpace = rootElement.attributeValue("namespace");

            Iterator<Element> iterator = rootElement.elementIterator();
            while(iterator.hasNext()){
                Element node = iterator.next();
                String id = node.attributeValue("id");
                String parameterType = node.attributeValue("parameterType");
                String resultType = node.attributeValue("resultType");
                String sql = node.getText();

                MapperNode selectNote = new MapperNode();
                selectNote.setNameSpace(nameSpace);
                selectNote.setId(id);
                selectNote.setParameterType(parameterType);
                selectNote.setResultType(resultType);
                selectNote.setSql(sql);
                selectNote.setParameter("");

                this.mapperNodeMap.put(nameSpace+"."+id, selectNote);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return this.mapperNodeMap;
    }
    @Override
    public SqlSession openSqlSession() {
        return null;
    }

    @Override
    public MapperNode getMapperNode(String name) {
        return this.mapperNodeMap.get(name);
    }
}
