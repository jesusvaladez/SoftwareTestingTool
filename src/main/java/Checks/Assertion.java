package Checks;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.visitor.filter.TypeFilter;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;


public class Assertion extends AbstractProcessor<CtMethod<?>> {

    int totalNumOfTests = 0;
    int testsWithAssertion = 0;
    int testsWithoutAssertion = 0;
    int testsWithNullBody = 0;

    private BufferedWriter csvWriter;

    public Assertion() {
        try {
            // Starts up the CSV writer
            csvWriter = new BufferedWriter(new FileWriter("test_analysis.csv"));

            // Include body code
            // "Name of Test, Assertion, Body Code, File Path, Kind of Assertions, Number of Assertions, Method Signature, Annotation"
            csvWriter.write("\"Name of Test\",\"Assertion\",\"Number of Assertions\",\"Kind of Assertions\",\"Body Code\",\"Method Signature\",\"Annotations\",\"File Path\",\"Git Log\"");
            csvWriter.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    private String fixBodyCode(String body, int maxLength) {
        if (body.length() > maxLength) {
            return body.substring(0, maxLength) + "...";
        }
        return body;
    }
     */

    @Override
    public void process(CtMethod<?> method) {

        // Check for tests in general -> Has annotation of @Test or Test
        boolean isTestCase = method.getAnnotations().stream()
                .anyMatch(annot -> annot.getAnnotationType().getSimpleName().equals("Test")
                        || method.getSimpleName().startsWith("test") ||
                        method.getSimpleName().endsWith("Test"));

        /*
        // If there is no test case then state there isn't one
        if (!isTestCase) {
            System.out.println("Not a proper test");
        }
         */


        // If there is a test case then do the following
        if (isTestCase) {
            // Tally amount of tests
            totalNumOfTests++;

            // Important values
            String testName = method.getSimpleName();
            String assertionState = "FALSE";
            int numberOfAssertions = 0;
            String kindOfAssertion = "None";
            String bodyCode = "No Body";
            String filePath = "Empty";
            String methodSignature = "None";
            String annotations = "None";
            String gitLog = "N/A";

            // Get method signature
            methodSignature = method.getSignature();

            // Get annotations
            annotations = method.getAnnotations().stream()
                    .map(annot -> annot.getAnnotationType().getSimpleName())
                    .reduce((a, b) -> a + "\n" + b)
                    .orElse("None");

            // Gets file path of Test
            if (method.getPosition() != null && method.getPosition().getFile() != null) {
                filePath = method.getPosition().getFile().getAbsolutePath();
                int startOfPath = filePath.indexOf("ambari");
                if (startOfPath != -1) {
                    filePath = filePath.substring(startOfPath).replace("\\", "/");
                }
                gitLog = getGitLogForFile(filePath);
            }

            // Logic of body
            if (method.getBody() != null) {
                // Gets body code
                bodyCode = method.getBody().toString();

                // Gets the number of assertions and the kind
                List<CtInvocation> assertionInvocations = method.getBody()
                        .getElements(new TypeFilter<>(CtInvocation.class)).stream()
                        .filter(invocation -> invocation.getExecutable()
                                .getSimpleName().startsWith("assert"))
                        .toList();
                numberOfAssertions = assertionInvocations.size();
                kindOfAssertion = assertionInvocations.stream().map(invocation -> invocation.getExecutable().getSimpleName())
                        .distinct()
                        .reduce((a, b) -> a + "\n" + b)
                        .orElse("None");

                // Check if the test contains assertions within
                boolean hasAssertions = method.getBody()
                        .getElements(new TypeFilter<>(CtInvocation.class)).stream()
                        .anyMatch(invocation -> invocation.getExecutable()
                                .getSimpleName().startsWith("assert"));

                if (hasAssertions) {
                    testsWithAssertion++;
                    assertionState = "TRUE";
                    numberOfAssertions++;
                    //System.out.println(testName + ": Is a test case and contains assertion");
                } else {
                    testsWithoutAssertion++;
                    assertionState = "FALSE";
                    //System.out.println(testName + ": Is a test case, but does NOT contain assertion");
                }
            } else {
                testsWithNullBody++;
            }

            // Use "git log --follow <filepath> to get commits of the test cases file
            // These commitIds will then be used to get refactorings of the tests using refactoringminer API


            try {
                csvWriter.write(String.format("\"%s\",\"%s\",\"%d\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"",
                        testName, assertionState, numberOfAssertions, kindOfAssertion, bodyCode.replace("\"", "\"\""), methodSignature, annotations, filePath,
                        gitLog));
                csvWriter.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void processingDone() {
        // Purpose: Print test totals
        try {
            System.out.println("Total Number of Tests: " + totalNumOfTests);
            System.out.println("Tests with Assertion: " + testsWithAssertion);
            System.out.println("Tests without Assertion: " + testsWithoutAssertion);
            System.out.println("Tests with Null Body: " + testsWithNullBody);

            if (csvWriter != null) {
                csvWriter.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getGitLogForFile(String filePath) {
        String gitBaseDir = System.getProperty("user.home") + "/Desktop/SoftwareTestingTool/src/main/resources/ambari";

        // Full path to git executable (adjust for your system)
        // Update this as needed/ Fix
        String gitExecutable = "/opt/homebrew/bin/git";

        try {
            int startOfPath = filePath.indexOf("ambari");
            if (startOfPath == -1) {
                System.err.println("Error: File path does not contain 'ambari'");
                return "Error: File path does not contain 'ambari'";
            }

            String relativePath = filePath.substring(startOfPath + "ambari".length());
            String adjustedPath = relativePath.replace("\\", "/");
            if (adjustedPath.startsWith("/")) {
                adjustedPath = adjustedPath.substring(1); // Remove leading slash
            }

            // Executes the command
            // Fix this
            ProcessBuilder processBuilder = new ProcessBuilder(gitExecutable, "log", "--pretty=format:%H", "--follow", "--", adjustedPath);
            processBuilder.directory(new File(gitBaseDir));
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String commitIDs = reader.lines().collect(Collectors.joining("\n"));
                if (commitIDs.isEmpty()) {
                    System.err.println("Git log returned no results for: " + adjustedPath);
                    return "No commit IDs found";
                }
                return commitIDs;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Error retrieving commit ID";
        }
    }
}

