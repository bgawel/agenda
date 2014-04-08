package agenda

import org.apache.commons.io.FilenameUtils
import org.apache.commons.io.IOUtils

class StaticResourceController {

    def staticResourceService
    def cacheHeadersService

    def img() {
        def file = new File("${staticResourceService.retrievePathToImageDir()}/${params.pic}")
        def ext = FilenameUtils.getExtension(params.pic) ?: 'jpeg'
        response.contentType = "image/$ext"
        response.status = 200
        cacheHeadersService.cache response, [neverExpires: true, shared: true]
        def ostream = response.getOutputStream()
        IOUtils.copy(new FileInputStream(file), ostream)
        ostream.flush()
        ostream.close()
    }
}
