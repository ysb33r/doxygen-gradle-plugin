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
        new File(gotIt,'Contents').exists()

        when: "The doxygen executable is run to display the help page"
        project.exec {
            executable dwn.doxygenExecutablePath
            args '-h'
        }

        then: "No runtime error is expected"
        true
    }
}