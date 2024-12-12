package org.apache.ambari.server.stack.upgrade.orchestrate;
enum Placeholder {

    OTHER(""),
    HOST_ALL("hosts.all"),
    HOST_MASTER("hosts.master"),
    VERSION("version"),
    DIRECTION_TEXT("direction.text"),
    DIRECTION_TEXT_PROPER("direction.text.proper"),
    DIRECTION_PAST("direction.past"),
    DIRECTION_PAST_PROPER("direction.past.proper"),
    DIRECTION_PLURAL("direction.plural"),
    DIRECTION_PLURAL_PROPER("direction.plural.proper"),
    DIRECTION_VERB("direction.verb"),
    DIRECTION_VERB_PROPER("direction.verb.proper");
    private java.lang.String pattern;

    Placeholder(java.lang.String key) {
        pattern = ("{{" + key) + "}}";
    }

    static org.apache.ambari.server.stack.upgrade.orchestrate.Placeholder find(java.lang.String pattern) {
        for (org.apache.ambari.server.stack.upgrade.orchestrate.Placeholder p : org.apache.ambari.server.stack.upgrade.orchestrate.Placeholder.values()) {
            if (p.pattern.equals(pattern)) {
                return p;
            }
        }
        return org.apache.ambari.server.stack.upgrade.orchestrate.Placeholder.OTHER;
    }
}