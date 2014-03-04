package agenda

class CategoryController {

    static responseFormats = ['json']

    def all() {
        respond Category.all
    }
}
