package agenda

import static org.springframework.http.HttpStatus.BAD_REQUEST
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.OK
import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

@Secured(['IS_AUTHENTICATED_FULLY'])
class FileUploadController {

    def imageConverterService
    def staticResourceService

    def evntPic() {
        def multipartFile = request.getFile('file')
        if (!multipartFile || multipartFile.empty) {
            log.error "Could not retrieve file from request, is-empty: ${multipartFile?.empty}"
            render status: BAD_REQUEST
        } else {
            def tmpFile = createTempFile('pic', "-${multipartFile.originalFilename}")
            if (tmpFile) {
                if (imageConverterService.resize(multipartFile.inputStream, multipartFile.originalFilename, tmpFile)) {
                    def pathToImg = [fileId: staticResourceService.makeImageId(tmpFile.path)]
                    render pathToImg as JSON
                    return
                }
            }
            render status: INTERNAL_SERVER_ERROR
        }
    }

    private createTempFile(prefix, suffix) {
        def tmpFile
        try {
            tmpFile = session.createTempFile(prefix, suffix)
        } catch (e) {
            log.error 'Could not create a temp file', e
        }
        tmpFile
    }
}
