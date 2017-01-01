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

package org.ysb33r.gradle.doxygen.helper

import org.gradle.internal.os.OperatingSystem
import spock.lang.Specification


/**
 * @author Schalk W. Cronjé
 */
class DownloadTestSpecification extends Specification {
    static final String DOX_VERSION = System.getProperty('DOX_VERSION') ?: '1.8.8'
    static final File DOXYGEN_CACHE_DIR = new File( System.getProperty('DOXYGEN_CACHE_DIR') ?: './build/doxygen-binaries').absoluteFile
    static final OperatingSystem OS = OperatingSystem.current()
    static final boolean SKIP_TESTS = !(OS.isMacOsX() || OS.isLinux() || OS.isWindows())
}