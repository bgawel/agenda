package agenda

class InstitutionProjectionController {

    static responseFormats = ['json']

    def names() {
        respond Institution.findAllByInternalAndEnabled(false, true, [sort: 'name'])
            .collect { [id: it.id, name: it.name] }
    }
}
