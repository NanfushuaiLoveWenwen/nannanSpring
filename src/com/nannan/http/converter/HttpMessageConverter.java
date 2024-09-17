package com.nannan.http.converter;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface HttpMessageConverter {
    //对controller层返回给前端的字符流数据进行格式转换
    void write(Object object, HttpServletResponse response) throws IOException;
}
