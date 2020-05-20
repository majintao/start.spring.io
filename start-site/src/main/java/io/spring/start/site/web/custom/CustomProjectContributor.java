/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.spring.start.site.web.custom;

import io.spring.initializr.generator.io.template.TemplateRenderer;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.contributor.ProjectContributor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 */
class CustomProjectContributor implements ProjectContributor {

	private final ProjectDescription description;

	private final TemplateRenderer template;

	CustomProjectContributor(ProjectDescription description, TemplateRenderer template) {
		this.description = description;
		this.template = template;
	}

	@Override
	public void contribute(Path projectRoot) throws IOException {
		// 根路径创建文件
		Files.createFile(projectRoot.resolve("custom.txt"));
		// 创建文件夹
		Path migrationDirectory = projectRoot.resolve("src/main/java/com/app");
		Files.createDirectories(migrationDirectory);

		// 渲染模板文件
		Map<String, Object> model = new LinkedHashMap<>();
		model.put("groupId", description.getGroupId());
		String code = this.template.render("custom/app", model);

		Path appFile = migrationDirectory.resolve("app.java");
		Files.write(appFile, code.getBytes("UTF-8"));

		if (this.description instanceof CustomProjectDescription
				&& ((CustomProjectDescription) this.description).isCustomFlag()) {
			Files.createFile(projectRoot.resolve("custom.txt"));
		}
	}

}
