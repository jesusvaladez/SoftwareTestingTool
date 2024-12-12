package org.apache.ambari.server.state;
public enum RepositoryVersionState {

    NOT_REQUIRED(0),
    INSTALLING(3),
    INSTALLED(2),
    INSTALL_FAILED(5),
    OUT_OF_SYNC(4),
    CURRENT(1);
    private final int weight;

    private RepositoryVersionState(int weight) {
        this.weight = weight;
    }

    public static org.apache.ambari.server.state.RepositoryVersionState getAggregateState(java.util.List<org.apache.ambari.server.state.RepositoryVersionState> states) {
        if ((null == states) || states.isEmpty()) {
            return org.apache.ambari.server.state.RepositoryVersionState.NOT_REQUIRED;
        }
        org.apache.ambari.server.state.RepositoryVersionState heaviestState = states.get(0);
        for (org.apache.ambari.server.state.RepositoryVersionState state : states) {
            if (state.weight > heaviestState.weight) {
                heaviestState = state;
            }
        }
        return heaviestState;
    }
}