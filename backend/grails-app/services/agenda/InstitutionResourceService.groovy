package agenda

class InstitutionResourceService {

    def show(id) {
        def inst = Institution.get(id)
        if (inst) {
            convert(inst)
        }
    }

    def save(inst) {
        convert(persistInst(inst))
    }

    def update(inst) {
        convert(persistInst(inst))
    }

    def delete(inst) {
        inst.delete()
    }

    private convert(inst) {
        [id: inst.id, name: inst.name, email: inst.email, password: inst.password, address: inst.address,
            web: inst.web, telephone: inst.telephone]
    }

    private persistInst(inst) {
        inst.save(validate: false)
    }
}
