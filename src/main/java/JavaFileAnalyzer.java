import Checks.Assertion;
import spoon.Launcher;
import spoon.processing.AbstractProcessor;
import spoon.processing.ProcessingManager;
import spoon.reflect.factory.Factory;
import spoon.support.QueueProcessingManager;

import java.nio.file.Paths;


public class JavaFileAnalyzer {
    //The Spoon Launcher (JavaDoc) is used to create the AST model of a project
    final static Launcher launcher = new Launcher();

    public static void main(String[] args) {
        //Add java file to be parsed in spoon
        launcher.addInputResource(Paths.get(System.getProperty("user.dir"),
                "src", "main", "resources", "ambari").toString());

        // Parse the java file into AST
        launcher.getEnvironment().setCommentEnabled(false);
        launcher.getEnvironment().setIgnoreDuplicateDeclarations(true);
        launcher.getEnvironment().setCopyResources(false);
        launcher.getEnvironment().setIgnoreSyntaxErrors(true);
        launcher.run();


        // Create a rule
        final Assertion rule1 = new Assertion();

        // run the rules
        addRuleToAnalyze(rule1);

    }
    /**
     *
     * @param rule: add your rule
     */
    public static void addRuleToAnalyze(AbstractProcessor<?> rule) {
        final Factory factory = launcher.getFactory();
        final ProcessingManager processingManager = new QueueProcessingManager(factory);
        processingManager.addProcessor(rule);
        processingManager.process(factory.Class().getAll());
    }
}
