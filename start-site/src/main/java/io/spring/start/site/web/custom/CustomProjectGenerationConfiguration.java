package io.spring.start.site.web.custom;

import io.spring.initializr.generator.project.MutableProjectDescription;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.ProjectDescriptionCustomizer;
import io.spring.initializr.generator.project.ProjectDescriptionDiffFactory;
import io.spring.initializr.metadata.InitializrMetadata;
import io.spring.initializr.metadata.InitializrMetadataProvider;
import io.spring.initializr.web.project.DefaultProjectRequestToDescriptionConverter;
import io.spring.initializr.web.project.ProjectGenerationInvoker;
import io.spring.initializr.web.project.ProjectRequestToDescriptionConverter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

/**
 * 支持自定义请求的配置
 */
@Configuration
public class CustomProjectGenerationConfiguration {

    /**
     * 生成自定义controller
     * @param metadataProvider
     * @param applicationContext
     * @return
     */
    @Bean
    CustomProjectGenerationController customProjectGenerationController(InitializrMetadataProvider metadataProvider,
                                                                        ApplicationContext applicationContext) {
        ProjectGenerationInvoker<CustomProjectRequest> projectGenerationInvoker = new ProjectGenerationInvoker<>(
                applicationContext, new CustomProjectRequestToDescriptionConverter());
        return new CustomProjectGenerationController(metadataProvider, projectGenerationInvoker);
    }

    @Bean
    CustomProjectDescriptionCustomizer customProjectDescriptionCustomizer() {
        return new CustomProjectDescriptionCustomizer();
    }

    @Bean
    CustomProjectDescriptionDiffFactory customProjectDescriptionDiffFactory() {
        return new CustomProjectDescriptionDiffFactory();
    }

}

/**
 * 自定义数据请求转换器
 */
class CustomProjectRequestToDescriptionConverter
        implements ProjectRequestToDescriptionConverter<CustomProjectRequest> {

    @Override
    public ProjectDescription convert(CustomProjectRequest request, InitializrMetadata metadata) {
        CustomProjectDescription description = new CustomProjectDescription();
        new DefaultProjectRequestToDescriptionConverter().convert(request, description, metadata);
        description.setCustomFlag(request.isCustomFlag());
        // Override attributes for test purposes
        description.setPackageName("org.example.custom");
        return description;
    }

}

class CustomProjectDescriptionCustomizer implements ProjectDescriptionCustomizer {

    @Override
    public void customize(MutableProjectDescription description) {
        description.setApplicationName("CustomApp");
    }

}

/**
 * 定义参数转换赋值器
 */
class CustomProjectDescriptionDiffFactory implements ProjectDescriptionDiffFactory {

    @Override
    public CustomProjectDescriptionDiff create(ProjectDescription description) {
        Assert.isInstanceOf(CustomProjectDescription.class, description);
        return new CustomProjectDescriptionDiff((CustomProjectDescription) description);
    }
}
