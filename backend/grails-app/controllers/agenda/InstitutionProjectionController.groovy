package agenda

class InstitutionProjectionController {

    static responseFormats = ['json']

    def names() {
        respond Institution.findAllByInternal(false, [sort: 'name']).collect { [id: it.id, name: it.name] }
    }
}
