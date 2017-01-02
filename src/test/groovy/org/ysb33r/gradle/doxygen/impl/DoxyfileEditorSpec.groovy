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

package org.ysb33r.gradle.doxygen.impl

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification


class DoxyfileEditorSpec extends Specification {
    static final File SRC_DOXYFILE = new File( System.getProperty('TESTFSREADROOT') ?: 'src/test/resources','DoxyfileEditor.dox' )
    static final File WRITEABLE_DOXYFILE = new File( System.getProperty('TESTFSWRITEROOT') ?: 'build/tmp/test','editor/DoxyfileEditor.dox' )

    Project project = ProjectBuilder.builder().build()
    DoxyfileEditor editor = new DoxyfileEditor( logger : project.logger )
    DoxygenProperties replacements = new DoxygenProperties()

    void setup() {
        if(WRITEABLE_DOXYFILE.parentFile.exists()) {
            WRITEABLE_DOXYFILE.parentFile.deleteDir()
        }

        WRITEABLE_DOXYFILE.parentFile.mkdirs()

        WRITEABLE_DOXYFILE.text = SRC_DOXYFILE.text
    }

    def "Default action will removes comments and collapse +="() {
        given:
            replacements.setProperty 'CREATE_SUBDIRS', true
            replacements.setProperty 'PROJECT_NUMBER', '1.11'
            editor.update(replacements.properties,WRITEABLE_DOXYFILE)
            def lines = []
            WRITEABLE_DOXYFILE.eachLine { lines.add(it) }

        expect:
            lines.find { it == 'CREATE_SUBDIRS = YES'}
            !lines.find { it.startsWith('#') }
            lines.find { it =~ /FILE_PATTERNS\s+=\s+\*\.c\s+\*\.cpp\s+\*\.cxx\s+\*\.cc/ }
            lines.find { it == 'PROJECT_NUMBER = 1.11' }
    }
}