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
import org.gradle.internal.os.OperatingSystem
import org.gradle.testfixtures.ProjectBuilder
import org.ysb33r.gradle.doxygen.helper.DownloadTestSpecification
import spock.lang.IgnoreIf
import spock.lang.Specification


/**
 */
class DownloaderSpec extends DownloadTestSpecification {

    Project project = ProjectBuilder.builder().build()

    @IgnoreIf({ DownloadTestSpecification.SKIP_TESTS })
    def "Download a Doxygen executable" () {
        given: "A requirement to download Doxygen #DOX_VERSION"
        Downloader dwn = new Downloader(DOX_VERSION,project)
        dwn.downloadRoot = new File(project.buildDir,'download')
        dwn.baseURI = DOXYGEN_CACHE_DIR.toURI()

        when: "The distribution root is requested"
        File gotIt = dwn.distributionRoot

        then: "The distribution is downloaded and unpacked"
        gotIt != null
        if(OS.windows) {
            new File(gotIt,'doxygen.exe').exists()
        } else if(OS.linux) {
            new File(gotIt,'bin').exists()
        } else if(OS.macOsX) {
            new File(gotIt,'Contents').exists()
        }

        when: "The doxygen executable is run to display the help page"
        project.exec {
            executable dwn.doxygenExecutablePath
            args '-h'
        }

        then: "No runtime error is expected"
        true
    }
}