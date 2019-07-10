package com.springboot.utils.scriptEngin;


import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.script.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: chenmingjian
 * @date: 19-6-21 14:05
 */
public class Test {

    public static void main(String[] args) throws ScriptException, IOException {

        // 获取工厂
        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();

        // 获取脚本
        ScriptEngine engine = scriptEngineManager.getEngineByName("JavaScript");

        // 内容
        Map<String, Object> contextData = new HashMap<>();
        contextData.put("num",2);
        contextData.put("a",1);
        contextData.put("b",2);

        String json = "{\"school\":{\"num\":2}}";
        JSONObject jsonObject = JSONObject.parseObject(json);

        engine.setBindings(new SimpleBindings(contextData),ScriptContext.ENGINE_SCOPE);

        Resource fileResource = new ClassPathResource("jsfun/Math.js");
        String fileContent = IOUtils.toString(fileResource.getInputStream(), "UTF-8");
        engine.eval(fileContent);

//        System.out.println(engine.eval("add(multiplication(school.num,school.num), school.num)"));
        Boolean value = (Boolean) engine.eval("isTrue(num,b)");
        System.out.println(value);


    }

}
