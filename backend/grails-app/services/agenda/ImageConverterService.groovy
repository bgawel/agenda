package agenda

import static org.apache.commons.io.FilenameUtils.getExtension

import javax.imageio.ImageIO

import org.imgscalr.Scalr

class ImageConverterService {

    static transactional = false

    def targetWidth = 285
    def targetHeight = 252

    def resize(inputStream, originalFileName, outputFile) {
        def inImage = ImageIO.read(inputStream)
        def outImage = Scalr.resize(inImage, Scalr.Method.SPEED, Scalr.Mode.FIT_TO_WIDTH, targetWidth, targetHeight,
            Scalr.OP_ANTIALIAS, Scalr.OP_BRIGHTER)
        def format = getExtension(originalFileName)
        def written = ImageIO.write(outImage, format, outputFile)
        if (!written) {
            log.error "Could not write image for format = $format"
        }
        written
    }
}
