package org.apache.ambari.server.security.authorization;
import static org.easymock.EasyMock.anyLong;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
public class AuthorizationHelperInitializer {
    public static void viewInstanceDAOReturningNull() {
        com.google.inject.Provider viewInstanceDAOProvider = EasyMock.createNiceMock(com.google.inject.Provider.class);
        org.apache.ambari.server.orm.dao.ViewInstanceDAO viewInstanceDAO = EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.ViewInstanceDAO.class);
        EasyMock.expect(viewInstanceDAOProvider.get()).andReturn(viewInstanceDAO).anyTimes();
        EasyMock.expect(viewInstanceDAO.findByResourceId(EasyMock.anyLong())).andReturn(null).anyTimes();
        EasyMock.replay(viewInstanceDAOProvider, viewInstanceDAO);
        org.apache.ambari.server.security.authorization.AuthorizationHelper.viewInstanceDAOProvider = viewInstanceDAOProvider;
    }
}