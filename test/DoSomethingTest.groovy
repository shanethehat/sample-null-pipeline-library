import com.lesfurets.jenkins.unit.declarative.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

import static com.lesfurets.jenkins.unit.global.lib.ProjectSource.projectSource
import static com.lesfurets.jenkins.unit.global.lib.LibraryConfiguration.library

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DoSomethingTest extends DeclarativePipelineTest {
    @Override
    @BeforeAll
    void setUp() throws Exception {
        scriptRoots += 'vars'

        super.setUp()

        // generic pipeline mocking
        helper.registerAllowedMethod('pipeline', [Closure.class], null)
        helper.registerAllowedMethod('agent', [Closure.class], null)
        helper.registerAllowedMethod('stages', [Closure.class], null)
        helper.registerAllowedMethod('steps', [Closure.class], null)
        binding.setVariable('none', {})

        // library registration
        def library = library()
                .name('my-library')
                .retriever(projectSource())
                .targetPath("<notNeeded>")
                .defaultVersion("<notNeeded>")
                .allowOverride(true)
                .implicit(true)
                .build()
        helper.registerSharedLibrary(library)
    }

    @Test
    void testDoSomething_withString() {
        runScript("job/library/pass/Jenkinsfile")
        assertJobStatusSuccess()
    }

    @Test
    void testDoSomething_withNull() {
        runScript("job/library/fail/Jenkinsfile")
        assertJobStatusSuccess()
    }
}