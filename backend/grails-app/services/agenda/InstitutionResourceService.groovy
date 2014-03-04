package agenda

class InstitutionResourceService {

    def save(inst) {
        persistInst(inst)
    }

    def update(inst) {
        persistInst(inst)
    }

    private persistInst(inst) {
        inst.save(validate: false)
    }
}
