package com.nannan.batis;

import lombok.Data;

@Data
public class MapperNode {
    String nameSpace;
    String id;
    String parameterType;
    String resultType;
    String sql;
    String parameter;

    @Override
    public String toString(){
        return this.nameSpace+"."+this.id+" : " +this.sql;
    }
}
