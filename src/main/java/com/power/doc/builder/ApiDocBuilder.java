/*
 * smart-doc https://github.com/shalousun/smart-doc
 *
 * Copyright (C) 2019-2020 smart-doc
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
package com.power.doc.builder;

import com.power.common.util.DateTimeUtil;
import com.power.doc.model.ApiConfig;
import com.power.doc.model.ApiDoc;
import com.power.doc.template.IDocBuildTemplate;
import com.power.doc.template.SpringBootDocBuildTemplate;
import com.thoughtworks.qdox.JavaProjectBuilder;

import java.util.List;

import static com.power.doc.constants.DocGlobalConstants.*;

/**
 * use to create markdown doc
 * markdown文件构建器
 *
 * @author yu 2019/09/20
 */
public class ApiDocBuilder {


    private static final String API_EXTENSION = "Api.md";

    private static final String DATE_FORMAT = "yyyyMMddHHmm";

    /**
     * @param config ApiConfig
     */
    public static void buildApiDoc(ApiConfig config) {
        JavaProjectBuilder javaProjectBuilder = new JavaProjectBuilder();
        buildApiDoc(config, javaProjectBuilder);
    }

    /**
     * Only for smart-doc-maven-plugin.
     *
     * @param config             ApiConfig
     * @param javaProjectBuilder ProjectDocConfigBuilder
     */
    public static void buildApiDoc(ApiConfig config, JavaProjectBuilder javaProjectBuilder) {
        config.setAdoc(false);
        DocBuilderTemplate builderTemplate = new DocBuilderTemplate();
        // 校验配置
        builderTemplate.checkAndInit(config);
        // 项目配置构建
        ProjectDocConfigBuilder configBuilder = new ProjectDocConfigBuilder(config, javaProjectBuilder);
        //文档构建模版
        IDocBuildTemplate docBuildTemplate = new SpringBootDocBuildTemplate();
        //根据配置信息 和 模版 获取 文档数据
        List<ApiDoc> apiDocList = docBuildTemplate.getApiData(configBuilder);
        if (config.isAllInOne()) {
            String version = config.isCoverOld() ? "" : "-V" + DateTimeUtil.long2Str(System.currentTimeMillis(), DATE_FORMAT);
            builderTemplate.buildAllInOne(apiDocList, config, javaProjectBuilder, ALL_IN_ONE_MD_TPL, "AllInOne" + version + ".md");
        } else {
            //
            builderTemplate.buildApiDoc(apiDocList, config, API_DOC_MD_TPL, API_EXTENSION);
            builderTemplate.buildErrorCodeDoc(config, ERROR_CODE_LIST_MD_TPL, ERROR_CODE_LIST_MD);
        }
    }

    /**
     * Generate a single controller api document
     * 生成单个controller的API文档
     * @param config         (ApiConfig
     * @param controllerName controller name
     */
    public static void buildSingleApiDoc(ApiConfig config, String controllerName) {
        config.setAdoc(false);
        // 用来加载提供的源码路径下源码文件
        JavaProjectBuilder javaProjectBuilder = new JavaProjectBuilder();
        // 项目配置构建
        ProjectDocConfigBuilder configBuilder = new ProjectDocConfigBuilder(config, javaProjectBuilder);
        DocBuilderTemplate builderTemplate = new DocBuilderTemplate();
        //校验配置信息
        builderTemplate.checkAndInit(config);
        //markdown模板文件  ApiDoc.btl  生成文件后缀名
        builderTemplate.buildSingleApi(configBuilder, controllerName, API_DOC_MD_TPL, API_EXTENSION);
    }
}
