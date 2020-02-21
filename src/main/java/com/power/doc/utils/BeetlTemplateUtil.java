/*
 * smart-doc
 *
 * Copyright (C) 2016-2020 smart-doc
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.power.doc.utils;

import com.power.common.util.FileUtil;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.ClasspathResourceLoader;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Beetl template handle util
 *
 * <P>
 *     Beetl 新一代Java模板引擎典范
 *     Beetl是Bee Template Language的缩写，它绝不是简单的另外一种模板引擎，而是新一代的模板引擎，
 *     它功能强大，性能良好，超过当前流行的模板引擎。而且还易学易用。
 *     http://ibeetl.com/
 * </P>
 *
 * @author sunyu on 2016/12/6.
 */
public class BeetlTemplateUtil {


    /**
     * Get Beetl template by file name
     *
     * @param templateName template name
     * @return Beetl Template Object
     */
    public static Template getByName(String templateName) {
        try {
            ClasspathResourceLoader resourceLoader = new ClasspathResourceLoader("/template/");
            Configuration cfg = Configuration.defaultConfiguration();
            GroupTemplate gt = new GroupTemplate(resourceLoader, cfg);
            return gt.getTemplate(templateName);
        } catch (IOException e) {
            throw new RuntimeException("Can't get Beetl template.");
        }
    }

    /**
     * Batch bind binding value to Beetl templates and return all file rendered,
     * Map key is file name,value is file content
     *
     * @param path   path
     * @param params params
     * @return map
     */
    public static Map<String, String> getTemplatesRendered(String path, Map<String, Object> params) {
        Map<String, String> templateMap = new HashMap<>();
        File[] files = FileUtil.getResourceFolderFiles(path);
        GroupTemplate gt = getGroupTemplate(path);
        for (File f : files) {
            if (f.isFile()) {
                String fileName = f.getName();
                Template tp = gt.getTemplate(fileName);
                if (null != params) {
                    tp.binding(params);
                }
                templateMap.put(fileName, tp.render());
            }
        }
        return templateMap;
    }

    /**
     * @param path
     * @return
     */
    private static GroupTemplate getGroupTemplate(String path) {
        try {
            ClasspathResourceLoader resourceLoader = new ClasspathResourceLoader("/" + path + "/");
            Configuration cfg = Configuration.defaultConfiguration();
            GroupTemplate gt = new GroupTemplate(resourceLoader, cfg);
            return gt;
        } catch (IOException e) {
            throw new RuntimeException("Can't get Beetl template.");
        }
    }
}
