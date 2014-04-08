package agenda

import grails.util.Holders

import javax.annotation.PostConstruct

import org.apache.commons.io.FileUtils
import org.apache.commons.io.FilenameUtils

class StaticResourceService {

    static transactional = false

    def appUrlService
    private config

    def makeImageSrc(imgName) {
        if (imgName) {
            "${appUrlService.makeServerUrl()}/${retrieveImageContext()}/$imgName"
        }
    }

    def copyToImageDir(imgId, newImgName) {
        def srcFile = new File(imgId)
        if (!srcFile.exists()) {
            throw new FileNotFoundException("Source file $srcFile does not exist")
        }
        def destFile = makeImageFile(newImgName)
        FileUtils.copyFile(srcFile, destFile)
        if (!destFile.exists()) {
            throw new FileNotFoundException("Destination file $destFile does not exist")
        } else {
            log.info "Copied a new image $newImgName"
        }
    }

    def deleteImage(imgName) {
        if (imgName) {
            def file = makeImageFile(imgName)
            if (file.exists()) {
                def wasDeleted = file.delete()
                if (!wasDeleted) {
                    log.warn "File $file could not be deleted"
                } else {
                    log.info "Deleted an image $imgName"
                }
            } else {
                log.info "File $file does not exist"
            }
        }
    }

    def makeImageId(path) {
        path
    }

    def getNameFromImageId(imgId) {
        def tmpName = FilenameUtils.getName(imgId)
        def indexOfRealName = tmpName.indexOf('-')
        if (indexOfRealName) {
            tmpName[indexOfRealName..-1]
        } else {
            tmpName
        }
    }

    private makeImageFile(imgName) {
        new File("${retrievePathToImageDir()}/$imgName")
    }

    private retrieveImageContext() {
        config.context
    }

    def retrievePathToImageDir() {
        config.dir
    }

    @PostConstruct
    void init() {
        config = Holders.grailsApplication.config.agenda.image
    }
}
