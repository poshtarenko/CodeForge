package com.poshtarenko.codeforge.integration.extension;

import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class TestConsoleLoggingExtension implements BeforeTestExecutionCallback, AfterTestExecutionCallback {

    @Override
    public void beforeTestExecution(ExtensionContext extensionContext) {
        System.out.println("--< TEST \"" + extensionContext.getTestMethod().get().getName() + "\" STARTED >--");
    }

    @Override
    public void afterTestExecution(ExtensionContext extensionContext) {
        System.out.println("--< TEST \"" + extensionContext.getTestMethod().get().getName() + "\" FINISHED >--");
    }

}
