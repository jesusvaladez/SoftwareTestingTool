package org.apache.ambari.server.topology;
public class Cardinality {
    java.lang.String cardinality;

    int min = 0;

    int max = java.lang.Integer.MAX_VALUE;

    int exact = -1;

    boolean isAll = false;

    public Cardinality(java.lang.String cardinality) {
        this.cardinality = cardinality;
        if ((cardinality != null) && (!cardinality.isEmpty())) {
            if (cardinality.contains("+")) {
                min = java.lang.Integer.parseInt(cardinality.split("\\+")[0]);
            } else if (cardinality.contains("-")) {
                java.lang.String[] toks = cardinality.split("-");
                min = java.lang.Integer.parseInt(toks[0]);
                max = java.lang.Integer.parseInt(toks[1]);
            } else if (cardinality.equals("ALL")) {
                isAll = true;
            } else {
                exact = java.lang.Integer.parseInt(cardinality);
            }
        }
    }

    public boolean isAll() {
        return isAll;
    }

    public boolean isValidCount(int count) {
        if (isAll) {
            return false;
        } else if (exact != (-1)) {
            return count == exact;
        } else
            return (count >= min) && (count <= max);

    }

    public boolean supportsAutoDeploy() {
        return isValidCount(1) || isAll;
    }

    public java.lang.String getValue() {
        return cardinality;
    }
}