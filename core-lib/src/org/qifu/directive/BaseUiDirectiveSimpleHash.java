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

import freemarker.template.ObjectWrapper;
import freemarker.template.SimpleHash;

public class BaseUiDirectiveSimpleHash extends SimpleHash {
	private static final long serialVersionUID = -5453087570121237117L;
	
	public BaseUiDirectiveSimpleHash(ObjectWrapper wrapper) {
		super(wrapper);
		put("button", new ButtonDirective());
		put("checkbox", new CheckBoxDirective());
		put("grid", new GridDirective());
		put("out", new OutDirective());
		put("select", new SelectDirective());
		put("textarea", new TextAreaDirective());
		put("textbox", new TextBoxDirective());
		put("toolBar", new ToolBarDirective());
		put("if", new IfDirective());
		put("elseif", new ElseifDirective());
		put("else", new ElseDirective());
	}
	
}
