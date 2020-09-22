/*
 * Copyright 2019-2021 qifu of copyright Chen Xin Nien
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * -----------------------------------------------------------------------
 *
 * author: 	Chen Xin Nien
 * contact: chen.xin.nien@gmail.com
 *
 */
package org.qifu.directive;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import org.qifu.base.model.YesNo;
import org.qifu.ui.PageUiDirectiveUtils;
import org.qifu.ui.impl.Button;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.util.Map;

public class ButtonDirective implements TemplateDirectiveModel {
    private String id = "";
    private String xhrUrl = "";
    private String xhrParameter = "";
    private String formId = "";
    private String onclick = "";
    private String cssClass = "btn btn-primary";
    private String label = "";
    private String errorFunction = "";
    private String loadFunction = "";
    private String disabled = YesNo.NO;
    private String xhrSendNoPleaseWait = YesNo.NO;
    private String selfPleaseWaitShow = YesNo.NO;
    private String bootboxConfirm = YesNo.NO;
    private String bootboxConfirmTitle = "";

    private Button handler() {
        Button button = new Button();
        button.setServletRequestAttributes((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
        button.setId(this.id);
        button.setXhrUrl(this.xhrUrl);
        button.setXhrParameter(this.xhrParameter);
        button.setFormId(this.formId);
        button.setOnclick(this.onclick);
        button.setCssClass(this.cssClass);
        button.setLabel(this.label);
        button.setErrorFunction(this.errorFunction);
        button.setLoadFunction(this.loadFunction);
        button.setDisabled(this.disabled);
        button.setXhrSendNoPleaseWait(this.xhrSendNoPleaseWait);
        button.setSelfPleaseWaitShow(this.selfPleaseWaitShow);
        button.setBootboxConfirm(this.bootboxConfirm);
        button.setBootboxConfirmTitle(this.bootboxConfirmTitle);
        return button;
    }

    @Override
    public void execute(Environment env, Map paramMap, TemplateModel[] models, TemplateDirectiveBody body) throws TemplateException, IOException {
        Button comp = this.handler();
        PageUiDirectiveUtils.write(env, paramMap, comp);
        comp = null;
    }
    
}
