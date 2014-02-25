package agenda

import groovy.transform.ToString;
import groovy.transform.EqualsAndHashCode;

@ToString(includeNames=true, includeFields=true)
@EqualsAndHashCode(includes='id')
class Event {

    String title
    String pic
    String more
    String description
    Category category
    
    Date dateCreated
    Date lastUpdated
    
    Boolean oneTimeType
    
    List pdtps = []
    static hasMany = [pdtps: Pdtp]
    static belongsTo = [institution: Institution]
    
    static constraints = {
        title maxSize: 64, blank: false
        pic maxSize: 64, nullable: true
        more maxSize: 64, nullable: true
        description maxSize: 1800, nullable: true
        category nullable: false
        oneTimeType nullable: false
        pdtps minSize: 1, lazy: false
    }
    
    static mapping = {
        version false
        institution index: 'Inst_Idx'
    }
}
