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