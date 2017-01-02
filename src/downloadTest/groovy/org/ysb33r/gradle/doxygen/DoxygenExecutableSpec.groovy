//
// ============================================================================
// (C) Copyright Schalk W. Cronje 2013-2017
//
// This software is licensed under the Apache License 2.0
// See http://www.apache.org/licenses/LICENSE-2.0 for license details
//
// Unless required by applicable law or agreed to in writing, software distributed under the License is
// distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and limitations under the License.
//
// ============================================================================
//

package org.ysb33r.gradle.doxygen

import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.testfixtures.ProjectBuilder
import org.ysb33r.gradle.doxygen.helper.DownloadTestSpecification
import spock.lang.IgnoreIf
import spock.lang.Specification
import spock.lang.Stepwise


/**
 * @author Schalk W. Cronjé
 */
@Stepwise
class DoxygenExecutableSpec extends DownloadTestSpecification {

    static final File TESTFSREADROOT  = new File( System.getProperty('TESTFSREADROOT')  ?: 'src/downloadTest/resources' )
    static final File TESTFSWRITEROOT = new File( System.getProperty('TESTFSWRITEROOT') ?: 'build/tmp/downloadTest', 'DoxygenExecutableSpec' )
    static final File DOXY_TEMPLATE   = new File ( System.getProperty('DOXY_TEMPLATE')  ?: 'src/main/resources/doxyfile-template.dox')

    Project project = ProjectBuilder.builder().withName('DoxygenExecutableSpec').build()
    Task dox = project.task('doxygen', type: Doxygen )
    File downloadRoot = new File(project.buildDir,'download')

    void setup() {
        if(TESTFSWRITEROOT.exists()) {
            TESTFSWRITEROOT.deleteDir()
        }

        TESTFSWRITEROOT.mkdirs()

        dox.configure {
            executables {
                doxygen version : DOX_VERSION, baseURI : DOXYGEN_CACHE_DIR.toURI(), downloadRoot : downloadRoot
            }
        }
    }

    @IgnoreIf({DownloadTestSpecification.SKIP_TESTS})
    def "Must be able to set doxygen executable using a version"() {
        given:
        dox.configure {
            executables {
                mscgen  '/path/to/mscgen'
            }
        }

        when:
        String doxPath = dox.executables.doxygen.call()

        then:
        doxPath.contains(downloadRoot.absolutePath)
        doxPath.contains(DOX_VERSION)
        dox.executables.mscgen  == '/path/to/mscgen'

    }

    @IgnoreIf({DownloadTestSpecification.SKIP_TESTS})
    def "Run Doxygen to generate simple documentation with a default template"() {
        given:
        dox.configure {
            source new File(TESTFSREADROOT,'sample-cpp')
            outputDir new File(TESTFSWRITEROOT,'docs')

            generate_xml   false
            generate_latex false
            generate_html  true
            have_dot       false

            executables {
                dot OS.windows ? 'C:/path/to/dot' :'/path/to/dot'
            }
        }

        dox.exec()

        expect:
        new File(TESTFSWRITEROOT,'docs/html').exists()
        new File(TESTFSWRITEROOT,'docs/html/index.html').exists()
        dox.doxygenProperties['DOT_PATH'] == new File( OS.windows ? 'C:/path/to/dot' : '/path/to/dot').absolutePath
    }


    @IgnoreIf({DownloadTestSpecification.SKIP_TESTS})
    def "When 'template' is supplied as a string, configuration should still work"() {
        given: 'A task configured with a custom template which is supplied as a string'
        dox.configure {
            source "${TESTFSREADROOT}/sample-cpp"
            outputDir "${TESTFSWRITEROOT}/docs"

            generate_xml   false
            generate_latex false
            generate_html  true
            have_dot       false

            template DOXY_TEMPLATE.absolutePath
        }

        when: 'The task is executed'
        dox.exec()
        def lines = new File(project.buildDir,'tmp/DoxygenExecutableSpec.doxyfile').text.readLines()

        then: 'The HTML files should have been created'
        new File(TESTFSWRITEROOT,'docs/html').exists()
        new File(TESTFSWRITEROOT,'docs/html/index.html').exists()

        and: 'Lines from the custom template should have been used'
        lines.find { 'FILE_PATTERNS ='}

        and: 'The custom template should be one of the task input files'
        dox.inputs.files.contains(DOXY_TEMPLATE)
    }

}