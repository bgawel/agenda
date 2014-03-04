package agenda

class MenuController {

    static responseFormats = ['json']

    def weekMenuService
    def categoryMenuService

    def week() {
        respond weekMenuService.week
    }

    def categories() {
        respond categoryMenuService.categories
    }
}
