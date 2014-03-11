package agenda

class InstitutionProjectionController {

    static responseFormats = ['json']

    def names() {
        respond Institution.list(sort: 'name').collect { [id: it.id, name: it.name] }
    }
}
