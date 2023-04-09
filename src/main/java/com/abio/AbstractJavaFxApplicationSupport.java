package com.abio;

import javafx.application.Application;
import javafx.application.Platform;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public abstract class AbstractJavaFxApplicationSupport extends Application {

    protected static String[] savedArgs;
    protected ConfigurableApplicationContext applicationContext;

    @Override
    public void init() {
        this.applicationContext = new SpringApplicationBuilder(AbioApplication.class).run(savedArgs);
        this.applicationContext.getAutowireCapableBeanFactory().autowireBean(this);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        applicationContext.close();
        Platform.exit();
    }

    protected static void launchApp(String[] args) {
        AbstractJavaFxApplicationSupport.savedArgs = args;
        launch(AbioApplication.class, args);
    }

}
