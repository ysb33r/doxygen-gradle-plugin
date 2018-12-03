//
// ============================================================================
// (C) Copyright Schalk W. Cronje 2013-2018
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

import org.gradle.api.logging.LogLevel
import spock.lang.*
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder

class DoxygenTaskSpec extends spock.lang.Specification {

    Project project = ProjectBuilder.builder().build()
    def dox = project.task('doxygen', type: Doxygen )

    def "Setting specific Doxygen properties that take boolean values"() {
        given:

            dox.configure {
                quiet true
                warnings false
                recursive true
                subgrouping false
            }

        expect:
            dox.doxygenProperties[doxName] == doxValue

        where:
            doxName      | doxValue
            'QUIET'      | 'YES'
            'WARNINGS'   | 'NO'
            'RECURSIVE'  | 'YES'
            'SUBGROUPING'| 'NO'
    }

    def "Using 'input' should throw an exception"() {
        when:
            dox.configure {
                input '/this/path'
            }

        then:
            thrown(DoxygenException)
    }

    def "Using 'mscgen_path' should throw an exception"() {
        when:
            dox.configure {
                mscgen_path '/this/path'
            }

        then:
            thrown(DoxygenException)
    }

    def "Must be able to set executable paths via executables closure"() {
        given:
            dox.configure {
                executables {
                    doxygen path : '/path/to/doxygen'
                    mscgen  '/path/to/mscgen'
                }
            }

        expect:
            dox.executables.doxygen.call() == '/path/to/doxygen'
            dox.executables.mscgen  == '/path/to/mscgen'

    }

    def "Only supported executables must be configurable"() {
        when:
            dox.configure {
                executables {
                    foobar '/path/to/foo'
                }
            }

        then:
            thrown(DoxygenException)
    }

    def "Only lower case equivalents of Doxygen properties are allowed"() {
        when:
            dox.configure {
                'OUTPUT_LANGUAGE' 'English'
            }

        then:
            thrown(DoxygenException)
    }

    def "Lower case equivalents of Doxygen properties should update final properties"() {
        given:
            dox.configure {

                output_language      'English'
                tab_size              2
                inherit_docs          true
                separate_member_pages false
                project_logo          new File('src/resources/logo.png')
                file_patterns         '*.c', '*.cpp'
                project_brief         'This is a description with spaces'
            }

        expect:
            dox.doxygenProperties[doxName] == doxValue

        where:
            doxName                | doxValue
            'OUTPUT_LANGUAGE'      | 'English'
            'TAB_SIZE'             | '2'
            'INHERIT_DOCS'         | 'YES'
            'SEPARATE_MEMBER_PAGES'| 'NO'
            'PROJECT_LOGO'         | new File('src/resources/logo.png').absolutePath
            'FILE_PATTERNS'        | '*.c *.cpp'
            'PROJECT_BRIEF'        | '"This is a description with spaces"'
    }

    @Unroll
    def "Default Doxygen properties should be set for #doxName"() {
        given:
            Project proj = ProjectBuilder.builder().withName('DoxygenTaskSpec').build()
            proj.version  = '1.1'
            proj.buildDir = 'build/foo'
            def defdox = proj.task('doxygen', type: Doxygen )


        expect:
            defdox.doxygenProperties[doxName] == doxValue

        where:
            doxName                | doxValue
            'PROJECT_NAME'         | 'DoxygenTaskSpec'
    }

    def "Setting image_path should also update the input files (not source files)"() {
        given:
            dox.configure {
                image_path project.file('src/non-existing1')
                image_path "${project.projectDir}/src/non-existing2"
            }
            project.version = '1.999'
            dox.setDefaults()

        expect:
            dox.inputs.files.contains(project.file('src/non-existing1'))
            dox.inputs.files.contains(project.file('src/non-existing2'))
            dox.doxygenProperties['IMAGE_PATH'] == project.file('src/non-existing1').absolutePath + ' ' +
                project.file('src/non-existing2').absolutePath
            dox.doxygenProperties['PROJECT_NUMBER'] == '1.999'
    }
}

