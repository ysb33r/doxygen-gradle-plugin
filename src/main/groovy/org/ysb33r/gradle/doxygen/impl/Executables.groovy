//
// ============================================================================
// (C) Copyright Schalk W. Cronje 2013-2015
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

/**
 * Created by schalkc on 23/05/2014.
 */
class Executables {

    static final def EXECUTABLES = [
            'doxygen' : '',
            'mscgen'  : 'MSCGEN_PATH',
            'dot'     : 'DOT_PATH',
            'perl'    : 'PERL_PATH',
            'hhc'     : 'HHC_LOCATION'
    ]

    private def mapToUpdate

    Executables( def map ) {
        mapToUpdate = map
    }

    def methodMissing( String name, args ) {

        if( args.size() == 1 && EXECUTABLES[name] != null ) {
            switch (args[0]) {
                case File:
                    return mapToUpdate[name] = args[0].absolutePath
                default:
                    return mapToUpdate[name] = args[0].toString()
            }
        }

        throw new MissingMethodException(name,Executables.class,args)
    }


}
